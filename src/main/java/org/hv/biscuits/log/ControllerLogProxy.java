package org.hv.biscuits.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hv.biscuits.constant.BiscuitsHttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hv.biscuits.constant.BiscuitsHttpHeaders.BUSINESS_DEPARTMENT_NAME;
import static org.hv.biscuits.constant.BiscuitsHttpHeaders.TRACE_ID;
import static org.hv.biscuits.constant.BiscuitsHttpHeaders.USER_NAME;

/**
 * @author leyan95
 */
@Component
@Aspect
public class ControllerLogProxy {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LogQueue logQueue;
    @Resource(name = "controllerLogThreadPool")
    private ExecutorService executorService;
    private final Map<String, AccessorLogView> accessorLogViewMap = new ConcurrentHashMap<>(92);
    private final Map<String, String> resultMap = new ConcurrentHashMap<>(92);
    private final Map<String, CountDownLatch> countDownLatchMap = new ConcurrentHashMap<>(92);

    public ControllerLogProxy(LogQueue logQueue) {
        this.logQueue = logQueue;
    }

    @Pointcut("@within(org.hv.biscuits.annotation.Controller)")
    public void verify() {
    }

    @Around("verify()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        if (servletRequestAttributes != null) {
            HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
            String traceId = httpServletRequest.getHeader(BiscuitsHttpHeaders.TRACE_ID);
            String userName = httpServletRequest.getHeader(BiscuitsHttpHeaders.USER_NAME);
            String businessDepartmentName = httpServletRequest.getHeader(BiscuitsHttpHeaders.BUSINESS_DEPARTMENT_NAME);
            logger.info("[{}: {}, {}: {}, {}: {}]", TRACE_ID, traceId, USER_NAME, userName, BUSINESS_DEPARTMENT_NAME, businessDepartmentName);
            if (traceId != null && userName != null) {
                String requestId = UUID.randomUUID().toString().replace("-", "");
                this.countDownLatchMap.put(requestId, new CountDownLatch(2));
                this.executorService.execute(() -> {
                    AccessorLogView accessorLogView = new AccessorLogView().setTraceId(traceId).setRequestId(requestId);
                    try {
                        accessorLogView.setUserName(URLDecoder.decode(userName, "UTF-8"));
                        if (businessDepartmentName != null) {
                            accessorLogView.setBusinessDeptName(URLDecoder.decode(businessDepartmentName, "UTF-8"));
                        }
                        Class<?> clazz = joinPoint.getTarget().getClass();
                        accessorLogView.setAccessorName(clazz.getTypeName());
                        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
                        accessorLogView.setMethodName(method.getName());
                        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
                        String[] argsName = discoverer.getParameterNames(method);
                        if (argsName != null && argsName.length > 0) {
                            String[] argsLog = new String[argsName.length];
                            Object[] argsValue = joinPoint.getArgs();
                            for (int argIndex = 0; argIndex < argsName.length; argIndex++) {
                                argsLog[argIndex] = argsName[argIndex] + ": " + objectMapper.writeValueAsString(argsValue[argIndex]);
                            }
                            accessorLogView.setInParameter(StringUtils.join(argsLog, ", "));
                        }
                        this.accessorLogViewMap.put(requestId, accessorLogView);
                    } catch (UnsupportedEncodingException e) {
                        logger.error("URLDecoder 解析头信息失败 {}", e.getMessage());
                    } catch (JsonProcessingException e) {
                        logger.error("ObjectMapper 序列化参数失败 {}", e.getMessage());
                    } finally {
                        this.countDownLatchMap.get(requestId).countDown();
                    }
                });
                try {
                    result = joinPoint.proceed();
                    if (result != null) {
                        this.resultMap.put(requestId, objectMapper.writeValueAsString(result));
                    }
                    this.countDownLatchMap.get(requestId).countDown();
                    this.executorService.execute(() -> {
                        try {
                            this.countDownLatchMap.get(requestId).await(100, TimeUnit.MILLISECONDS);
                            this.accessorLogViewMap.get(requestId).setOutParameter(this.resultMap.get(requestId));
                        } catch (InterruptedException e) {
                            logger.error("日志线程被异常中断 {} \n {}", Thread.currentThread().getName(), e.getMessage());
                        } finally {
                            logQueue.offerAccessorLog(this.accessorLogViewMap.get(requestId).setEndDateTime(LocalDateTime.now()));
                            this.resultMap.remove(requestId);
                            this.countDownLatchMap.remove(requestId);
                            this.accessorLogViewMap.remove(requestId);
                        }
                    });
                } catch (Throwable throwable) {
                    this.countDownLatchMap.get(requestId).countDown();
                    this.executorService.execute(() -> {
                        try {
                            this.countDownLatchMap.get(requestId).await(100, TimeUnit.MILLISECONDS);
                            this.accessorLogViewMap.get(requestId).setException(objectMapper.writeValueAsString(throwable));
                        } catch (InterruptedException | JsonProcessingException e) {
                            logger.error("日志线程被异常中断 {} 或 \n 反序列化长信息失败 {}", Thread.currentThread().getName(), throwable);
                        } finally {
                            logQueue.offerAccessorLog(this.accessorLogViewMap.get(requestId).setEndDateTime(LocalDateTime.now()));
                            this.resultMap.remove(requestId);
                            this.countDownLatchMap.remove(requestId);
                            this.accessorLogViewMap.remove(requestId);
                        }
                    });
                }
                return result;
            } else {
                return joinPoint.proceed();
            }
        } else {
            return joinPoint.proceed();
        }
    }
}

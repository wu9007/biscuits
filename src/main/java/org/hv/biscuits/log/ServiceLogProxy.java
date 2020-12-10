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
import org.hv.biscuits.log.model.ServiceLogView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hv.biscuits.constant.BiscuitsHttpHeaders.REQUEST_ID;
import static org.hv.biscuits.constant.BiscuitsHttpHeaders.SERVICE_ID;
import static org.hv.biscuits.constant.BiscuitsHttpHeaders.TRANSACTION_ID;

/**
 * @author wujianchuan
 */
@Component
@Aspect
public class ServiceLogProxy {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Resource
    private LogQueue logQueue;
    @Resource(name = "serviceLogThreadPool")
    private ExecutorService executorService;
    private final Map<String, ServiceLogView> serviceLogViewMap = new ConcurrentHashMap<>(92);
    private final Map<String, String> resultMap = new ConcurrentHashMap<>(92);
    private final Map<String, CountDownLatch> countDownLatchMap = new ConcurrentHashMap<>(92);

    @Pointcut("@within(org.hv.biscuits.annotation.Service)")
    public void verify() {
    }

    @Around("verify()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        if (servletRequestAttributes != null) {
            HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
            String requestId = (String) httpServletRequest.getAttribute(BiscuitsHttpHeaders.REQUEST_ID);
            String transactionId = (String) httpServletRequest.getAttribute(BiscuitsHttpHeaders.TRANSACTION_ID);
            if (requestId != null) {
                logger.info("[{}: {}, {}: {}]", REQUEST_ID, requestId, TRANSACTION_ID, transactionId);
                String serviceId = UUID.randomUUID().toString().replace("-", "");
                httpServletRequest.setAttribute(SERVICE_ID, serviceId);
                this.countDownLatchMap.put(serviceId, new CountDownLatch(1));
                this.executorService.execute(() -> {
                    ServiceLogView serviceLogView = new ServiceLogView().setRequestId(requestId).setServiceId(serviceId).setGlobalTransactionId(transactionId);
                    try {
                        Class<?> clazz = joinPoint.getTarget().getClass();
                        serviceLogView.setServiceName(clazz.getTypeName());
                        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
                        serviceLogView.setMethodName(method.getName());
                        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
                        String[] argsName = discoverer.getParameterNames(method);
                        if (argsName != null && argsName.length > 0) {
                            String[] argsLog = new String[argsName.length];
                            Object[] argsValue = joinPoint.getArgs();
                            for (int argIndex = 0; argIndex < argsName.length; argIndex++) {
                                argsLog[argIndex] = argsName[argIndex] + ": " + objectMapper.writeValueAsString(argsValue[argIndex]);
                            }
                            serviceLogView.setInParameter(StringUtils.join(argsLog, ", "));
                        }
                        this.serviceLogViewMap.put(serviceId, serviceLogView);
                    } catch (JsonProcessingException e) {
                        logger.error("ObjectMapper 序列化参数失败 {}", e.getMessage());
                    } finally {
                        this.countDownLatchMap.get(serviceId).countDown();
                    }
                });
                try {
                    result = joinPoint.proceed();
                    if (result != null) {
                        this.resultMap.put(serviceId, objectMapper.writeValueAsString(result));
                    }
                    this.executorService.execute(() -> {
                        try {
                            this.countDownLatchMap.get(serviceId).await(100, TimeUnit.MILLISECONDS);
                            this.serviceLogViewMap.get(serviceId).setOutParameter(this.resultMap.get(serviceId));
                        } catch (InterruptedException e) {
                            logger.error("日志线程被异常中断 {} \n {}", Thread.currentThread().getName(), e.getMessage());
                        } finally {
                            logQueue.offerServiceLog(this.serviceLogViewMap.get(serviceId).setEndDateTime(LocalDateTime.now()));
                            this.resultMap.remove(serviceId);
                            this.countDownLatchMap.remove(serviceId);
                            this.serviceLogViewMap.remove(serviceId);
                        }
                    });
                } catch (Throwable throwable) {
                    this.countDownLatchMap.get(serviceId).countDown();
                    this.executorService.execute(() -> {
                        try {
                            this.countDownLatchMap.get(serviceId).await(100, TimeUnit.MILLISECONDS);
                            this.serviceLogViewMap.get(serviceId).setException(objectMapper.writeValueAsString(throwable));
                        } catch (InterruptedException | JsonProcessingException e) {
                            logger.error("日志线程被异常中断 {} 或 \n 反序列化长信息失败 {}", Thread.currentThread().getName(), throwable.getMessage());
                        } finally {
                            logQueue.offerServiceLog(this.serviceLogViewMap.get(serviceId).setEndDateTime(LocalDateTime.now()));
                            this.resultMap.remove(serviceId);
                            this.countDownLatchMap.remove(serviceId);
                            this.serviceLogViewMap.remove(serviceId);
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

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
import org.hv.biscuits.log.model.PersistenceLogView;
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

import static org.hv.biscuits.constant.BiscuitsHttpHeaders.PERSISTENCE_ID;
import static org.hv.biscuits.constant.BiscuitsHttpHeaders.REQUEST_ID;
import static org.hv.biscuits.constant.BiscuitsHttpHeaders.SERVICE_ID;
import static org.hv.biscuits.constant.BiscuitsHttpHeaders.TRANSACTION_ID;

/**
 * @author wujianchuan
 */
@Component
@Aspect
public class PersistenceLogProxy {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Resource
    private LogQueue logQueue;
    @Resource(name = "persistenceLogThreadPool")
    private ExecutorService executorService;
    private final Map<String, PersistenceLogView> persistenceLogViewMap = new ConcurrentHashMap<>(92);
    private final Map<String, String> resultMap = new ConcurrentHashMap<>(92);
    private final Map<String, CountDownLatch> countDownLatchMap = new ConcurrentHashMap<>(92);

    @Pointcut("target(org.hv.biscuits.repository.AbstractRepository) " +
            "&& !execution(public void pourSession()) " +
            "&& !execution(public void injectSession(*)) " +
            "&& !execution(public * getSession())")
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
            String serviceId = (String) httpServletRequest.getAttribute(SERVICE_ID);
            logger.info("[{}: {}, {}: {}, {}: {}]", REQUEST_ID, requestId, TRANSACTION_ID, transactionId, SERVICE_ID, serviceId);
            if (serviceId != null) {
                String persistenceId = UUID.randomUUID().toString().replace("-", "");
                httpServletRequest.setAttribute(PERSISTENCE_ID, persistenceId);
                this.countDownLatchMap.put(persistenceId, new CountDownLatch(1));
                this.executorService.execute(() -> {
                    PersistenceLogView persistenceLogView = new PersistenceLogView(serviceId).setPersistenceId(persistenceId).setServiceId(serviceId).setGlobalTransactionId(transactionId);
                    try {
                        Class<?> clazz = joinPoint.getTarget().getClass();
                        persistenceLogView.setPersistenceName(clazz.getTypeName());
                        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
                        persistenceLogView.setMethodName(method.getName());
                        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
                        String[] argsName = discoverer.getParameterNames(method);
                        if (argsName != null && argsName.length > 0) {
                            String[] argsLog = new String[argsName.length];
                            Object[] argsValue = joinPoint.getArgs();
                            for (int argIndex = 0; argIndex < argsName.length; argIndex++) {
                                argsLog[argIndex] = argsName[argIndex] + ": " + objectMapper.writeValueAsString(argsValue[argIndex]);
                            }
                            persistenceLogView.setInParameter(StringUtils.join(argsLog, ", "));
                        }
                        this.persistenceLogViewMap.put(persistenceId, persistenceLogView);
                    } catch (JsonProcessingException e) {
                        logger.error("ObjectMapper 序列化参数失败 {}", e.getMessage());
                    } finally {
                        this.countDownLatchMap.get(persistenceId).countDown();
                    }
                });
                try {
                    result = joinPoint.proceed();
                    if (result != null) {
                        this.resultMap.put(persistenceId, objectMapper.writeValueAsString(result));
                    }
                    this.executorService.execute(() -> {
                        try {
                            this.countDownLatchMap.get(persistenceId).await(100, TimeUnit.MILLISECONDS);
                            this.persistenceLogViewMap.get(persistenceId).setOutParameter(this.resultMap.get(persistenceId));
                        } catch (InterruptedException e) {
                            logger.error("日志线程被异常中断 {} \n {}", Thread.currentThread().getName(), e.getMessage());
                        } finally {
                            logQueue.offerPersistenceLog(this.persistenceLogViewMap.get(persistenceId).setEndDateTime(LocalDateTime.now()));
                            this.resultMap.remove(persistenceId);
                            this.countDownLatchMap.remove(persistenceId);
                            this.persistenceLogViewMap.remove(persistenceId);
                        }
                    });
                } catch (Throwable throwable) {
                    this.countDownLatchMap.get(persistenceId).countDown();
                    this.executorService.execute(() -> {
                        try {
                            this.countDownLatchMap.get(persistenceId).await(100, TimeUnit.MILLISECONDS);
                            this.persistenceLogViewMap.get(persistenceId).setException(objectMapper.writeValueAsString(throwable));
                        } catch (InterruptedException | JsonProcessingException e) {
                            logger.error("日志线程被异常中断 {} 或 \n 反序列化长信息失败 {}", Thread.currentThread().getName(), throwable.getMessage());
                        } finally {
                            logQueue.offerPersistenceLog(this.persistenceLogViewMap.get(persistenceId).setEndDateTime(LocalDateTime.now()));
                            this.resultMap.remove(persistenceId);
                            this.countDownLatchMap.remove(persistenceId);
                            this.persistenceLogViewMap.remove(persistenceId);
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

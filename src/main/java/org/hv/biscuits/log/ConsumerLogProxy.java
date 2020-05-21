package org.hv.biscuits.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.common.message.MessageExt;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
public class ConsumerLogProxy {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LogQueue logQueue;
    private final Map<String, AccessorLogView> accessorLogViewMap = new ConcurrentHashMap<>(92);
    private final Map<String, CountDownLatch> countDownLatchMap = new ConcurrentHashMap<>(92);
    private final Map<String, String> resultMap = new ConcurrentHashMap<>(92);

    @Resource(name = "consumerLogThreadPool")
    private ExecutorService executorService;

    public ConsumerLogProxy(LogQueue logQueue) {
        this.logQueue = logQueue;
    }

    @Pointcut("target(org.hv.biscuits.message.BisRocketMqListener) || target(org.hv.biscuits.message.BisRocketMqReplyListener)")
    public void verify() {
    }

    @Around("verify()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        String requestId = UUID.randomUUID().toString().replace("-", "");
        this.countDownLatchMap.put(requestId, new CountDownLatch(2));
        this.executorService.execute(() -> {
            MessageExt messageExt = ((MessageExt) joinPoint.getArgs()[0]);
            String traceId = messageExt.getProperty(TRACE_ID);
            String userName = messageExt.getProperty(USER_NAME);
            String businessDepartmentName = messageExt.getProperty(BUSINESS_DEPARTMENT_NAME);
            logger.info("[{}: {}, {}: {}, {}: {}]", TRACE_ID, traceId, USER_NAME, userName, BUSINESS_DEPARTMENT_NAME, businessDepartmentName);
            AccessorLogView accessorLogView = new AccessorLogView(traceId, requestId, userName, businessDepartmentName)
                    .setAccessorName(joinPoint.getTarget().getClass().getTypeName())
                    .setMethodName("execute")
                    .setInParameter(new String(messageExt.getBody()));
            this.accessorLogViewMap.put(requestId, accessorLogView);
            this.countDownLatchMap.get(requestId).countDown();
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
    }
}

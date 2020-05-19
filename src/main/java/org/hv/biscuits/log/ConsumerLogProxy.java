package org.hv.biscuits.log;

import org.apache.rocketmq.common.message.MessageExt;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hv.biscuits.constant.BiscuitsHttpHeaders.BUSINESS_DEPARTMENT_NAME;
import static org.hv.biscuits.constant.BiscuitsHttpHeaders.TRACE_ID;
import static org.hv.biscuits.constant.BiscuitsHttpHeaders.USER_NAME;

/**
 * @author wujianchuan
 */
@Component
@Aspect
public class ConsumerLogProxy {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final LogQueue logQueue;

    public ConsumerLogProxy(LogQueue logQueue) {
        this.logQueue = logQueue;
    }

    @Pointcut("target(org.hv.biscuits.message.AbstractRocketMqListener)")
    public void verify() {
    }

    @Around("verify()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        MessageExt messageExt = ((MessageExt) joinPoint.getArgs()[0]);
        String traceId = messageExt.getProperty(TRACE_ID);
        String requestId = UUID.randomUUID().toString().replace("-", "");
        String userName = messageExt.getProperty(USER_NAME);
        String businessDepartmentName = messageExt.getProperty(BUSINESS_DEPARTMENT_NAME);
        logger.info("[{}: {}, {}: {}, {}: {}]", TRACE_ID, traceId, USER_NAME, userName, BUSINESS_DEPARTMENT_NAME, businessDepartmentName);
        AccessorLogView accessorLogView = new AccessorLogView(traceId, requestId, userName, businessDepartmentName)
                .setAccessorName(joinPoint.getTarget().getClass().getTypeName())
                .setMethodName("execute")
                .setInParameter(new String(messageExt.getBody()));
        result = joinPoint.proceed();
        accessorLogView.setEndDateTime(LocalDateTime.now());
        this.logQueue.offerAccessorLog(accessorLogView);
        return result;
    }
}

package org.hv.biscuits.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hv.biscuits.constant.BiscuitsHttpHeaders;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author wujianchuan
 */
@Component
@Aspect
public class AccessorLogProxy {
    private final LogQueue logQueue;

    public AccessorLogProxy(LogQueue logQueue) {
        this.logQueue = logQueue;
    }

    @Pointcut("@within(org.hv.biscuits.annotation.Controller)")
    public void verify() {
    }

    @Around("verify()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        if (servletRequestAttributes != null) {
            HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
            String traceId = httpServletRequest.getHeader(BiscuitsHttpHeaders.TRACE_ID);
            String userName = httpServletRequest.getHeader(BiscuitsHttpHeaders.USER_NAME);
            String businessDepartmentName = httpServletRequest.getHeader(BiscuitsHttpHeaders.BUSINESS_DEPARTMENT_NAME);
            if (traceId != null && userName != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                String requestId = UUID.randomUUID().toString().replace("-", "");
                AccessorLogView accessorLogView = new AccessorLogView(traceId, requestId, URLDecoder.decode(userName, "UTF-8"));
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
                try {
                    result = joinPoint.proceed();
                    if (result != null) {
                        accessorLogView.setOutParameter(objectMapper.writeValueAsString(result));
                    }
                    return result;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    accessorLogView.setException(objectMapper.writeValueAsString(throwable));
                    throw throwable;
                } finally {
                    accessorLogView.setEndDateTime(LocalDateTime.now());
                    this.logQueue.offerAccessorLog(accessorLogView);
                }
            } else {
                return joinPoint.proceed();
            }
        } else {
            return joinPoint.proceed();
        }
    }
}

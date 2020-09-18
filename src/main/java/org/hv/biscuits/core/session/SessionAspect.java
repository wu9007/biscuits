package org.hv.biscuits.core.session;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hv.biscuits.annotation.Affairs;
import org.hv.biscuits.annotation.Service;
import org.hv.biscuits.annotation.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author leyan95 2020/09/10
 */
@Aspect
@Component
public class SessionAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionAspect.class);

    private final ApplicationContext context;

    public SessionAspect(ApplicationContext context) {
        this.context = context;
    }

    @Pointcut("@annotation(org.hv.biscuits.annotation.Affairs)")
    public void verify() {
        //过时切点
        //@Pointcut("execution(public * *(..)) && @within(org.hv.biscuits.annotation.Service) && !execution(public String[] evenSourceIds())")
    }

    @Around("verify()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Affairs affairs = method.getAnnotation(Affairs.class);
        String sessionName = affairs.sessionName();
        boolean enableTransaction = affairs.on();
        Object target = joinPoint.getTarget();
        LOGGER.debug("方法入栈 >>>> {}.{}({})", target.getClass().getName(), method.getName(), org.apache.commons.lang3.StringUtils.join(joinPoint.getArgs(), ","));
        if (StringUtils.isEmpty(sessionName)) {
            Session sessionAnnotation = target.getClass().getAnnotation(Session.class);
            if (sessionAnnotation != null) {
                sessionName = sessionAnnotation.sessionName();
            } else {
                Service serviceAnnotation = target.getClass().getAnnotation(Service.class);
                if (serviceAnnotation != null) {
                    sessionName = serviceAnnotation.session();
                } else {
                    sessionName = context.getEnvironment().getProperty("spring.application.name");
                }
            }
        }
        Object result;
        try {
            ActiveSessionCenter.register(sessionName, enableTransaction);
            result = joinPoint.proceed();
            ActiveSessionCenter.cancelTheRegistration(enableTransaction);
        } catch (Throwable throwable) {
            ActiveSessionCenter.handleException(throwable, enableTransaction);
            throw throwable;
        } finally {
            LOGGER.debug("方法出栈 >>>> {}.{}({})", target.getClass().getName(), method.getName(), org.apache.commons.lang3.StringUtils.join(joinPoint.getArgs(), ","));
        }
        return result;
    }
}

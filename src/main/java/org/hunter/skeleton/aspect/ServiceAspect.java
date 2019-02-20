package org.hunter.skeleton.aspect;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hunter.pocket.session.Session;
import org.hunter.pocket.session.SessionFactory;
import org.hunter.pocket.session.Transaction;
import org.hunter.skeleton.annotation.Affairs;
import org.hunter.skeleton.annotation.Business;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * @author wujianchuan 2019/1/31
 */
@Aspect
@Component
public class ServiceAspect {
    private static final Logger log = LoggerFactory.getLogger(ServiceAspect.class);

    private static long startTime;
    private static long endTime;
    private ThreadLocal<Session> sessionThreadLocal;
    private ThreadLocal<Boolean> transOn = new ThreadLocal<>();

    @Pointcut("execution(public * org.hunter.*.*.service.*.*(..))")
    public void point() {
    }

    @Before("point()")
    public void before(JoinPoint joinPoint) {
        startTime = System.currentTimeMillis();
        Object target = joinPoint.getTarget();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Affairs affairs = method.getDeclaredAnnotation(Affairs.class);
        Business business = target.getClass().getAnnotation(Business.class);
        log.info("<Before> Call method: {}({})", method.getName(), StringUtils.join(joinPoint.getArgs(), ","));
        try {
            Field sessionThreadLocalField = target.getClass().getSuperclass().getDeclaredField("sessionThreadLocal");
            sessionThreadLocalField.setAccessible(true);
            this.sessionThreadLocal = (ThreadLocal<Session>) sessionThreadLocalField.get(target);
            this.sessionThreadLocal.set(SessionFactory.getSession(business.session()));
            Session session = sessionThreadLocal.get();
            if (session != null) {
                if (session.getClosed()) {
                    session.open();
                    transOn.set(false);
                    if (affairs != null && affairs.on()) {
                        Transaction transaction = session.getTransaction();
                        transaction.begin();
                        transOn.set(true);
                    }
                } else {
                    throw new RuntimeException("the session has been opened.");
                }
            } else {
                throw new NullPointerException("Can not find session in thread local.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After("point()")
    public void after() {
        Session session = this.sessionThreadLocal.get();
        if (session != null && !session.getClosed()) {
            if (this.transOn.get()) {
                try {
                    session.getTransaction().commit();
                    this.transOn.set(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            session.close();
            this.sessionThreadLocal.remove();
        }
        endTime = System.currentTimeMillis() - startTime;
    }

    @AfterReturning(pointcut = "point()", returning = "object")
    public void getAfterReturn(Object object) {
        log.info("<AfterReturning> Consuming time={}ms", endTime);
        log.info("afterReturning={}", object != null ? object.toString() : null);
    }

    @AfterThrowing(pointcut = "point()", throwing = "exception")
    public void afterThrowing(Exception exception) {
        Session session = this.sessionThreadLocal.get();
        if (session != null && !session.getClosed()) {
            if (this.transOn.get()) {
                try {
                    session.getTransaction().rollBack();
                    this.transOn.set(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            session.close();
            this.sessionThreadLocal.remove();
        }
        endTime = System.currentTimeMillis() - startTime;
        exception.printStackTrace();
    }

}

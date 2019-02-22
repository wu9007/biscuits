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
import org.hunter.skeleton.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wujianchuan 2019/1/31
 */
@Aspect
@Component
public class ServiceAspect {
    private static final Logger log = LoggerFactory.getLogger(ServiceAspect.class);

    private ThreadLocal<Long> startTimeLocal = new ThreadLocal<>();
    private ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<>();
    private ThreadLocal<Boolean> transOn = new ThreadLocal<>();
    private ThreadLocal<LinkedList<Method>> methodThreadLocal = new ThreadLocal<>();
    private ThreadLocal<Integer> transOnIndex = new ThreadLocal<>();

    @Pointcut("execution(public * org.hunter.*.*.service.*.*(..))")
    public void point() {
    }

    @Before("point()")
    public void before(JoinPoint joinPoint) {
        if (this.methodThreadLocal.get() == null) {
            this.methodThreadLocal.set(new LinkedList<>());
        }

        this.startTimeLocal.set(System.currentTimeMillis());
        Object target = joinPoint.getTarget();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        this.pushMethod(method);
        log.info("<Before> Call method: {}({})", method.getName(), StringUtils.join(joinPoint.getArgs(), ","));
        // session 嵌套时只开启最外层session
        this.sessionOpen(method, target);
    }

    @After("point()")
    public void after() {
        Session session = getSession();
        if (session != null && !session.getClosed()) {
            //锁定开启事务的方法，提交事务
            if (this.getTransOnIndex() != null && this.getMethodLocal().size() == this.getTransOnIndex() && this.getTransOn()) {
                try {
                    session.getTransaction().commit();
                    this.setTransOn(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            //关闭最外层session
            if (this.getMethodLocal().size() == 1) {
                session.close();
            }
        }
        Method method = this.popMethod();
        log.info("<After> Call method: {}", method.getName());
    }

    @AfterReturning(pointcut = "point()", returning = "object")
    public void getAfterReturn(Object object) {
        log.info("<AfterReturning> Consuming time is: {}ms     return value is: {}", System.currentTimeMillis() - this.startTimeLocal.get(), object != null ? object.toString() : null);
        if (this.getMethodLocal().size() == 0) {
            this.remove();
        }
    }

    @AfterThrowing(pointcut = "point()", throwing = "exception")
    public void afterThrowing(Exception exception) {
        Session session = this.getSession();
        if (session != null && !session.getClosed()) {
            if (this.getTransOn()) {
                try {
                    session.getTransaction().rollBack();
                    this.setTransOn(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            session.close();
        }
        this.remove();
        exception.printStackTrace();
    }

    /**
     * 清空所有 thread local
     */
    private void remove() {
        this.startTimeLocal.remove();
        this.removeTransOnLocal();
        this.removeSessionLocal();
        this.removeMethodLocal();
    }

    /**
     * 开启session
     *
     * @param method method
     * @param target obj
     */
    private void sessionOpen(Method method, Object target) {
        Affairs affairs = method.getDeclaredAnnotation(Affairs.class);
        Service service = target.getClass().getAnnotation(Service.class);
        try {
            Field sessionField = target.getClass().getSuperclass().getDeclaredField("session");
            sessionField.setAccessible(true);
            Session session = (Session) sessionField.get(target);
            if (this.getSession() == null) {
                SessionFactory.sideEffect(session, service.session());
                this.setSession(session);

                if (session.getClosed()) {
                    session.open();
                    // 事务嵌到时只开启最外层事务
                    if (!this.getTransOn() && affairs != null && affairs.on()) {
                        Transaction transaction = session.getTransaction();
                        transaction.begin();
                        this.setTransOn(true);
                        //标记开启事务的方法所在下标
                        this.setTransOnIndex(this.getMethodLocal().size());
                    }
                } else {
                    throw new RuntimeException("the session has been opened.");
                }
            } else {
                session.setUpperStorySession(this.getSession());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeSessionLocal() {
        this.sessionThreadLocal.remove();
    }

    private void setSession(Session session) {
        this.sessionThreadLocal.set(session);
    }

    private Session getSession() {
        return this.sessionThreadLocal.get();
    }

    private void removeTransOnLocal() {
        this.transOn.remove();
    }

    private boolean getTransOn() {
        return this.transOn.get() != null && this.transOn.get();
    }

    private void setTransOn(boolean transOn) {
        this.transOn.set(transOn);
    }

    private List<Method> getMethodLocal() {
        return this.methodThreadLocal.get();
    }

    private void removeMethodLocal() {
        this.methodThreadLocal.remove();
    }

    private void pushMethod(Method method) {
        this.methodThreadLocal.get().push(method);
    }

    private Method popMethod() {
        return this.methodThreadLocal.get().pop();
    }

    private void setTransOnIndex(int index) {
        this.transOnIndex.set(index);
    }

    private Integer getTransOnIndex() {
        return this.transOnIndex.get();
    }
}

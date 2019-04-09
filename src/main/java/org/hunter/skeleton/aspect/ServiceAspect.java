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
import org.hunter.skeleton.repository.Repository;
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

    private final ThreadLocal<Long> startTimeLocal = new ThreadLocal<>();
    private final ThreadLocal<ThreadLocal<Session>> sessionThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<Boolean> transOn = new ThreadLocal<>();
    private final ThreadLocal<LinkedList<Method>> methodThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<LinkedList<Object>> targetThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<Integer> transOnIndex = new ThreadLocal<>();

    @Pointcut("execution(public * *..*.service.*.*(..))")
    public void point() {
    }

    @Before("point()")
    public void before(JoinPoint joinPoint) {
        if (this.methodThreadLocal.get() == null) {
            this.methodThreadLocal.set(new LinkedList<>());
        }
        if (this.targetThreadLocal.get() == null) {
            this.targetThreadLocal.set(new LinkedList<>());
        }

        this.startTimeLocal.set(System.currentTimeMillis());
        Object target = joinPoint.getTarget();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        this.pushTarget(target);
        this.pushMethod(method);
        log.info("<Before> Call method: {}({})", method.getName(), StringUtils.join(joinPoint.getArgs(), ","));
        // session 嵌套时只开启最外层session
        this.sessionOpen(method, target);
    }

    @After("point()")
    public void after() {
        ThreadLocal<Session> sessionLocal = this.getSessionLocal();
        if (sessionLocal.get() != null && !sessionLocal.get().getClosed()) {
            //锁定开启事务的方法，提交事务
            if (this.getTransOnIndex() != null && this.getMethodLocal().size() == this.getTransOnIndex() && this.getTransOn()) {
                try {
                    sessionLocal.get().getTransaction().commit();
                    this.setTransOn(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            //关闭最外层session
            if (this.getMethodLocal().size() == 1) {
                sessionLocal.get().close();
            }
        }
        log.info("<After> Call method: {}===>{}", this.getLastTarget().getClass().getSimpleName(), this.getLastMethod().getName());
    }

    @AfterReturning(pointcut = "point()", returning = "object")
    public void getAfterReturn(Object object) {
        Object target = this.popTarget();
        Method method = this.popMethod();
        log.info("<AfterReturning> Call method: {}     Consuming time is: {}ms     return value is: {}", method.getName(), System.currentTimeMillis() - this.startTimeLocal.get(), object != null ? object.toString() : null);
        if (this.getMethodLocal().size() > 0) {
            throw new RuntimeException("Method thread local not empty.");
        }
        if (this.getTargetLocal().size() > 0) {
            throw new RuntimeException("Target thread local not empty.");
        }

        //清空service下repository们的thread local.
        this.getRepositoryList(target).forEach(Repository::pourSession);

        //清空切面中的thread local.
        this.remove();
    }

    @AfterThrowing(pointcut = "point()", throwing = "exception")
    public void afterThrowing(Exception exception) {
        ThreadLocal<Session> sessionLocal = this.getSessionLocal();
        if (sessionLocal.get() != null && !sessionLocal.get().getClosed()) {
            if (this.getTransOn()) {
                try {
                    sessionLocal.get().getTransaction().rollBack();
                    this.setTransOn(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            sessionLocal.get().close();
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
        this.removeTargetLocal();
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
            Field sessionLocalField = target.getClass().getSuperclass().getDeclaredField("sessionLocal");
            sessionLocalField.setAccessible(true);
            ThreadLocal<Session> sessionLocal = (ThreadLocal<Session>) sessionLocalField.get(target);

            if (this.getSessionLocal() == null) {
                sessionLocal.set(SessionFactory.getSession(service.session()));
                this.setSessionLocal(sessionLocal);

                if (sessionLocal.get().getClosed()) {
                    sessionLocal.get().open();
                    // 事务嵌到时只开启最外层事务
                    if (!this.getTransOn() && affairs != null && affairs.on()) {
                        Transaction transaction = sessionLocal.get().getTransaction();
                        transaction.begin();
                        this.setTransOn(true);
                        //标记开启事务的方法所在下标
                        this.setTransOnIndex(this.getMethodLocal().size());
                    }
                } else {
                    throw new RuntimeException("the session has been opened.");
                }
            } else {
                sessionLocal.set(this.getSessionLocal().get());
            }

            //给service下的所有repository注入session
            this.getRepositoryList(target).forEach(repository -> repository.injectSession(sessionLocal.get()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeSessionLocal() {
        this.sessionThreadLocal.get().remove();
        this.sessionThreadLocal.remove();
    }

    private List<Repository> getRepositoryList(Object target) {
        try {
            Field repositoryListField = target.getClass().getSuperclass().getDeclaredField("repositoryList");
            repositoryListField.setAccessible(true);
            return (List<Repository>) repositoryListField.get(target);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private void setSessionLocal(ThreadLocal<Session> sessionLocal) {
        this.sessionThreadLocal.set(sessionLocal);
    }

    private ThreadLocal<Session> getSessionLocal() {
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

    private void setTransOnIndex(int index) {
        this.transOnIndex.set(index);
    }

    private Integer getTransOnIndex() {
        return this.transOnIndex.get();
    }

    //======== 方法站操作 ========

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

    private Method getLastMethod() {
        return this.methodThreadLocal.get().getLast();
    }

    //======== 对象栈操作 ========

    private List<Object> getTargetLocal() {
        return this.targetThreadLocal.get();
    }

    private void removeTargetLocal() {
        this.targetThreadLocal.remove();
    }

    private void pushTarget(Object object) {
        this.targetThreadLocal.get().push(object);
    }

    private Object popTarget() {
        return this.targetThreadLocal.get().pop();
    }

    private Object getLastTarget() {
        return this.targetThreadLocal.get().getLast();
    }
}

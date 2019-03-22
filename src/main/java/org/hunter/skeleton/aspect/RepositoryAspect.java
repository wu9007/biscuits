package org.hunter.skeleton.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hunter.pocket.model.PocketEntity;
import org.springframework.stereotype.Component;

/**
 * @author wujianchuan 2019/3/22
 * @version 1.0
 */
@Aspect
@Component
public class RepositoryAspect {

    private static final String SAVE = "save";
    private static final String UPDATE = "update";
    private static final String DELETE = "delete";

    @Pointcut("execution(public * org.hunter..*.repository.*.save(..)) || execution(public * org.hunter..*.repository.*.update(..)) || execution(public * org.hunter..*.repository.*.delete(..))")
    public void point() {
    }

    @Before("point()")
    public void before(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
        PocketEntity entity = (PocketEntity) args[0];
        String operator = (String) args[1];
        //TODO 反射存储操作历史
        String operate;
        StringBuilder history = new StringBuilder("操作内容：");
        switch (methodName) {
            case SAVE:
                operate = "保存";
                history.append("【新增数据】- ");
                break;
            case UPDATE:
                operate = "更新";
                history.append("【编辑数据】- ");
                break;
            case DELETE:
                operate = "删除";
                history.append("【删除数据】- ");
                break;
                default:
                    break;
        }
    }
}

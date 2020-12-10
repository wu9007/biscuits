package org.hv.biscuits.core.history;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hv.biscuits.annotation.Track;
import org.hv.biscuits.core.session.ActiveSessionCenter;
import org.hv.pocket.model.AbstractEntity;
import org.hv.pocket.session.Session;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author leyan95 2019/3/22
 * @version 1.0
 */
@Aspect
@Component
public class RepositoryHistoryAspect {

    private static final HistoryFactory HISTORY_FACTORY = HistoryFactory.getInstance();

    @Around("@annotation(org.hv.biscuits.annotation.Track)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Track track = method.getAnnotation(Track.class);
        String dataKey = track.data();
        String operatorKey = track.operator();
        String operateNameKey = track.operateName();
        String operate = track.operate().getId();

        ExpressionParser parser = new SpelExpressionParser();
        Expression dataExpression = parser.parseExpression(dataKey);
        Expression operatorExpression = parser.parseExpression(operatorKey);
        Expression operateNameExpression = null;
        if (operateNameKey.startsWith("#")) {
            operateNameExpression = parser.parseExpression(operateNameKey);
        }
        EvaluationContext context = new StandardEvaluationContext();
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        String[] argsName = discoverer.getParameterNames(method);
        Object[] argsValue = joinPoint.getArgs();
        if (argsName != null) {
            for (int index = 0; index < argsName.length; index++) {
                context.setVariable(argsName[index], argsValue[index]);
            }
        } else {
            throw new IllegalArgumentException("can not found any arg.");
        }
        Object operatorValue = operatorExpression.getValue(context);
        Object operateNameValue = null;
        if (operateNameExpression != null) {
            operateNameValue = operateNameExpression.getValue(context);
        }
        Object dataValue = dataExpression.getValue(context);
        String operator;
        AbstractEntity entity;
        String operateName;
        if (operatorValue != null && dataValue != null) {
            operator = operatorValue.toString();
            entity = (AbstractEntity) dataValue;
        } else {
            throw new IllegalArgumentException("can not found data and operator.");
        }
        if (operateNameValue == null) {
            operateName = track.operateName();
        } else {
            operateName = operateNameValue.toString();
        }
        Session session = ActiveSessionCenter.getCurrentSession();
        AbstractEntity newEntity;
        AbstractEntity olderEntity = null;
        if (entity.loadIdentify() != null) {
            olderEntity = session.findDirect(entity.getClass(), entity.loadIdentify());
        }
        Object result = joinPoint.proceed();
        if (entity.loadIdentify() != null) {
            newEntity = session.findDirect(entity.getClass(), entity.loadIdentify());
        } else {
            newEntity = entity;
        }
        HISTORY_FACTORY.saveHistory(olderEntity, newEntity, session, operateName, operate, operator);
        return result;
    }
}

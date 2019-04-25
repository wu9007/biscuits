package org.hunter.skeleton.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.model.BaseEntity;
import org.hunter.pocket.model.MapperFactory;
import org.hunter.pocket.session.Session;
import org.hunter.skeleton.annotation.Track;
import org.hunter.skeleton.spine.model.History;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.hunter.skeleton.constant.Operate.ADD;
import static org.hunter.skeleton.constant.Operate.DELETE;
import static org.hunter.skeleton.constant.Operate.EDIT;

/**
 * @author wujianchuan 2019/3/22
 * @version 1.0
 */
@Aspect
@Component
public class RepositoryAspect {

    private final Collector<HistoryData, ?, Map<String, Object>> businessCollector = Collectors.toMap(entityData ->
                    MapperFactory.getBusinessName(entityData.entity.getClass().getName(), entityData.field.getName()),
            entityData -> {
                try {
                    Field field = entityData.getField();
                    field.setAccessible(true);
                    Object value = field.get(entityData.getEntity());
                    return value != null ? value : "---";
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return "---";
                }
            });

    @Around("@annotation(org.hunter.skeleton.annotation.Track)")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Track track = method.getAnnotation(Track.class);
        String dataKey = track.data();
        String operatorKey = track.operator();
        String operate = track.operate().getId();

        ExpressionParser parser = new SpelExpressionParser();
        Expression dataExpression = parser.parseExpression(dataKey);
        Expression operatorExpression = parser.parseExpression(operatorKey);
        EvaluationContext context = new StandardEvaluationContext();
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        String[] argsName = discoverer.getParameterNames(method);
        Object[] argsValue = joinPoint.getArgs();
        if (argsName != null) {
            for (int index = 0; index < argsName.length; index++) {
                context.setVariable(argsName[index], argsValue[index]);
            }
        } else {
            throw new RuntimeException("can not found any arg.");
        }
        Object operateValue = operatorExpression.getValue(context);
        Object dataValue = dataExpression.getValue(context);
        String operator;
        BaseEntity entity;
        if (operateValue != null && dataValue != null) {
            operator = operateValue.toString();
            entity = (BaseEntity) dataValue;
        } else {
            throw new RuntimeException("can not found data and operator.");
        }
        Object target = joinPoint.getTarget();
        Field sessionLocalField = target.getClass().getSuperclass().getDeclaredField("sessionLocal");
        sessionLocalField.setAccessible(true);
        ThreadLocal<Session> sessionLocal = (ThreadLocal<Session>) sessionLocalField.get(target);
        Session session = sessionLocal.get();
        BaseEntity olderEntity = (BaseEntity) session.findDirect(entity.getClass(), entity.getUuid());
        Object result = joinPoint.proceed();
        this.saveHistory(olderEntity, entity, session, track.operateName(), operate, operator);
        return result;
    }

    private void saveHistory(BaseEntity olderEntity, BaseEntity entity, Session session, String operateName, String operate, String operator) throws SQLException {
        Class clazz = entity.getClass();
        Entity entityAnnotation = (Entity) clazz.getAnnotation(Entity.class);

        if (entityAnnotation != null) {
            String tableBusinessName = entityAnnotation.businessName();

            Map<String, Object> operateContent = new LinkedHashMap<>(4);
            operateContent.put("操作对象", tableBusinessName);
            operateContent.put("业务", operateName);
            Map<String, Object> keyBusinessData = Arrays.stream(MapperFactory.getKeyBusinessFields(clazz.getName()))
                    .map(field -> new HistoryData(entity, field))
                    .collect(businessCollector);
            operateContent.put("关键数据", keyBusinessData);
            switch (operate) {
                case ADD:
                    Map<String, Object> commonBusinessData = Arrays.stream(MapperFactory.getBusinessFields(clazz.getName()))
                            .map(field -> new HistoryData(entity, field))
                            .collect(businessCollector);
                    operateContent.put("操作数据", commonBusinessData);
                    break;
                case EDIT:
                    BaseEntity newEntity = (BaseEntity) session.findDirect(clazz, entity.getUuid());
                    Map<String, Object> dirtyCommonBusinessData = Arrays.stream(MapperFactory.getBusinessFields(clazz.getName()))
                            .filter(field -> this.dirtyFieldFilter(newEntity, olderEntity, field))
                            .map(field -> new HistoryData(newEntity, field))
                            .collect(businessCollector);
                    operateContent.put("操作数据", dirtyCommonBusinessData);
                    break;
                case DELETE:
                    newEntity = (BaseEntity) session.findDirect(clazz, entity.getUuid());
                    Map<String, Object> deleteData = Arrays.stream(MapperFactory.getRepositoryFields(clazz.getName()))
                            .collect(Collectors.toMap(Field::getName, field -> {
                                field.setAccessible(true);
                                try {
                                    return field.get(newEntity);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                    throw new RuntimeException("获取属性值失败");
                                }
                            }));

                    operateContent.put("操作数据", deleteData);
                    break;
                default:
                    throw new NullPointerException(String.format("%s - 该操作不产生历史数据。", operate));
            }

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Objects.requireNonNull(session).save(new History(operate, new Date(), operator, olderEntity.getUuid(), objectMapper.writeValueAsString(operateContent)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean dirtyFieldFilter(Object modern, Object older, Field field) {
        field.setAccessible(true);
        try {
            Object modernValue = field.get(modern);
            Object olderValue = field.get(older);
            return modernValue == null && olderValue != null || olderValue == null && modernValue != null || modernValue != null && !modernValue.equals(olderValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("获取属性值失败。");
        }
    }

    private class HistoryData {
        private BaseEntity entity;
        private Field field;

        HistoryData(BaseEntity entity, Field field) {
            this.entity = entity;
            this.field = field;
        }

        public BaseEntity getEntity() {
            return entity;
        }

        public void setEntity(BaseEntity entity) {
            this.entity = entity;
        }

        Field getField() {
            return field;
        }

        public void setField(Field field) {
            this.field = field;
        }
    }
}

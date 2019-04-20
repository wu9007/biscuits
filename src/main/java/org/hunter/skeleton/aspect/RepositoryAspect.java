package org.hunter.skeleton.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.annotation.Join;
import org.hunter.pocket.annotation.OneToMany;
import org.hunter.pocket.model.PocketEntity;
import org.hunter.pocket.session.Session;
import org.hunter.pocket.utils.ReflectUtils;
import org.hunter.skeleton.annotation.Track;
import org.hunter.skeleton.spine.model.History;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.management.ReflectionException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
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

    private final Predicate<Field> businessPredicate = field -> {
        Column column = field.getAnnotation(Column.class);
        OneToMany oneToMany = field.getAnnotation(OneToMany.class);
        Join join = field.getAnnotation(Join.class);

        boolean businessColumn = column != null && column.businessName().length() > 0;
        boolean businessMany = oneToMany != null && oneToMany.businessName().length() > 0;
        boolean businessJoin = join != null && join.businessName().length() > 0;
        return businessColumn || businessMany || businessJoin;
    };
    private final Collector<HistoryData, ?, Map<String, Object>> businessCollector = Collectors.toMap(entityData -> {
        Field field = entityData.getField();
        Column column = field.getAnnotation(Column.class);
        OneToMany oneToMany = field.getAnnotation(OneToMany.class);
        Join join = field.getAnnotation(Join.class);
        return column != null ? column.businessName() : oneToMany != null ? oneToMany.businessName() : join.businessName();
    }, entityData -> {
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
        PocketEntity entity;
        if (operateValue != null && dataValue != null) {
            operator = operateValue.toString();
            entity = (PocketEntity) dataValue;
        } else {
            throw new RuntimeException("can not found data and operator.");
        }
        Object target = joinPoint.getTarget();
        Field sessionLocalField = target.getClass().getSuperclass().getDeclaredField("sessionLocal");
        sessionLocalField.setAccessible(true);
        ThreadLocal<Session> sessionLocal = (ThreadLocal<Session>) sessionLocalField.get(target);
        Session session = sessionLocal.get();

        this.saveHistory(entity, session, operate, operator);

        return joinPoint.proceed();
    }

    private void saveHistory(PocketEntity entity, Session session, String operate, String operator) {
        Class clazz = entity.getClass();
        Entity entityAnnotation = (Entity) clazz.getAnnotation(Entity.class);

        if (entityAnnotation != null) {
            String tableBusinessName = entityAnnotation.businessName();

            Map<String, Object> operateContent = new LinkedHashMap<>(4);
            operateContent.put("业务名称", tableBusinessName);

            switch (operate) {
                case ADD:
                    Map<String, Object> fieldBusinessData = Arrays.stream(clazz.getDeclaredFields())
                            .filter(businessPredicate)
                            .map(field -> new HistoryData(entity, field))
                            .collect(businessCollector);
                    operateContent.put("操作方式", "新增数据");
                    operateContent.put("操作数据", fieldBusinessData);
                    break;
                case EDIT:
                    PocketEntity dirtyEntity = (PocketEntity) session.findOne(clazz, this.getUuid(entity));
                    Map<String, Object> dirtyFieldBusinessData = Arrays.stream(clazz.getDeclaredFields())
                            .filter(businessPredicate)
                            .filter(field -> this.dirtyFieldFilter(entity, dirtyEntity, field))
                            .map(field -> new HistoryData(entity, field))
                            .collect(businessCollector);
                    operateContent.put("操作方式", "编辑数据");
                    operateContent.put("操作数据", dirtyFieldBusinessData);
                    break;
                case DELETE:
                    operateContent.put("操作方式", "删除数据");
                    operateContent.put("操作数据", this.getUuid(entity));
                    break;
                default:
                    throw new NullPointerException(String.format("%s - 该操作不产生历史数据。", operate));
            }

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Objects.requireNonNull(session).save(new History(operate, new Date(), operator, objectMapper.writeValueAsString(operateContent)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    private Serializable getUuid(PocketEntity entity) {
        try {
            Field uuidField = entity.getClass().getSuperclass().getDeclaredField("uuid");
            Objects.requireNonNull(uuidField).setAccessible(true);
            return (Serializable) uuidField.get(entity);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new NullPointerException("未找到uuid");
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
        private PocketEntity entity;
        private Field field;

        HistoryData(PocketEntity entity, Field field) {
            this.entity = entity;
            this.field = field;
        }

        public PocketEntity getEntity() {
            return entity;
        }

        public void setEntity(PocketEntity entity) {
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

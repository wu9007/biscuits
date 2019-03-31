package org.hunter.skeleton.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    private final Predicate<Field> businessPredicate = field -> {
        Column column = field.getAnnotation(Column.class);
        OneToMany oneToMany = field.getAnnotation(OneToMany.class);
        if (column != null && column.businessName().length() > 0) {
            return true;
        } else {
            return oneToMany != null && oneToMany.businessName().length() > 0;
        }
    };
    private final Collector<EntityData, ?, Map<String, Object>> businessCollector = Collectors.toMap(entityData -> {
        Field field = entityData.getField();
        Column column = field.getAnnotation(Column.class);
        OneToMany oneToMany = field.getAnnotation(OneToMany.class);
        return column != null ? column.businessName() : oneToMany.businessName();
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
        Object result = joinPoint.proceed();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Track track = method.getAnnotation(Track.class);
        String dataKey = track.data();
        String operatorKey = track.operator();
        String operate = track.operate();

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

        Class clazz = entity.getClass();
        Entity entityAnnotation = (Entity) clazz.getAnnotation(Entity.class);
        Field uuidField = clazz.getSuperclass().getDeclaredField("uuid");
        Objects.requireNonNull(uuidField).setAccessible(true);
        Serializable uuid = (Serializable) uuidField.get(entity);
        Object target = joinPoint.getTarget();
        Field sessionLocalField = target.getClass().getSuperclass().getDeclaredField("sessionLocal");
        sessionLocalField.setAccessible(true);
        ThreadLocal<Session> sessionLocal = (ThreadLocal<Session>) sessionLocalField.get(target);
        Session session = sessionLocal.get();

        if (entityAnnotation != null) {
            String tableBusinessName = entityAnnotation.businessName();

            Map<String, Object> operateContent = new LinkedHashMap<>(4);
            operateContent.put("业务名称", tableBusinessName);

            switch (operate) {
                case SAVE:
                    Map<String, Object> fieldBusinessData = Arrays.stream(ReflectUtils.getInstance().getMappingFields(clazz))
                            .filter(businessPredicate)
                            .map(field -> new EntityData(entity, field))
                            .collect(businessCollector);
                    operateContent.put("操作方式", "新增数据");
                    operateContent.put("操作数据", fieldBusinessData);
                    break;
                case UPDATE:
                    Map<String, Object> dirtyFieldBusinessData = new HashMap<>(20);
                    PocketEntity dirtyEntity = (PocketEntity) session.findOne(clazz, uuid);
                    dirtyFieldBusinessData = Arrays.stream(ReflectUtils.getInstance().dirtyFieldFilter(entity, dirtyEntity))
                            .filter(businessPredicate)
                            .map(field -> new EntityData(entity, field))
                            .collect(businessCollector);
                    operateContent.put("操作方式", "编辑数据");
                    operateContent.put("操作数据", dirtyFieldBusinessData);
                    break;
                case DELETE:
                    operateContent.put("操作方式", "删除数据");
                    operateContent.put("操作数据", uuid);
                    break;
                default:
                    break;
            }

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Objects.requireNonNull(session).save(new History(operate, new Date(), operator, objectMapper.writeValueAsString(operateContent)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private class EntityData {
        private PocketEntity entity;
        private Field field;

        EntityData(PocketEntity entity, Field field) {
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

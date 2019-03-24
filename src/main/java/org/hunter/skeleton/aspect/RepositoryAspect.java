package org.hunter.skeleton.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.annotation.OneToMany;
import org.hunter.pocket.model.PocketEntity;
import org.hunter.pocket.session.Session;
import org.hunter.pocket.utils.ReflectUtils;
import org.hunter.skeleton.spine.model.History;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Field;
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

    @Pointcut("execution(public * org.hunter..*.repository.*.save(..)) || execution(public * org.hunter..*.repository.*.update(..)) || execution(public * org.hunter..*.repository.*.delete(..))")
    public void point() {
    }

    @Before("point()")
    public void before(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String operate = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
        PocketEntity entity = ((PocketEntity) args[0]);
        Class clazz = entity.getClass();
        Entity entityAnnotation = (Entity) clazz.getAnnotation(Entity.class);
        Serializable uuid = null;
        Session session = null;
        try {
            Field uuidField = clazz.getSuperclass().getDeclaredField("uuid");
            Objects.requireNonNull(uuidField).setAccessible(true);
            uuid = (Serializable) uuidField.get(entity);
            Object target = joinPoint.getTarget();
            Field sessionLocalField = target.getClass().getSuperclass().getDeclaredField("sessionLocal");
            sessionLocalField.setAccessible(true);
            ThreadLocal<Session> sessionLocal = (ThreadLocal<Session>) sessionLocalField.get(target);
            session = sessionLocal.get();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (entityAnnotation != null && entityAnnotation.history()) {
            String tableBusinessName = entityAnnotation.businessName();
            String operator = (String) args[1];

            Map<String, Object> operateContent = new LinkedHashMap<>(4);
            operateContent.put("业务名称", tableBusinessName);

            switch (operate) {
                case SAVE:
                    Map<String, Object> fieldBusinessData = Arrays.stream(ReflectUtils.getInstance().getMappingField(clazz))
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

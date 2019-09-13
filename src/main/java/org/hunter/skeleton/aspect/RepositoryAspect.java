package org.hunter.skeleton.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.constant.AnnotationType;
import org.hunter.pocket.model.BaseEntity;
import org.hunter.pocket.model.DetailInductiveBox;
import org.hunter.pocket.model.MapperFactory;
import org.hunter.pocket.session.Session;
import org.hunter.pocket.utils.ReflectUtils;
import org.hunter.skeleton.annotation.Track;
import org.hunter.skeleton.constant.OperateEnum;
import org.hunter.skeleton.repository.AbstractRepository;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.hunter.skeleton.constant.Operate.ADD;
import static org.hunter.skeleton.constant.Operate.AUDIT;
import static org.hunter.skeleton.constant.Operate.DELETE;
import static org.hunter.skeleton.constant.Operate.EDIT;
import static org.hunter.skeleton.constant.Operate.REVOKE;

/**
 * @author wujianchuan 2019/3/22
 * @version 1.0
 */
@SuppressWarnings("unchecked")
@Aspect
@Component
public class RepositoryAspect {

    private final Collector<HistoryData, ?, Map<String, Object>> flagBusinessCollector = Collectors.toMap(entityData ->
                    MapperFactory.getBusinessName(entityData.getNewEntity().getClass().getName(), entityData.field.getName()),
            entityData -> {
                try {
                    Field field = entityData.getField();
                    field.setAccessible(true);
                    Object value = field.get(entityData.getNewEntity());
                    return value != null ? value : "---";
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return "---";
                }
            });
    private final Collector<HistoryData, ?, Map<String, Object>> businessCollector = Collectors.toMap(entityData ->
                    MapperFactory.getBusinessName(entityData.getNewEntity() != null ? entityData.getNewEntity().getClass().getName() : entityData.getOldEntity().getClass().getName(), entityData.field.getName()),
            entityData -> {
                try {
                    BaseEntity newEntity = entityData.getNewEntity();
                    BaseEntity oldEntity = entityData.getOldEntity();
                    Field field = entityData.getField();
                    OperateEnum operateEnum = entityData.getOperateEnum();
                    field.setAccessible(true);
                    if (OperateEnum.ADD.equals(operateEnum)) {
                        Object newValue = field.get(newEntity);
                        AnnotationType annotationType = MapperFactory.getAnnotationType(newEntity.getClass().getName(), field.getName());
                        if (AnnotationType.ONE_TO_MANY.equals(annotationType)) {
                            List<BaseEntity> details = (List<BaseEntity>) newValue;
                            return details.stream()
                                    .map(detail -> this.getBusinessContent(detail, null))
                                    .collect(Collectors.toList());
                        }
                        return newValue != null ? newValue : "---";
                    } else if (OperateEnum.EDIT.equals(operateEnum)) {
                        Object newValue = field.get(newEntity);
                        AnnotationType annotationType = MapperFactory.getAnnotationType(newEntity.getClass().getName(), field.getName());
                        Object oldValue = field.get(oldEntity);
                        if (AnnotationType.ONE_TO_MANY.equals(annotationType)) {
                            List<BaseEntity> newDetails = (List<BaseEntity>) newValue;
                            List<BaseEntity> oldDetails = (List<BaseEntity>) oldValue;
                            DetailInductiveBox detailInductiveBox = DetailInductiveBox.newInstance(newDetails, oldDetails);
                            List<Map> moribundDetailListContent = detailInductiveBox.getMoribund().stream()
                                    .map(moribund -> this.getBusinessContent(null, moribund))
                                    .collect(Collectors.toList());
                            List<Map> newbornDetailListContent = detailInductiveBox.getNewborn().stream()
                                    .map(newborn -> this.getBusinessContent(newborn, null))
                                    .collect(Collectors.toList());
                            Map<String, BaseEntity> olderMapper = oldDetails.stream().collect(Collectors.toMap(BaseEntity::getUuid, detail -> detail));
                            List<Map> updateDetailListContent = detailInductiveBox.getUpdate().stream()
                                    .map(newDetail -> {
                                        BaseEntity olderDetail = olderMapper.get(newDetail.getUuid());
                                        return this.getBusinessContent(newDetail, olderDetail);
                                    })
                                    .collect(Collectors.toList());
                            Map<String, Object> content = new HashMap<>(4);
                            content.put("编辑明细", updateDetailListContent);
                            content.put("新增明细", newbornDetailListContent);
                            content.put("删除明细", moribundDetailListContent);
                            return content;
                        }
                        return String.format("由 %s 改变为 %s", oldValue, newValue);
                    } else if (OperateEnum.DELETE.equals(operateEnum)) {
                        Object oldValue = field.get(oldEntity);
                        AnnotationType annotationType = MapperFactory.getAnnotationType(oldEntity.getClass().getName(), field.getName());
                        if (AnnotationType.ONE_TO_MANY.equals(annotationType)) {
                            List<BaseEntity> oldDetails = (List<BaseEntity>) oldValue;
                            return oldDetails.stream()
                                    .map(oldDetail -> this.getBusinessContent(null, oldDetail))
                                    .collect(Collectors.toList());
                        }
                        return oldValue != null ? oldValue : "---";
                    } else {
                        throw new IllegalArgumentException("非法参数");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return "警告⚠该条数据不合法";
                }
            }, (u, v) -> {
                throw new IllegalStateException(String.format("Duplicate key %s", u));
            }, LinkedHashMap::new);


    @Around("@annotation(org.hunter.skeleton.annotation.Track)")
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
        BaseEntity entity;
        String operateName;
        if (operatorValue != null && dataValue != null) {
            operator = operatorValue.toString();
            entity = (BaseEntity) dataValue;
        } else {
            throw new IllegalArgumentException("can not found data and operator.");
        }
        if (operateNameValue == null) {
            operateName = track.operateName();
        } else {
            operateName = operateNameValue.toString();
        }
        Object target = joinPoint.getTarget();
        Field sessionLocalField = AbstractRepository.class.getDeclaredField("sessionLocal");

        sessionLocalField.setAccessible(true);
        ThreadLocal<Session> sessionLocal = (ThreadLocal<Session>) sessionLocalField.get(target);
        Session session = sessionLocal.get();
        BaseEntity newEntity;
        BaseEntity olderEntity = null;
        if (entity.getUuid() != null) {
            olderEntity = (BaseEntity) session.findDirect(entity.getClass(), entity.getUuid());
        }
        Object result = joinPoint.proceed();
        if (entity.getUuid() != null) {
            newEntity = (BaseEntity) session.findDirect(entity.getClass(), entity.getUuid());
        } else {
            newEntity = entity;
        }
        this.saveHistory(olderEntity, newEntity, session, operateName, operate, operator);
        return result;
    }

    private void saveHistory(BaseEntity olderEntity, BaseEntity newEntity, Session session, String operateName, String operate, String operator) throws SQLException {

        Class clazz = newEntity != null ? newEntity.getClass() : olderEntity.getClass();
        Entity entityAnnotation = (Entity) clazz.getAnnotation(Entity.class);

        if (entityAnnotation != null) {
            String tableBusinessName = entityAnnotation.businessName();

            Map<String, Object> operateContent = new LinkedHashMap<>(4);
            operateContent.put("操作对象", tableBusinessName);
            operateContent.put("业务", operateName);
            operateContent.put("关键数据", this.getFlagBusinessContent(newEntity != null ? newEntity : olderEntity));
            switch (operate) {
                case ADD:
                    operateContent.put("新增的数据", this.getBusinessContent(newEntity, null));
                    break;
                case AUDIT:
                    operateContent.put("审核", this.getBusinessContent(newEntity, olderEntity));
                    break;
                case REVOKE:
                    operateContent.put("撤销审核", this.getBusinessContent(newEntity, olderEntity));
                    break;
                case EDIT:
                    operateContent.put("更新的数据", this.getBusinessContent(newEntity, olderEntity));
                    break;
                case DELETE:
                    operateContent.put("删除的数据", this.getBusinessContent(null, olderEntity));
                    break;
                default:
                    throw new IllegalArgumentException(String.format("%s - 该操作不产生历史数据。", operate));
            }

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Objects.requireNonNull(session).save(new History(operate, new Date(), operator, newEntity != null ? newEntity.getUuid() : olderEntity.getUuid(), objectMapper.writeValueAsString(operateContent)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 生成操作历史内容（所有业务内容）
     *
     * @param newEntity   新
     * @param olderEntity 老
     * @return business content
     */
    private Map<String, Object> getBusinessContent(BaseEntity newEntity, BaseEntity olderEntity) {
        if (olderEntity == null) {
            // 新增
            return Arrays.stream(MapperFactory.getBusinessFields(newEntity.getClass().getName()))
                    .map(field -> new HistoryData(newEntity, null, field, OperateEnum.ADD))
                    .collect(businessCollector);
        } else if (newEntity != null) {
            // 编辑
            List<Field> dirtyFields = Arrays.asList(ReflectUtils.getInstance().dirtyFieldFilter(newEntity, olderEntity));
            List<Field> onToManyFields = Arrays.asList(MapperFactory.getOneToMayFields(newEntity.getClass().getName()));
            return Arrays.stream(MapperFactory.getBusinessFields(newEntity.getClass().getName()))
                    .filter(field -> dirtyFields.contains(field) || onToManyFields.contains(field))
                    .map(field -> new HistoryData(newEntity, olderEntity, field, OperateEnum.EDIT))
                    .collect(businessCollector);
        } else {
            // 删除
            return Arrays.stream(MapperFactory.getBusinessFields(olderEntity.getClass().getName()))
                    .map(field -> new HistoryData(null, olderEntity, field, OperateEnum.DELETE))
                    .collect(businessCollector);
        }
    }

    /**
     * 生成操作历史内容（关键业务内容）
     *
     * @param newEntity 新
     * @return business content
     */
    private Map<String, Object> getFlagBusinessContent(BaseEntity newEntity) {
        return Arrays.stream(MapperFactory.getKeyBusinessFields(newEntity.getClass().getName()))
                .map(field -> new HistoryData(newEntity, field))
                .collect(flagBusinessCollector);
    }

    private class HistoryData {
        private final BaseEntity newEntity;
        private BaseEntity oldEntity;
        private final Field field;
        private OperateEnum operateEnum;

        HistoryData(BaseEntity newEntity, BaseEntity oldEntity, Field field, OperateEnum operateEnum) {
            this.newEntity = newEntity;
            this.oldEntity = oldEntity;
            this.field = field;
            this.operateEnum = operateEnum;
        }

        HistoryData(BaseEntity newEntity, Field field) {
            this.newEntity = newEntity;
            this.field = field;
        }

        private BaseEntity getNewEntity() {
            return newEntity;
        }


        private BaseEntity getOldEntity() {
            return oldEntity;
        }

        private Field getField() {
            return field;
        }

        private OperateEnum getOperateEnum() {
            return operateEnum;
        }
    }
}

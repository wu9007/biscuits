package org.hv.biscuits.core.history;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hv.biscuits.constant.Operate;
import org.hv.biscuits.constant.OperateEnum;
import org.hv.biscuits.spine.model.History;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.constant.AnnotationType;
import org.hv.pocket.model.AbstractEntity;
import org.hv.pocket.model.DetailInductiveBox;
import org.hv.pocket.model.MapperFactory;
import org.hv.pocket.session.Session;
import org.hv.pocket.utils.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
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

/**
 * @author wujianchuan 2020/9/21 10:00
 */
public class HistoryFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryFactory.class);
    private static final HistoryFactory INSTANCE = new HistoryFactory();
    private final Collector<HistoryData, ?, Map<String, Object>> flagBusinessCollector = Collectors.toMap(entityData ->
                    MapperFactory.getBusinessName(entityData.getNewEntity().getClass().getName(), entityData.getField().getName()),
            entityData -> {
                try {
                    Field field = entityData.getField();
                    field.setAccessible(true);
                    Object value = field.get(entityData.getNewEntity());
                    return value != null ? value : "---";
                } catch (IllegalAccessException e) {
                    LOGGER.warn(e.getMessage());
                    return "---";
                }
            });
    private final Collector<HistoryData, ?, Map<String, Object>> businessCollector = Collectors.toMap(entityData ->
                    MapperFactory.getBusinessName(entityData.getNewEntity() != null ? entityData.getNewEntity().getClass().getName() : entityData.getOldEntity().getClass().getName(), entityData.getField().getName()),
            entityData -> {
                try {
                    AbstractEntity newEntity = entityData.getNewEntity();
                    AbstractEntity oldEntity = entityData.getOldEntity();
                    Field field = entityData.getField();
                    OperateEnum operateEnum = entityData.getOperateEnum();
                    field.setAccessible(true);
                    if (OperateEnum.ADD.equals(operateEnum)) {
                        return getAddHistoryContent(newEntity, field);
                    } else if (OperateEnum.EDIT.equals(operateEnum)) {
                        return getEditHistoryContent(newEntity, oldEntity, field);
                    } else if (OperateEnum.DELETE.equals(operateEnum)) {
                        return getDeleteHistoryContent(oldEntity, field);
                    } else {
                        throw new IllegalArgumentException("非法参数");
                    }
                } catch (IllegalAccessException e) {
                    LOGGER.warn(e.getMessage());
                    return "警告⚠该条数据不合法";
                }
            }, (u, v) -> {
                throw new IllegalStateException("Duplicate businessName.");
            }, LinkedHashMap::new);

    private HistoryFactory() {
    }

    public static HistoryFactory getInstance() {
        return INSTANCE;
    }

    /**
     * 获取新增历史数据
     *
     * @param newEntity 新增实例
     * @param field     属性
     * @return 历史数据
     * @throws IllegalAccessException e
     */
    private Object getAddHistoryContent(AbstractEntity newEntity, Field field) throws IllegalAccessException {
        Object newValue = field.get(newEntity);
        AnnotationType annotationType = MapperFactory.getAnnotationType(newEntity.getClass().getName(), field.getName());
        if (AnnotationType.ONE_TO_MANY.equals(annotationType)) {
            List<AbstractEntity> details = (List<AbstractEntity>) newValue;
            return details.stream()
                    .map(detail -> this.getBusinessContent(detail, null))
                    .collect(Collectors.toList());
        }
        return newValue != null ? newValue : "---";
    }

    /**
     * 获取删除历史数据
     *
     * @param oldEntity 老实例
     * @param field     属性
     * @return 历史数据
     * @throws IllegalAccessException e
     */
    private Object getDeleteHistoryContent(AbstractEntity oldEntity, Field field) throws IllegalAccessException {
        Object oldValue = field.get(oldEntity);
        AnnotationType annotationType = MapperFactory.getAnnotationType(oldEntity.getClass().getName(), field.getName());
        if (AnnotationType.ONE_TO_MANY.equals(annotationType)) {
            List<AbstractEntity> oldDetails = (List<AbstractEntity>) oldValue;
            return oldDetails.stream()
                    .map(oldDetail -> this.getBusinessContent(null, oldDetail))
                    .collect(Collectors.toList());
        }
        return oldValue != null ? oldValue : "---";
    }

    /**
     * 获取编辑历史数据
     *
     * @param newEntity 新增实例
     * @param oldEntity 删除实例
     * @param field     属性
     * @return 历史数据
     * @throws IllegalAccessException e
     */
    public Object getEditHistoryContent(AbstractEntity newEntity, AbstractEntity oldEntity, Field field) throws IllegalAccessException {
        Object newValue = field.get(newEntity);
        AnnotationType annotationType = MapperFactory.getAnnotationType(newEntity.getClass().getName(), field.getName());
        Object oldValue = field.get(oldEntity);
        if (AnnotationType.ONE_TO_MANY.equals(annotationType)) {
            List<AbstractEntity> newDetails = (List<AbstractEntity>) newValue;
            List<AbstractEntity> oldDetails = (List<AbstractEntity>) oldValue;
            DetailInductiveBox detailInductiveBox = DetailInductiveBox.newInstance(newDetails, oldDetails);
            List<Map<String, Object>> moribundDetailListContent = detailInductiveBox.getMoribund().stream()
                    .map(moribund -> this.getBusinessContent(null, moribund))
                    .collect(Collectors.toList());
            List<Map<String, Object>> newbornDetailListContent = detailInductiveBox.getNewborn().stream()
                    .map(newborn -> this.getBusinessContent(newborn, null))
                    .collect(Collectors.toList());
            Map<String, AbstractEntity> olderMapper = oldDetails.stream().collect(Collectors.toMap(detail -> (String) detail.loadIdentify(), detail -> detail));
            List<Map<String, Object>> updateDetailListContent = detailInductiveBox.getUpdate().stream()
                    .map(newDetail -> {
                        AbstractEntity olderDetail = olderMapper.get(newDetail.loadIdentify());
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
    }

    /**
     * 保存历史数据
     *
     * @param olderEntity 老实例
     * @param newEntity   新实例
     * @param session     数据库会话
     * @param operateName 操作方式名
     * @param operate     操作
     * @param operator    操作人
     * @throws SQLException e
     */
    void saveHistory(AbstractEntity olderEntity, AbstractEntity newEntity, Session session, String operateName, String operate, String operator) throws SQLException {
        Class<?> clazz = newEntity != null ? newEntity.getClass() : olderEntity.getClass();
        Entity entityAnnotation = clazz.getAnnotation(Entity.class);

        if (entityAnnotation != null) {
            String tableBusinessName = entityAnnotation.businessName();

            Map<String, Object> operateContent = new LinkedHashMap<>(4);
            operateContent.put("操作对象", tableBusinessName);
            operateContent.put("业务", operateName);
            operateContent.put("关键数据", this.getFlagBusinessContent(newEntity != null ? newEntity : olderEntity));
            switch (operate) {
                case Operate.ADD:
                    operateContent.put("新增的数据", this.getBusinessContent(newEntity, null));
                    break;
                case Operate.AUDIT:
                    operateContent.put("审核", this.getBusinessContent(newEntity, olderEntity));
                    break;
                case Operate.REVOKE:
                    operateContent.put("撤销审核", this.getBusinessContent(newEntity, olderEntity));
                    break;
                case Operate.EDIT:
                    operateContent.put("更新的数据", this.getBusinessContent(newEntity, olderEntity));
                    break;
                case Operate.DELETE:
                    operateContent.put("删除的数据", this.getBusinessContent(null, olderEntity));
                    break;
                default:
                    throw new IllegalArgumentException(String.format("%s - 该操作不产生历史数据。", operate));
            }

            ObjectMapper objectMapper = new ObjectMapper();
            String businessUuid = newEntity != null ? (String) newEntity.loadIdentify() : (String) olderEntity.loadIdentify();
            History history = null;
            try {
                history = new History(operate, new Date(), operator, businessUuid, objectMapper.writeValueAsString(operateContent));
            } catch (JsonProcessingException e) {
                LOGGER.warn(e.getMessage());
            }
            Objects.requireNonNull(session).save(history);
        }
    }

    /**
     * 生成操作历史内容（关键业务内容）
     *
     * @param newEntity 新
     * @return business content
     */
    private Map<String, Object> getFlagBusinessContent(AbstractEntity newEntity) {
        return Arrays.stream(MapperFactory.getKeyBusinessFields(newEntity.getClass().getName()))
                .map(field -> new HistoryData(newEntity, field))
                .collect(flagBusinessCollector);
    }

    /**
     * 获取业务历史数据
     *
     * @param newEntity   新实例
     * @param olderEntity 老实例
     * @return 历史数据
     */
    private Map<String, Object> getBusinessContent(AbstractEntity newEntity, AbstractEntity olderEntity) {
        if (olderEntity == null) {
            // NOTE: 新增
            return Arrays.stream(MapperFactory.getBusinessFields(newEntity.getClass().getName()))
                    .map(field -> new HistoryData(newEntity, null, field, OperateEnum.ADD))
                    .collect(businessCollector);
        } else if (newEntity != null) {
            // NOTE: 编辑
            List<Field> dirtyFields = Arrays.asList(ReflectUtils.getInstance().dirtyFieldFilter(newEntity, olderEntity));
            List<Field> onToManyFields = Arrays.asList(MapperFactory.getOneToMayFields(newEntity.getClass().getName()));
            return Arrays.stream(MapperFactory.getBusinessFields(newEntity.getClass().getName()))
                    .filter(field -> dirtyFields.contains(field) || onToManyFields.contains(field))
                    .map(field -> new HistoryData(newEntity, olderEntity, field, OperateEnum.EDIT))
                    .collect(businessCollector);
        } else {
            // NOTE: 删除
            return Arrays.stream(MapperFactory.getBusinessFields(olderEntity.getClass().getName()))
                    .map(field -> new HistoryData(null, olderEntity, field, OperateEnum.DELETE))
                    .collect(businessCollector);
        }
    }
}

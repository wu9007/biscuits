package org.hv.biscuits.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hv.biscuits.annotation.Affairs;
import org.hv.biscuits.annotation.Track;
import org.hv.biscuits.constant.OperateEnum;
import org.hv.biscuits.controller.FilterView;
import org.hv.biscuits.core.history.HistoryFactory;
import org.hv.biscuits.service.PageList;
import org.hv.biscuits.spine.AbstractWithOperatorEntity;
import org.hv.biscuits.spine.model.History;
import org.hv.biscuits.utils.CommonObjectMapper;
import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.annotation.Join;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.criteria.Modern;
import org.hv.pocket.criteria.Restrictions;
import org.hv.pocket.model.AbstractEntity;
import org.hv.pocket.model.DetailInductiveBox;
import org.hv.pocket.model.MapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author leyan95
 */
public abstract class AbstractCommonRepository<T extends AbstractEntity> extends AbstractRepository implements CommonRepository<T> {
    protected Class<T> genericClazz;
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommonRepository.class);
    private static final HistoryFactory HISTORY_FACTORY = HistoryFactory.getInstance();

    public AbstractCommonRepository() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        this.genericClazz = (Class<T>) params[0];
    }

    @Override
    public T findOne(Serializable uuid) throws SQLException {
        return super.getSession().findOne(this.genericClazz, uuid);
    }

    @Override
    public List<T> findAll() {
        return super.getSession().list(this.genericClazz);
    }

    @Override
    public int save(T obj, boolean cascade) throws SQLException, IllegalAccessException {
        return super.getSession().save(obj, cascade);
    }

    @Override
    public int forcibleSave(T obj, boolean cascade) throws SQLException, IllegalAccessException {
        return super.getSession().forcibleSave(obj, cascade);
    }

    @Override
    public int update(T obj, boolean cascade) throws SQLException, IllegalAccessException {
        return super.getSession().update(obj, cascade);
    }

    @Override
    public int delete(T obj) throws SQLException, IllegalAccessException {
        return super.getSession().delete(obj);
    }

    @Override
    @Track(data = "#obj", operateName = "#trackDescription", operator = "#trackOperator", operate = OperateEnum.ADD)
    public int saveWithTrack(T obj, boolean cascade, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException {
        return this.saveWithTrack(obj, false, cascade, trackOperator);
    }

    @Override
    @Track(data = "#obj", operateName = "#trackDescription", operator = "#trackOperator", operate = OperateEnum.ADD)
    public int forcibleSaveWithTrack(T obj, boolean cascade, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException {
        return this.saveWithTrack(obj, true, cascade, trackOperator);
    }

    @Override
    @Track(data = "#obj", operateName = "#trackDescription", operator = "#trackOperator", operate = OperateEnum.EDIT)
    public int updateWithTrack(T obj, boolean cascade, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException {
        if (obj instanceof AbstractWithOperatorEntity) {
            ((AbstractWithOperatorEntity) obj).setLastOperator(trackOperator).setLastOperationTime(LocalDateTime.now());
            Field[] detailField = MapperFactory.getOneToMayFields(obj.getClass().getName());
            if (cascade && detailField.length > 0) {
                T older = this.findOne(((AbstractWithOperatorEntity) obj).getUuid());
                for (Field field : detailField) {
                    Class<?> detailClazz = MapperFactory.getDetailClass(field.getDeclaringClass().getName(), field.getName());
                    if (AbstractWithOperatorEntity.class.isAssignableFrom(detailClazz)) {
                        field.setAccessible(true);
                        DetailInductiveBox detailBox = DetailInductiveBox.newInstance((List) field.get(obj), (List) field.get(older));
                        List<? extends AbstractWithOperatorEntity> newbornDetails = (List) detailBox.getNewborn();
                        for (AbstractWithOperatorEntity detail : newbornDetails) {
                            detail.setLastOperator(trackOperator).setLastOperationTime(((AbstractWithOperatorEntity) obj).getLastOperationTime());
                        }
                        List<? extends AbstractWithOperatorEntity> updateDetails = (List) detailBox.getUpdate();
                        for (AbstractWithOperatorEntity detail : updateDetails) {
                            detail.setLastOperator(trackOperator).setLastOperationTime(((AbstractWithOperatorEntity) obj).getLastOperationTime());
                        }
                    }
                }
            }
        }
        return this.update(obj, cascade);
    }

    @Override
    @Affairs
    public int atomUpdateWithTrack(T newObj, T oldObj, boolean cascade, String[] fieldNameArray, String operator, String trackDescription) throws Exception {
        int count = 0;
        // NOTE：如果fieldNameArray为null或空集合则将所有持久化属性填充到该数组
        if (fieldNameArray == null || fieldNameArray.length <= 0) {
            fieldNameArray = Arrays.stream(MapperFactory.getRepositoryFields(this.genericClazz.getName())).map(Field::getName).toArray(String[]::new);
        }
        List<String> onToManyFieldsName = Arrays.stream(MapperFactory.getOneToMayFields(this.genericClazz.getName())).map(Field::getName).collect(Collectors.toList());
        // NOTE: 生成历史数据
        Map<String, String> keyBusinessFieldMapper = Arrays.stream(MapperFactory.getKeyBusinessFields(genericClazz.getName()))
                .filter(field -> field.getAnnotation(Column.class) != null || field.getAnnotation(Join.class) != null)
                .collect(Collectors.toMap(Field::getName, item -> {
                    Column column = item.getAnnotation(Column.class);
                    if (column != null) {
                        return column.businessName();
                    } else {
                        Join join = item.getAnnotation(Join.class);
                        return join.businessName();
                    }
                }));
        Map<String, String> commonBusinessFieldsMapper = Arrays.stream(MapperFactory.getBusinessFields(genericClazz.getName()))
                .filter(field -> field.getAnnotation(Column.class) != null || field.getAnnotation(Join.class) != null)
                .collect(Collectors.toMap(Field::getName, item -> {
                    Column column = item.getAnnotation(Column.class);
                    if (column != null) {
                        return column.businessName();
                    } else {
                        Join join = item.getAnnotation(Join.class);
                        return join.businessName();
                    }
                }));
        Map<String, Object> operateContent = new LinkedHashMap<>(4);
        Entity entity = genericClazz.getAnnotation(Entity.class);
        operateContent.put("操作对象", entity.businessName());
        operateContent.put("业务", trackDescription);
        Map<String, Object> keyBusinessData = new HashMap<>(fieldNameArray.length);
        operateContent.put("关键数据", keyBusinessData);
        Map<String, Object> commonBusinessData = new HashMap<>(fieldNameArray.length);
        operateContent.put("更新的数据", commonBusinessData);
        // NOTE: 保存业务数据
        List<Restrictions> restrictionsList = new ArrayList<>();
        restrictionsList.add(Restrictions.equ("uuid", oldObj.loadIdentify()));
        List<Modern> modernList = new ArrayList<>();
        for (String fieldName : fieldNameArray) {
            Field field = MapperFactory.getField(this.genericClazz.getName(), fieldName);
            field.setAccessible(true);
            Object newValue = null;
            Object oldValue = null;
            try {
                newValue = field.get(newObj);
                oldValue = field.get(oldObj);
            } catch (IllegalAccessException e) {
                LOGGER.error("在实体类{}中找不到属性{}", this.genericClazz.getSimpleName(), fieldName);
            }
            if (onToManyFieldsName.contains(fieldName)) {
                if (cascade) {
                    // NOTE: 明细持久化
                    Class<?> detailClazz = MapperFactory.getDetailClass(genericClazz.getName(), fieldName);
                    String upFieldName = MapperFactory.getManyToOneUpField(detailClazz.getName(), genericClazz.getName());
                    String ownFieldName = MapperFactory.getOneToMayDownFieldName(genericClazz.getName(), fieldName);
                    Field upField = MapperFactory.getField(genericClazz.getName(), upFieldName);
                    upField.setAccessible(true);
                    Object upFieldValue = upField.get(newObj);
                    Field ownField = MapperFactory.getField(detailClazz.getName(), ownFieldName);
                    ownField.setAccessible(true);
                    List<AbstractEntity> newDetails = (List<AbstractEntity>) newValue;
                    for (AbstractEntity newDetail : newDetails) {
                        ownField.set(newDetail, upFieldValue);
                    }
                    List<AbstractEntity> oldDetails = (List<AbstractEntity>) oldValue;
                    DetailInductiveBox detailInductiveBox = DetailInductiveBox.newInstance(newDetails, oldDetails);
                    int affectRowCount = 0;
                    for (AbstractEntity detail : detailInductiveBox.getNewborn()) {
                        affectRowCount += this.getSession().save(detail, true);
                    }
                    for (AbstractEntity detail : detailInductiveBox.getMoribund()) {
                        affectRowCount += this.getSession().delete(detail, true);
                    }
                    for (AbstractEntity detail : detailInductiveBox.getUpdate()) {
                        affectRowCount += this.getSession().update(detail, true);
                    }
                    count += affectRowCount;
                    // NOTE: 生成历史数据
                    commonBusinessData.put(commonBusinessFieldsMapper.get(fieldName), HISTORY_FACTORY.getEditHistoryContent(newObj, oldObj, field));
                }
            } else {
                if (oldValue == null) {
                    restrictionsList.add(Restrictions.isNull(fieldName));
                } else {
                    restrictionsList.add(Restrictions.equ(fieldName, oldValue));
                }
                modernList.add(Modern.set(fieldName, newValue));
                if (!Objects.equals(newValue, oldValue)) {
                    // NOTE: 生成历史数据
                    String businessName = commonBusinessFieldsMapper.get(fieldName);
                    if (businessName == null) {
                        businessName = keyBusinessFieldMapper.get(fieldName);
                        if (StringUtils.isEmpty(businessName)) {
                            throw new Exception(String.format("实体类-%s的字段-%s未未设置业务名。", this.genericClazz.getName(), fieldName));
                        }
                    }
                    if (keyBusinessFieldMapper.containsKey(fieldName)) {
                        keyBusinessData.put(businessName, String.format("由 %s 改变为 %s", oldValue, newValue));
                    }
                    commonBusinessData.put(businessName, String.format("由 %s 改变为 %s", oldValue, newValue));
                }
            }
        }
        Criteria updateCriteria = this.getSession().createCriteria(genericClazz);
        Criteria historyQueryCriteria = this.getSession().createCriteria(genericClazz);
        for (Restrictions restrictions : restrictionsList) {
            updateCriteria.add(restrictions);
            historyQueryCriteria.add(restrictions);
        }
        for (Modern modern : modernList) {
            updateCriteria.add(modern);
        }
        List<T> persistenceObjs = historyQueryCriteria.specifyField("uuid").list();
        if (persistenceObjs == null || persistenceObjs.size() < 1) {
            throw new Exception("当前页面信息已发生变化，请刷新浏览后重新操作。");
        }
        List<Serializable> uuidList = persistenceObjs.stream().map(AbstractEntity::loadIdentify).collect(Collectors.toList());
        int mainCount = updateCriteria.update();
        count += mainCount;

        // NOTE: 保存历史数据
        ObjectMapper objectMapper = CommonObjectMapper.getInstance();
        try {
            for (Serializable serializable : uuidList) {
                History history = new History(OperateEnum.EDIT.getId(), new Date(), operator, (String) serializable, objectMapper.writeValueAsString(operateContent));
                this.getSession().save(history);
            }
        } catch (JsonProcessingException e) {
            LOGGER.warn(e.getMessage());
        }
        return count;
    }

    @Override
    @Track(data = "#obj", operateName = "#trackDescription", operator = "#trackOperator", operate = OperateEnum.DELETE)
    public int deleteWithTrack(T obj, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException {
        return this.delete(obj);
    }

    @Override
    public PageList<T> loadPage(FilterView filterView, String... fieldNames) throws SQLException {
        return this.loadPage(filterView, false, fieldNames);
    }

    @Override
    public List<T> loadListByFilter(FilterView filterView, String... fieldNames) {
        return this.loadListByFilter(filterView, false, fieldNames);
    }

    @Override
    public PageList<T> loadPageCascade(FilterView filterView, String... fieldNames) throws SQLException {
        return this.loadPage(filterView, true, fieldNames);
    }

    @Override
    public List<T> loadListCascadeByFilter(FilterView filterView, String... fieldNames) throws SQLException {
        return this.loadListByFilter(filterView, true, fieldNames);
    }

    private PageList<T> loadPage(FilterView filterView, boolean cascade, String... fieldNames) throws SQLException {
        Criteria criteria;
        if (filterView == null) {
            criteria = this.getSession().createCriteria(this.genericClazz);
        } else {
            criteria = filterView.createCriteria(this.getSession(), this.genericClazz);
            if (criteria == null) {
                return PageList.newInstance(new ArrayList<>(), 0);
            }
        }
        List<T> list = criteria.specifyField(fieldNames).listNotCleanRestrictions(cascade);
        return PageList.newInstance(list, criteria.count());
    }

    private List<T> loadListByFilter(FilterView filterView, boolean cascade, String... fieldNames) {
        Criteria criteria;
        if (filterView == null) {
            criteria = this.getSession().createCriteria(this.genericClazz);
        } else {
            criteria = filterView.createCriteria(this.getSession(), this.genericClazz);

            if (criteria == null) {
                return new ArrayList<>();
            }
        }
        return criteria.specifyField(fieldNames).list(cascade);
    }

    private int saveWithTrack(T obj, boolean forcible, boolean cascade, String trackOperator) throws SQLException, IllegalAccessException {
        if (obj instanceof AbstractWithOperatorEntity) {
            ((AbstractWithOperatorEntity) obj).setLastOperator(trackOperator).setLastOperationTime(LocalDateTime.now());
            Field[] detailField = MapperFactory.getOneToMayFields(obj.getClass().getName());
            if (cascade && detailField.length > 0) {
                for (Field field : detailField) {
                    Class<?> detailClazz = MapperFactory.getDetailClass(field.getDeclaringClass().getName(), field.getName());
                    if (AbstractWithOperatorEntity.class.isAssignableFrom(detailClazz)) {
                        field.setAccessible(true);
                        Iterable<? extends AbstractWithOperatorEntity> detailIterable = (Iterable) field.get(obj);
                        if (detailIterable != null) {
                            for (AbstractWithOperatorEntity detail : detailIterable) {
                                detail.setLastOperator(trackOperator).setLastOperationTime(((AbstractWithOperatorEntity) obj).getLastOperationTime());
                            }
                        }
                    }
                }
            }
        }
        if (forcible) {
            return this.forcibleSave(obj, cascade);
        } else {
            return this.save(obj, cascade);
        }
    }
}

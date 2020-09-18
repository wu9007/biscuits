package org.hv.biscuits.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hv.biscuits.annotation.Affairs;
import org.hv.biscuits.annotation.Track;
import org.hv.biscuits.constant.OperateEnum;
import org.hv.biscuits.controller.FilterView;
import org.hv.biscuits.service.PageList;
import org.hv.biscuits.spine.AbstractWithOperatorEntity;
import org.hv.biscuits.spine.model.History;
import org.hv.biscuits.utils.CommonObjectMapper;
import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.criteria.Modern;
import org.hv.pocket.criteria.Restrictions;
import org.hv.pocket.model.AbstractEntity;
import org.hv.pocket.model.DetailInductiveBox;
import org.hv.pocket.model.MapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.util.stream.Collectors;

/**
 * @author leyan95
 */
public abstract class AbstractCommonRepository<T extends AbstractEntity> extends AbstractRepository implements CommonRepository<T> {
    protected Class<T> genericClazz;
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommonRepository.class);

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
    public int atomUpdateWithTrack(T newObj, T oldObj, String[] fieldNameArray, String operator, String trackDescription) throws SQLException {
        // NOTE: 生成历史数据
        Map<String, String> keyBusinessFieldMapper = Arrays.stream(MapperFactory.getKeyBusinessFields(genericClazz.getName()))
                .collect(Collectors.toMap(Field::getName, item -> {
                    Column column = item.getAnnotation(Column.class);
                    return column.businessName();
                }));
        Map<String, String> commonBusinessFieldsMapper = Arrays.stream(MapperFactory.getBusinessFields(genericClazz.getName()))
                .collect(Collectors.toMap(Field::getName, item -> {
                    Column column = item.getAnnotation(Column.class);
                    return column.businessName();
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
        List<Modern> modernList = new ArrayList<>();
        for (String fieldName : fieldNameArray) {
            Field field = MapperFactory.getField(this.genericClazz.getName(), fieldName);
            Object newValue = null;
            Object oldValue = null;
            try {
                newValue = field.get(newObj);
                oldValue = field.get(oldObj);
            } catch (IllegalAccessException e) {
                LOGGER.error("在实体类{}中找不到属性{}", this.genericClazz.getSimpleName(), fieldName);
            }
            restrictionsList.add(Restrictions.equ(fieldName, oldValue));
            modernList.add(Modern.set(fieldName, newValue));

            // NOTE: 生成历史数据
            if (keyBusinessFieldMapper.containsKey(fieldName)) {
                keyBusinessData.put(keyBusinessFieldMapper.get(fieldName), String.format("由 %s 改变为 %s", oldValue, newValue));
            }
            commonBusinessData.put(commonBusinessFieldsMapper.get(fieldName), String.format("由 %s 改变为 %s", oldValue, newValue));
        }
        Criteria updateCriteria = this.getSession().createCriteria(genericClazz);
        Criteria queryCriteria = this.getSession().createCriteria(genericClazz);
        for (Restrictions restrictions : restrictionsList) {
            updateCriteria.add(restrictions);
            queryCriteria.add(restrictions);
        }
        for (Modern modern : modernList) {
            updateCriteria.add(modern);
        }
        int count = updateCriteria.update();

        // NOTE: 保存历史数据
        List<Serializable> uuidList = queryCriteria.specifyField("uuid").list().stream().map(AbstractEntity::loadIdentify).collect(Collectors.toList());
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

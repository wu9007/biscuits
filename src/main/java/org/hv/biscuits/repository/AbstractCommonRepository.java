package org.hv.biscuits.repository;

import org.hv.biscuits.constant.OperateEnum;
import org.hv.biscuits.service.PageList;
import org.hv.biscuits.spine.AbstractWithOperatorEntity;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.model.AbstractEntity;
import org.hv.biscuits.annotation.Track;
import org.hv.biscuits.controller.FilterView;
import org.hv.pocket.model.DetailInductiveBox;
import org.hv.pocket.model.MapperFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author leyan95
 */
public abstract class AbstractCommonRepository<T extends AbstractEntity> extends AbstractRepository implements CommonRepository<T> {
    protected Class<T> genericClazz;

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

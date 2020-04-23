package org.hv.biscuits.repository;

import org.hv.biscuits.constant.OperateEnum;
import org.hv.biscuits.service.PageList;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.model.BaseEntity;
import org.hv.biscuits.annotation.Track;
import org.hv.biscuits.controller.FilterView;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

/**
 * @author wujianchuan
 */
public abstract class AbstractCommonRepository<T extends BaseEntity> extends AbstractRepository implements CommonRepository<T> {
    protected Class<T> genericClazz;

    public AbstractCommonRepository() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        this.genericClazz = (Class<T>) params[0];
    }

    @Override
    public T findOne(Serializable uuid) throws SQLException {
        return (T) super.getSession().findOne(this.genericClazz, uuid);
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
        return this.save(obj, cascade);
    }

    @Override
    @Track(data = "#obj", operateName = "#trackDescription", operator = "#trackOperator", operate = OperateEnum.ADD)
    public int forcibleSaveWithTrack(T obj, boolean cascade, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException {
        return this.forcibleSave(obj, cascade);
    }

    @Override
    @Track(data = "#obj", operateName = "#trackDescription", operator = "#trackOperator", operate = OperateEnum.EDIT)
    public int updateWithTrack(T obj, boolean cascade, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException {
        return this.update(obj, cascade);
    }

    @Override
    @Track(data = "#obj", operateName = "#trackDescription", operator = "#trackOperator", operate = OperateEnum.DELETE)
    public int deleteWithTrack(T obj, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException {
        return this.delete(obj);
    }

    @Override
    public PageList<T> loadPage(FilterView filterView) throws SQLException {
        Criteria criteria;
        if (filterView == null) {
            criteria = this.getSession().createCriteria(this.genericClazz);
        } else {
            criteria = filterView.createCriteria(this.getSession(), this.genericClazz);
        }
        List<T> list = criteria.listNotCleanRestrictions();
        return PageList.newInstance(list, criteria.count());
    }
}

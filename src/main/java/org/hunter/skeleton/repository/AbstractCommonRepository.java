package org.hunter.skeleton.repository;

import org.hunter.pocket.criteria.Criteria;
import org.hunter.pocket.model.BaseEntity;
import org.hunter.skeleton.annotation.Track;
import org.hunter.skeleton.constant.OperateEnum;
import org.hunter.skeleton.controller.FilterView;
import org.hunter.skeleton.service.PageList;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

/**
 * @author wujianchuan
 */
public abstract class AbstractCommonRepository<T extends BaseEntity> extends AbstractRepository implements CommonRepository<T> {
    protected Class genericClazz;

    public AbstractCommonRepository() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        this.genericClazz = (Class) params[0];
    }

    @Override
    public Object findOne(Serializable uuid) throws SQLException {
        return super.getSession().findDirect(this.genericClazz, uuid);
    }

    @Override
    public int save(T obj, boolean cascade) throws SQLException, IllegalAccessException {
        return super.getSession().save(obj, cascade);
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
    @Track(data = "#obj", operateName = "#trackDescription", operator = "#trackOperator", operate = OperateEnum.EDIT)
    public int updateWithTrack(T obj, boolean cascade, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException {
        return this.update(obj, cascade);
    }

    @Override
    @Track(data = "#obj", operateName = "#trackDescription", operator = "#trackOperator", operate = OperateEnum.DELETE)
    public int deleteWithTrack(T obj, String operator, String trackDescription) throws SQLException, IllegalAccessException {
        return this.delete(obj);
    }

    @Override
    public PageList loadPage(FilterView filterView) throws SQLException {
        Criteria criteria;
        if (filterView == null) {
            criteria = this.getSession().createCriteria(this.genericClazz);
        } else {
            criteria = filterView.createCriteria(this.getSession(), this.genericClazz);
        }
        List list = criteria.listNotCleanRestrictions();
        return PageList.newInstance(list, criteria.count());
    }
}

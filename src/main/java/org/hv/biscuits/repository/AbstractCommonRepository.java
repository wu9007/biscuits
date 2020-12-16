package org.hv.biscuits.repository;

import org.hv.biscuits.controller.FilterView;
import org.hv.biscuits.service.PageList;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.model.AbstractEntity;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public int delete(T obj, boolean cascade) throws SQLException, IllegalAccessException {
        return super.getSession().delete(obj, cascade);
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
}

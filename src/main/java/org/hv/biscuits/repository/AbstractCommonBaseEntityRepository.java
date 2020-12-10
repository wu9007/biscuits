package org.hv.biscuits.repository;

import org.hv.biscuits.annotation.Track;
import org.hv.biscuits.constant.OperateEnum;
import org.hv.biscuits.controller.FilterView;
import org.hv.biscuits.service.PageList;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.model.AbstractEntity;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leyan95
 */
public abstract class AbstractCommonBaseEntityRepository extends AbstractRepository implements CommonBaseEntityRepository {

    public AbstractCommonBaseEntityRepository() {
    }

    @Override
    public <T extends AbstractEntity> T findOne(String className, Serializable uuid) throws SQLException, ClassNotFoundException {
        return super.getSession().findOne((Class<T>) Class.forName(className), uuid);
    }

    @Override
    public <T extends AbstractEntity> List<T> findAll(String className) throws ClassNotFoundException {
        return super.getSession().list((Class<T>) Class.forName(className));
    }

    @Override
    public int save(AbstractEntity obj, boolean cascade) throws SQLException, IllegalAccessException {
        return super.getSession().save(obj, cascade);
    }

    @Override
    public int forcibleSave(AbstractEntity obj, boolean cascade) throws SQLException, IllegalAccessException {
        return super.getSession().forcibleSave(obj, cascade);
    }

    @Override
    public int update(AbstractEntity obj, boolean cascade) throws SQLException, IllegalAccessException {
        return super.getSession().update(obj, cascade);
    }

    @Override
    public int delete(AbstractEntity obj) throws SQLException, IllegalAccessException {
        return super.getSession().delete(obj);
    }

    @Override
    @Track(data = "#obj", operateName = "#trackDescription", operator = "#trackOperator", operate = OperateEnum.ADD)
    public int saveWithTrack(AbstractEntity obj, boolean cascade, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException {
        return this.save(obj, cascade);
    }

    @Override
    @Track(data = "#obj", operateName = "#trackDescription", operator = "#trackOperator", operate = OperateEnum.ADD)
    public int forcibleSaveWithTrack(AbstractEntity obj, boolean cascade, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException {
        return this.forcibleSave(obj, cascade);
    }

    @Override
    @Track(data = "#obj", operateName = "#trackDescription", operator = "#trackOperator", operate = OperateEnum.EDIT)
    public int updateWithTrack(AbstractEntity obj, boolean cascade, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException {
        return this.update(obj, cascade);
    }

    @Override
    @Track(data = "#obj", operateName = "#trackDescription", operator = "#trackOperator", operate = OperateEnum.DELETE)
    public int deleteWithTrack(AbstractEntity obj, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException {
        return this.delete(obj);
    }

    @Override
    public <T extends AbstractEntity> PageList<T> loadPage(String className, FilterView filterView) throws SQLException, ClassNotFoundException {
        Criteria criteria;
        if (filterView == null) {
            criteria = this.getSession().createCriteria((Class<T>) Class.forName(className));
        } else {
            criteria = filterView.createCriteria(this.getSession(), (Class<T>) Class.forName(className));
            if (criteria == null) {
                return PageList.newInstance(new ArrayList<>(), 0);
            }
        }
        List<T> list = criteria.listNotCleanRestrictions();
        return PageList.newInstance(list, criteria.count());
    }
}

package org.hv.biscuits.repository;

import org.hv.biscuits.annotation.Track;
import org.hv.biscuits.constant.OperateEnum;
import org.hv.biscuits.controller.FilterView;
import org.hv.biscuits.service.PageList;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.model.BaseEntity;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

/**
 * @author wujianchuan
 */
public abstract class AbstractCommonBaseEntityRepository extends AbstractRepository implements CommonBaseEntityRepository
{
    protected Class clazz;

    public AbstractCommonBaseEntityRepository()
    {
    }

    @Override
    public BaseEntity findOne(String className, Serializable uuid) throws SQLException, ClassNotFoundException
    {
        return (BaseEntity) super.getSession().findOne(Class.forName(className), uuid);
    }

    @Override
    public List<BaseEntity> findAll(String className) throws ClassNotFoundException
    {
        return super.getSession().list(Class.forName(className));
    }

    @Override
    public int save(BaseEntity obj, boolean cascade) throws SQLException, IllegalAccessException
    {
        return super.getSession().save(obj, cascade);
    }

    @Override
    public int forcibleSave(BaseEntity obj, boolean cascade) throws SQLException, IllegalAccessException
    {
        return super.getSession().forcibleSave(obj, cascade);
    }

    @Override
    public int update(BaseEntity obj, boolean cascade) throws SQLException, IllegalAccessException
    {
        return super.getSession().update(obj, cascade);
    }

    @Override
    public int delete(BaseEntity obj) throws SQLException, IllegalAccessException
    {
        return super.getSession().delete(obj);
    }

    @Override
    @Track(data = "#obj", operateName = "#trackDescription", operator = "#trackOperator", operate = OperateEnum.ADD)
    public int saveWithTrack(BaseEntity obj, boolean cascade, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException
    {
        return this.save(obj, cascade);
    }

    @Override
    @Track(data = "#obj", operateName = "#trackDescription", operator = "#trackOperator", operate = OperateEnum.ADD)
    public int forcibleSaveWithTrack(BaseEntity obj, boolean cascade, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException
    {
        return this.forcibleSave(obj, cascade);
    }

    @Override
    @Track(data = "#obj", operateName = "#trackDescription", operator = "#trackOperator", operate = OperateEnum.EDIT)
    public int updateWithTrack(BaseEntity obj, boolean cascade, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException
    {
        return this.update(obj, cascade);
    }

    @Override
    @Track(data = "#obj", operateName = "#trackDescription", operator = "#trackOperator", operate = OperateEnum.DELETE)
    public int deleteWithTrack(BaseEntity obj, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException
    {
        return this.delete(obj);
    }

    @Override
    public PageList loadPage(String className, FilterView filterView) throws SQLException, ClassNotFoundException
    {
        Criteria criteria;
        if (filterView == null)
        {
            criteria = this.getSession().createCriteria(Class.forName(className));
        }
        else
        {
            criteria = filterView.createCriteria(this.getSession(), Class.forName(className));
        }
        List list = criteria.listNotCleanRestrictions();
        return PageList.newInstance(list, criteria.count());
    }
}

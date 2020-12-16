package org.hv.biscuits.repository;

import org.hv.biscuits.controller.FilterView;
import org.hv.biscuits.service.PageList;
import org.hv.pocket.model.AbstractEntity;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * @author leyan95
 */
public interface CommonRepository<T extends AbstractEntity> {
    /**
     * 根据数据标识加载数据 (默认级联查询).
     *
     * @param uuid 数据标识
     * @return T
     * @throws SQLException e
     */
    T findOne(Serializable uuid) throws SQLException;

    /**
     * 加载所有数据
     *
     * @return List
     */
    List<T> findAll();

    /**
     * 保存（字段为空的字段不进行保存）数据
     *
     * @param obj     需要保存的持久化实例数据，该实例类型必须继承自 {@link AbstractEntity}
     * @param cascade 是否级联更新
     * @return 影响行数
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int save(T obj, boolean cascade) throws SQLException, IllegalAccessException;

    /**
     * 强制（字段为空的字段也进行保存）保存数据
     *
     * @param obj     需要保存的持久化实例数据，该实例类型必须继承自 {@link AbstractEntity}
     * @param cascade 是否级联更新
     * @return 影响行数
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int forcibleSave(T obj, boolean cascade) throws SQLException, IllegalAccessException;

    /**
     * 更新数据
     *
     * @param obj     需要更新的持久化实例数据，该实例类型必须继承自 {@link AbstractEntity}
     * @param cascade 是否级联更新
     * @return 影响行数
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int update(T obj, boolean cascade) throws SQLException, IllegalAccessException;

    /**
     * 删除数据
     *
     * @param obj 需要删除的持久化实例数据，该实例类型必须继承自 {@link AbstractEntity}
     * @return 影响行数
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int delete(T obj) throws SQLException, IllegalAccessException;

    /**
     * 删除数据
     *
     * @param obj     需要删除的持久化实例数据，该实例类型必须继承自 {@link AbstractEntity}
     * @param cascade 是否级联更新
     * @return 影响行数
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int delete(T obj, boolean cascade) throws SQLException, IllegalAccessException;

    /**
     * 加载分页数据 非级联
     *
     * @param filterView 过滤条件
     * @param fieldNames 指定查询多个属性名
     * @return 分页数据
     * @throws SQLException e
     */
    PageList<T> loadPage(FilterView filterView, String... fieldNames) throws SQLException;

    /**
     * 加载分页数据 级联
     *
     * @param filterView 过滤条件
     * @param fieldNames 指定查询多个属性名
     * @return 分页数据
     * @throws SQLException e
     */
    PageList<T> loadPageCascade(FilterView filterView, String... fieldNames) throws SQLException;

    /**
     * 根据过滤条件查询集合 非级联
     *
     * @param filterView 过滤条件
     * @param fieldNames 指定查询多个属性名
     * @return 分页数据
     * @throws SQLException e
     */
    List<T> loadListByFilter(FilterView filterView, String... fieldNames) throws SQLException;

    /**
     * 根据过滤条件查询集合 级联
     *
     * @param filterView 过滤条件
     * @param fieldNames 指定查询多个属性名
     * @return 分页数据
     * @throws SQLException e
     */
    List<T> loadListCascadeByFilter(FilterView filterView, String... fieldNames) throws SQLException;
}

package org.hunter.skeleton.repository;

import org.hunter.pocket.model.BaseEntity;
import org.hunter.skeleton.controller.FilterView;
import org.hunter.skeleton.service.PageList;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * @author wujianchuan
 */
public interface UniversalOperate<T extends BaseEntity> extends Repository {
    /**
     * Select data by identify (Default with querying subsets).
     *
     * @param uuid Primary Key
     * @return T
     * @throws SQLException e
     */
    Object findOne(Serializable uuid) throws SQLException;

    /**
     * Save
     *
     * @param obj     New object who extend {@link BaseEntity}
     * @param cascade With save subsets
     * @return Number of rows affected
     * @throws SQLException e
     */
    int save(T obj, boolean cascade) throws SQLException;

    /**
     * Update
     *
     * @param obj     Persisted object who extend {@link BaseEntity}
     * @param cascade With update subsets
     * @return Number of rows affected
     * @throws SQLException e
     */
    int update(T obj, boolean cascade) throws SQLException;

    /**
     * Delete
     *
     * @param obj Delete object who extend {@link BaseEntity}
     * @return Number of rows affected
     * @throws SQLException e
     */
    int delete(T obj) throws SQLException, IllegalAccessException;

    /**
     * Save With Track
     *
     * @param obj              New object who extend {@link BaseEntity}
     * @param cascade          With save subsets
     * @param trackOperator    Track Operator
     * @param trackDescription Track Description
     * @return Number of rows affected
     * @throws SQLException e
     */
    int saveWithTrack(T obj, boolean cascade, String trackOperator, String trackDescription) throws SQLException;

    /**
     * Update With Track
     *
     * @param obj              Persisted object who extend {@link BaseEntity}
     * @param cascade          With update subsets
     * @param operator         Operator
     * @param trackDescription Track Description
     * @return Number of rows affected
     * @throws SQLException e
     */
    int updateWithTrack(T obj, boolean cascade, String operator, String trackDescription) throws SQLException;

    /**
     * Delete With Track
     *
     * @param obj              Delete object who extend {@link BaseEntity}
     * @param operator         Operator
     * @param trackDescription Track Description
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int deleteWithTrack(T obj, String operator, String trackDescription) throws SQLException, IllegalAccessException;

    PageList loadPage(FilterView filterView) throws SQLException;
}

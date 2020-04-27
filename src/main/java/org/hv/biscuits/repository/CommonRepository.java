package org.hv.biscuits.repository;

import org.hv.pocket.model.AbstractEntity;
import org.hv.biscuits.controller.FilterView;
import org.hv.biscuits.service.PageList;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * @author wujianchuan
 */
public interface CommonRepository<T extends AbstractEntity> {
    /**
     * Select data by identify (Default with querying subsets).
     *
     * @param uuid Primary Key
     * @return T
     * @throws SQLException e
     */
    T findOne(Serializable uuid) throws SQLException;

    /**
     * Load all data
     *
     * @return List<T
     */
    List<T> findAll();

    /**
     * Save
     *
     * @param obj     New object who extend {@link AbstractEntity}
     * @param cascade With save subsets
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int save(T obj, boolean cascade) throws SQLException, IllegalAccessException;

    /**
     * forcible save
     *
     * @param obj     New object who extend {@link AbstractEntity}
     * @param cascade With save subsets
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int forcibleSave(T obj, boolean cascade) throws SQLException, IllegalAccessException;

    /**
     * Update
     *
     * @param obj     Persisted object who extend {@link AbstractEntity}
     * @param cascade With update subsets
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int update(T obj, boolean cascade) throws SQLException, IllegalAccessException;

    /**
     * Delete
     *
     * @param obj Delete object who extend {@link AbstractEntity}
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int delete(T obj) throws SQLException, IllegalAccessException;

    /**
     * Save With Track
     *
     * @param obj              New object who extend {@link AbstractEntity}
     * @param cascade          With save subsets
     * @param trackOperator    Track Operator
     * @param trackDescription Track Description
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int saveWithTrack(T obj, boolean cascade, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException;


    /**
     * Forcible Save With Track
     *
     * @param obj              New object who extend {@link AbstractEntity}
     * @param cascade          With save subsets
     * @param trackOperator    Track Operator
     * @param trackDescription Track Description
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int forcibleSaveWithTrack(T obj, boolean cascade, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException;

    /**
     * Update With Track
     *
     * @param obj              Persisted object who extend {@link AbstractEntity}
     * @param cascade          With update subsets
     * @param operator         Operator
     * @param trackDescription Track Description
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int updateWithTrack(T obj, boolean cascade, String operator, String trackDescription) throws SQLException, IllegalAccessException;

    /**
     * Delete With Track
     *
     * @param obj              Delete object who extend {@link AbstractEntity}
     * @param operator         Operator
     * @param trackDescription Track Description
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int deleteWithTrack(T obj, String operator, String trackDescription) throws SQLException, IllegalAccessException;

    PageList<T> loadPage(FilterView filterView) throws SQLException;
}

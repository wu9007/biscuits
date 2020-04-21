package org.hv.biscuits.repository;

import org.hv.biscuits.controller.FilterView;
import org.hv.biscuits.service.PageList;
import org.hv.pocket.model.BaseEntity;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * @author wujianchuan
 */
public interface CommonBaseEntityRepository extends Repository
{
    /**
     * Select data by identify (Default with querying subsets).
     *
     * @param className Object Class Name
     * @param uuid      Primary Key
     * @return BaseEntity
     * @throws SQLException e
     */
    BaseEntity findOne(String className, Serializable uuid) throws SQLException, ClassNotFoundException;

    /**
     * Load all data
     *
     * @param className Object Class Name
     * @return List<BaseEntity>
     */
    List<BaseEntity> findAll(String className) throws ClassNotFoundException;

    /**
     * Save
     *
     * @param obj     New object who extend {@link BaseEntity}
     * @param cascade With save subsets
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int save(BaseEntity obj, boolean cascade) throws SQLException, IllegalAccessException;

    /**
     * forcible save
     *
     * @param obj     New object who extend {@link BaseEntity}
     * @param cascade With save subsets
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int forcibleSave(BaseEntity obj, boolean cascade) throws SQLException, IllegalAccessException;

    /**
     * Update
     *
     * @param obj     Persisted object who extend {@link BaseEntity}
     * @param cascade With update subsets
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int update(BaseEntity obj, boolean cascade) throws SQLException, IllegalAccessException;

    /**
     * Delete
     *
     * @param obj Delete object who extend {@link BaseEntity}
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int delete(BaseEntity obj) throws SQLException, IllegalAccessException;

    /**
     * Save With Track
     *
     * @param obj              New object who extend {@link BaseEntity}
     * @param cascade          With save subsets
     * @param trackOperator    Track Operator
     * @param trackDescription Track Description
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int saveWithTrack(BaseEntity obj, boolean cascade, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException;


    /**
     * Forcible Save With Track
     *
     * @param obj              New object who extend {@link BaseEntity}
     * @param cascade          With save subsets
     * @param trackOperator    Track Operator
     * @param trackDescription Track Description
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int forcibleSaveWithTrack(BaseEntity obj, boolean cascade, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException;

    /**
     * Update With Track
     *
     * @param obj              Persisted object who extend {@link BaseEntity}
     * @param cascade          With update subsets
     * @param operator         Operator
     * @param trackDescription Track Description
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int updateWithTrack(BaseEntity obj, boolean cascade, String operator, String trackDescription) throws SQLException, IllegalAccessException;

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
    int deleteWithTrack(BaseEntity obj, String operator, String trackDescription) throws SQLException, IllegalAccessException;

    PageList loadPage(String className, FilterView filterView) throws SQLException, ClassNotFoundException;
}

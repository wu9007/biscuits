package org.hv.biscuits.repository;

import org.hv.biscuits.controller.FilterView;
import org.hv.biscuits.service.PageList;
import org.hv.pocket.model.AbstractEntity;

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
     * @return AbstractEntity
     * @throws SQLException e
     */
    <T extends AbstractEntity> T  findOne(String className, Serializable uuid) throws SQLException, ClassNotFoundException;

    /**
     * Load all data
     *
     * @param className Object Class Name
     * @return List<AbstractEntity>
     */
    <T extends AbstractEntity> List<T> findAll(String className) throws ClassNotFoundException;

    /**
     * Save
     *
     * @param obj     New object who extend {@link AbstractEntity}
     * @param cascade With save subsets
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int save(AbstractEntity obj, boolean cascade) throws SQLException, IllegalAccessException;

    /**
     * forcible save
     *
     * @param obj     New object who extend {@link AbstractEntity}
     * @param cascade With save subsets
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int forcibleSave(AbstractEntity obj, boolean cascade) throws SQLException, IllegalAccessException;

    /**
     * Update
     *
     * @param obj     Persisted object who extend {@link AbstractEntity}
     * @param cascade With update subsets
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int update(AbstractEntity obj, boolean cascade) throws SQLException, IllegalAccessException;

    /**
     * Delete
     *
     * @param obj Delete object who extend {@link AbstractEntity}
     * @return Number of rows affected
     * @throws SQLException           e
     * @throws IllegalAccessException e
     */
    int delete(AbstractEntity obj) throws SQLException, IllegalAccessException;

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
    int saveWithTrack(AbstractEntity obj, boolean cascade, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException;


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
    int forcibleSaveWithTrack(AbstractEntity obj, boolean cascade, String trackOperator, String trackDescription) throws SQLException, IllegalAccessException;

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
    int updateWithTrack(AbstractEntity obj, boolean cascade, String operator, String trackDescription) throws SQLException, IllegalAccessException;

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
    int deleteWithTrack(AbstractEntity obj, String operator, String trackDescription) throws SQLException, IllegalAccessException;

    <T extends AbstractEntity> PageList<T> loadPage(String className, FilterView filterView) throws SQLException, ClassNotFoundException;
}

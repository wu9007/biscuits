package org.hunter.skeleton.spine.model.repository;

import org.hunter.skeleton.repository.Repository;
import org.hunter.skeleton.spine.model.BundleGroupRelation;

import java.sql.SQLException;

/**
 * @author wujianchuan 2019/3/1
 * @version 1.0
 */
public interface BundleGroupRelationRepository extends Repository {
    int save(BundleGroupRelation bundleGroupRelation) throws SQLException;

    BundleGroupRelation findByGroupUuidAndBundleUuid(String groupUuid, String bundleUuid) throws SQLException;

    int delete(BundleGroupRelation bundleGroupRelation) throws SQLException, IllegalAccessException;

    int updateSort(String groupUuid, String bundleUuid, double sort) throws SQLException;

    int deleteByGroupUuidAndBundleUuid(String groupUuid, String bundleUuid) throws SQLException;
}

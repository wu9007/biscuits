package org.hunter.skeleton.spine.model.repository;

import org.hunter.skeleton.repository.Repository;
import org.hunter.skeleton.spine.model.BundleGroupRelation;

/**
 * @author wujianchuan 2019/3/1
 * @version 1.0
 */
public interface BundleGroupRelationRepository extends Repository {
    int save(BundleGroupRelation bundleGroupRelation);

    BundleGroupRelation findByGroupUuidAndBundleUuid(String groupUuid, String bundleUuid);

    int delete(BundleGroupRelation bundleGroupRelation);

    int updateSort(String groupUuid, String bundleUuid, double sort);

    int deleteByGroupUuidAndBundleUuid(String groupUuid, String bundleUuid);
}

package org.hunter.skeleton.spine.model.repository;

import org.hunter.skeleton.repository.Repository;
import org.hunter.skeleton.spine.model.BundleGroupRelation;

/**
 * @author wujianchuan 2019/3/1
 * @version 1.0
 */
public interface BundleGroupRelationRepository extends Repository {
    int save(BundleGroupRelation bundleGroupRelation);

    BundleGroupRelation findByGroupUuidAndBundleUuid(long groupUuid, long bundleUuid);

    int delete(BundleGroupRelation bundleGroupRelation);

    int updateSort(long groupUuid, long bundleUuid, double sort);

    int deleteByGroupUuidAndBundleUuid(long groupUuid, long bundleUuid);
}

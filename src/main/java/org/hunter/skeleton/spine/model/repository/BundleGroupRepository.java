package org.hunter.skeleton.spine.model.repository;

import org.hunter.skeleton.controller.FilterView;
import org.hunter.skeleton.repository.Repository;
import org.hunter.skeleton.spine.model.BundleGroup;

import java.util.List;

/**
 * @author wujianchuan 2019/2/27
 * @version 1.0
 */
public interface BundleGroupRepository extends Repository {

    BundleGroup findOne(long uuid);

    List<BundleGroup> findAll(FilterView filterView);

    int save(BundleGroup bundleGroup, String avatar);

    int delete(BundleGroup bundleGroup, String avatar);

    int update(BundleGroup bundleGroup, String avatar);
}

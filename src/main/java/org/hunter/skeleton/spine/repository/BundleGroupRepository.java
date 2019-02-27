package org.hunter.skeleton.spine.repository;

import org.hunter.skeleton.repository.Repository;
import org.hunter.skeleton.spine.model.BundleGroup;

import java.util.List;

/**
 * @author wujianchuan 2019/2/27
 * @version 1.0
 */
public interface BundleGroupRepository extends Repository {

    List<BundleGroup> findAll();

    int save(BundleGroup bundleGroup);
}

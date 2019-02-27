package org.hunter.skeleton.spine.service;

import org.hunter.skeleton.spine.model.BundleGroup;

import java.util.List;

/**
 * @author wujianchuan 2019/2/27
 * @version 1.0
 */
public interface BundleGroupService {

    List<BundleGroup> loadGroups();

    int saveGroup(BundleGroup bundleGroup);
}

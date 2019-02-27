package org.hunter.skeleton.spine.service.impl;

import org.hunter.skeleton.annotation.Service;
import org.hunter.skeleton.service.AbstractService;
import org.hunter.skeleton.spine.model.BundleGroup;
import org.hunter.skeleton.spine.repository.BundleGroupRepository;
import org.hunter.skeleton.spine.service.BundleGroupService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wujianchuan 2019/2/27
 * @version 1.0
 */
@Service(session = "skeleton")
public class BundleGroupServiceImpl extends AbstractService implements BundleGroupService {
    private final BundleGroupRepository repository;

    @Autowired
    public BundleGroupServiceImpl(BundleGroupRepository repository) {
        this.repository = repository;
        this.repository.setSession(this.session);
    }

    @Override
    public List<BundleGroup> loadGroups() {
        return this.repository.findAll();
    }

    @Override
    public void saveGroup(BundleGroup bundleGroup) {
        this.repository.save(bundleGroup);
    }
}

package org.hunter.skeleton.spine.service.impl;

import org.hunter.skeleton.annotation.Service;
import org.hunter.skeleton.service.AbstractService;
import org.hunter.skeleton.spine.model.Bundle;
import org.hunter.skeleton.spine.repository.BundleRepository;
import org.hunter.skeleton.spine.service.BundleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
@Service(session = "skeleton")
public class BundleServiceImpl extends AbstractService implements BundleService {
    private final
    BundleRepository repository;

    @Autowired
    public BundleServiceImpl(BundleRepository repository) {
        this.repository = repository;
        this.repository.setSession(this.session);
    }

    @Override
    public List<Bundle> getNoAuthBundle() {
        return this.repository.findByAuthIf(false);
    }

    @Override
    public Bundle findOne(Long uuid) {
        return (Bundle) this.session.findOne(Bundle.class, uuid);
    }

    @Override
    public List<Bundle> findNoGrouping() {
        return this.repository.findAll().stream()
                .filter(bundle -> bundle.getBundleGroupRelations() == null || bundle.getBundleGroupRelations().size() <= 0)
                .collect(Collectors.toList());
    }
}

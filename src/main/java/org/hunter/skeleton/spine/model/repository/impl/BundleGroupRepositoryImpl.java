package org.hunter.skeleton.spine.model.repository.impl;

import org.hunter.pocket.criteria.Criteria;
import org.hunter.skeleton.repository.AbstractRepository;
import org.hunter.skeleton.spine.model.BundleGroup;
import org.hunter.skeleton.spine.model.repository.BundleGroupRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wujianchuan 2019/2/27
 * @version 1.0
 */
@Repository
public class BundleGroupRepositoryImpl extends AbstractRepository implements BundleGroupRepository {
    @Override
    public List<BundleGroup> findAll() {
        Criteria criteria = this.getSession().creatCriteria(BundleGroup.class);
        return criteria.list();
    }

    @Override
    public int save(BundleGroup bundleGroup) {
        return this.getSession().save(bundleGroup);
    }
}

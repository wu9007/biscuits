package org.hunter.skeleton.spine.model.repository.impl;

import org.hunter.pocket.criteria.Criteria;
import org.hunter.pocket.criteria.Modern;
import org.hunter.pocket.criteria.Restrictions;
import org.hunter.skeleton.repository.AbstractRepository;
import org.hunter.skeleton.spine.model.BundleGroupRelation;
import org.hunter.skeleton.spine.model.repository.BundleGroupRelationRepository;
import org.springframework.stereotype.Repository;

/**
 * @author wujianchuan 2019/3/1
 * @version 1.0
 */
@Repository
public class BundleGroupRelationRepositoryImpl extends AbstractRepository implements BundleGroupRelationRepository {
    @Override
    public int save(BundleGroupRelation bundleGroupRelation) {
        return this.getSession().save(bundleGroupRelation);
    }

    @Override
    public BundleGroupRelation findByGroupUuidAndBundleUuid(long groupUuid, long bundleUuid) {
        Criteria criteria = this.getSession().creatCriteria(BundleGroupRelation.class)
                .add(Restrictions.equ("groupUuid", groupUuid))
                .add(Restrictions.equ("bundleUuid", bundleUuid));
        return (BundleGroupRelation) criteria.unique();
    }

    @Override
    public int delete(BundleGroupRelation bundleGroupRelation) {
        return this.getSession().delete(bundleGroupRelation);
    }

    @Override
    public int updateSort(long groupUuid, long bundleUuid, double sort) {
        Criteria criteria = this.getSession().creatCriteria(BundleGroupRelation.class);
        criteria.add(Modern.set("sort", sort))
                .add(Restrictions.equ("groupUuid", groupUuid))
                .add(Restrictions.equ("bundleUuid", bundleUuid));
        return criteria.update();
    }

    @Override
    public int deleteByGroupUuidAndBundleUuid(long groupUuid, long bundleUuid) {
        return (int) this.getSession().creatCriteria(BundleGroupRelation.class)
                .add(Restrictions.equ("groupUuid", groupUuid))
                .add(Restrictions.equ("bundleUuid", bundleUuid))
                .delete();
    }
}

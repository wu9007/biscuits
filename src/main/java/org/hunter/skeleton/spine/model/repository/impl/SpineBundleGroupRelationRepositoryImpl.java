package org.hunter.skeleton.spine.model.repository.impl;

import org.hunter.pocket.criteria.Criteria;
import org.hunter.pocket.criteria.Modern;
import org.hunter.pocket.criteria.Restrictions;
import org.hunter.skeleton.repository.AbstractRepository;
import org.hunter.skeleton.spine.model.BundleGroupRelation;
import org.hunter.skeleton.spine.model.repository.SpineBundleGroupRelationRepository;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

/**
 * @author wujianchuan 2019/3/1
 * @version 1.0
 */
@Repository
public class SpineBundleGroupRelationRepositoryImpl extends AbstractRepository implements SpineBundleGroupRelationRepository {
    @Override
    public int save(BundleGroupRelation bundleGroupRelation) throws SQLException {
        return this.getSession().save(bundleGroupRelation);
    }

    @Override
    public BundleGroupRelation findByGroupUuidAndBundleUuid(String groupUuid, String bundleUuid) throws SQLException {
        Criteria criteria = this.getSession().createCriteria(BundleGroupRelation.class)
                .add(Restrictions.equ("groupUuid", groupUuid))
                .add(Restrictions.equ("bundleUuid", bundleUuid));
        return (BundleGroupRelation) criteria.unique();
    }

    @Override
    public int delete(BundleGroupRelation bundleGroupRelation) throws SQLException, IllegalAccessException {
        return this.getSession().delete(bundleGroupRelation);
    }

    @Override
    public int updateSort(String groupUuid, String bundleUuid, double sort) throws SQLException {
        Criteria criteria = this.getSession().createCriteria(BundleGroupRelation.class);
        criteria.add(Modern.set("sort", sort))
                .add(Restrictions.equ("groupUuid", groupUuid))
                .add(Restrictions.equ("bundleUuid", bundleUuid));
        return criteria.update();
    }

    @Override
    public int deleteByGroupUuidAndBundleUuid(String groupUuid, String bundleUuid) throws SQLException {
        return (int) this.getSession().createCriteria(BundleGroupRelation.class)
                .add(Restrictions.equ("groupUuid", groupUuid))
                .add(Restrictions.equ("bundleUuid", bundleUuid))
                .delete();
    }
}

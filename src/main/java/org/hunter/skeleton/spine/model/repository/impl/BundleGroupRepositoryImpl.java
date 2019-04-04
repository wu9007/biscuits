package org.hunter.skeleton.spine.model.repository.impl;

import org.hunter.pocket.criteria.Criteria;
import org.hunter.pocket.criteria.Restrictions;
import org.hunter.pocket.criteria.Sort;
import org.hunter.skeleton.controller.FilterView;
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
    public BundleGroup findOne(long uuid) {
        return (BundleGroup) this.getSession().findDirect(BundleGroup.class, uuid);
    }

    @Override
    public List<BundleGroup> findAll(FilterView filterView) {
        Criteria criteria = this.getSession().createCriteria(BundleGroup.class)
                .add(Sort.asc("sort"));
        if (filterView != null && filterView.getFilter() != null && filterView.getKeyWord() != null) {
            criteria.add(Restrictions.or(
                    Restrictions.like("groupName", "%" + filterView.getFilter().get("keyWord") + "%"),
                    Restrictions.like("groupId", "%" + filterView.getFilter().get("keyWord") + "%"))
            );
        }
        return criteria.list(true);
    }

    @Override
    public int save(BundleGroup bundleGroup) {
        return this.getSession().save(bundleGroup);
    }

    @Override
    public int delete(BundleGroup bundleGroup) {
        return this.getSession().delete(bundleGroup);
    }

    @Override
    public int update(BundleGroup bundleGroup) {
        return this.getSession().update(bundleGroup);
    }
}

package org.hunter.skeleton.spine.model.repository.impl;

import org.hunter.pocket.criteria.Criteria;
import org.hunter.pocket.criteria.Restrictions;
import org.hunter.pocket.criteria.Sort;
import org.hunter.skeleton.controller.FilterView;
import org.hunter.skeleton.repository.AbstractRepository;
import org.hunter.skeleton.spine.model.BundleGroup;
import org.hunter.skeleton.spine.model.repository.SpineBundleGroupRepository;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * @author wujianchuan 2019/2/27
 * @version 1.0
 */
@Repository
public class SpineBundleGroupRepositoryImpl extends AbstractRepository implements SpineBundleGroupRepository {
    @Override
    public BundleGroup findOne(String uuid) throws SQLException {
        return (BundleGroup) this.getSession().findDirect(BundleGroup.class, uuid);
    }

    @Override
    public List<BundleGroup> findAll(FilterView filterView) {
        Criteria criteria = this.getSession().createCriteria(BundleGroup.class)
                .add(Sort.asc("sort"));
        if (filterView != null && filterView.getFilter() != null) {
            String keyWorld = (String) filterView.getFilter().get("keyWorld");
            if (keyWorld != null && keyWorld.length() > 0) {
                criteria.add(Restrictions.or(
                        Restrictions.like("groupName", "%" + filterView.getFilter().get("keyWord") + "%"),
                        Restrictions.like("groupId", "%" + filterView.getFilter().get("keyWord") + "%"))
                );
            }
        }
        return criteria.list(true);
    }

    @Override
    public int save(BundleGroup bundleGroup) throws SQLException {
        return this.getSession().save(bundleGroup);
    }

    @Override
    public int delete(BundleGroup bundleGroup) throws SQLException, IllegalAccessException {
        return this.getSession().delete(bundleGroup);
    }

    @Override
    public int update(BundleGroup bundleGroup) throws SQLException {
        return this.getSession().update(bundleGroup);
    }
}

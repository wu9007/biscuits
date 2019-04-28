package org.hunter.skeleton.spine.model.repository.impl;

import org.hunter.pocket.criteria.Criteria;
import org.hunter.pocket.criteria.Restrictions;
import org.hunter.skeleton.repository.AbstractRepository;
import org.hunter.skeleton.spine.model.Bundle;
import org.hunter.skeleton.spine.model.repository.BundleRepository;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
@Repository
public class BundleRepositoryImpl extends AbstractRepository implements BundleRepository {
    @Override
    public List<Bundle> findByAuthIf(Boolean auth) {
        Criteria criteria = this.getSession().createCriteria(Bundle.class);
        criteria.add(Restrictions.equ("withAuth", false));
        return criteria.list();
    }

    @Override
    public Bundle findOne(String uuid) throws SQLException {
        return (Bundle) this.getSession().findOne(Bundle.class, uuid);
    }

    @Override
    public List<Bundle> findAll() {
        Criteria criteria = this.getSession().createCriteria(Bundle.class);
        return criteria.list(true);
    }
}

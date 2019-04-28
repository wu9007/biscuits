package org.hunter.skeleton.spine.model.repository.impl;

import org.hunter.pocket.criteria.Criteria;
import org.hunter.pocket.criteria.Restrictions;
import org.hunter.skeleton.repository.AbstractRepository;
import org.hunter.skeleton.spine.model.Role;
import org.hunter.skeleton.spine.model.repository.RoleRepository;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

/**
 * @author wujianchuan 2019/2/21
 */
@Repository
public class RoleRepositoryImpl extends AbstractRepository implements RoleRepository {
    @Override
    public Role findOne(String uuid) throws SQLException {
        return (Role) this.getSession().findOne(Role.class, uuid);
    }

    @Override
    public Role findById(String id) throws SQLException {
        Criteria criteria = this.getSession().createCriteria(Role.class);
        criteria.add(Restrictions.equ("id", id));
        return (Role) criteria.unique(true);
    }
}

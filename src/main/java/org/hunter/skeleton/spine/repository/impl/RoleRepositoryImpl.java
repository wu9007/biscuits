package org.hunter.skeleton.spine.repository.impl;

import org.hunter.pocket.criteria.Criteria;
import org.hunter.pocket.criteria.Restrictions;
import org.hunter.skeleton.repository.AbstractRepository;
import org.hunter.skeleton.spine.model.Role;
import org.hunter.skeleton.spine.repository.RoleRepository;
import org.springframework.stereotype.Repository;

/**
 * @author wujianchuan 2019/2/21
 */
@Repository
public class RoleRepositoryImpl extends AbstractRepository implements RoleRepository {
    @Override
    public Role findOne(Long uuid) {
        return (Role) this.session().findOne(Role.class, uuid);
    }

    @Override
    public Role findById(String id) {
        Criteria criteria = this.session().creatCriteria(Role.class);
        criteria.add(Restrictions.equ("id", id));
        return (Role) criteria.unique(true);
    }
}

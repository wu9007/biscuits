package org.hv.biscuit.spine.model.repository.impl;

import org.hv.biscuit.repository.AbstractRepository;
import org.hv.biscuit.spine.model.Role;
import org.hv.biscuit.spine.model.repository.SpineRoleRepository;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

/**
 * @author wujianchuan 2019/2/21
 */
@Repository
public class SpineRoleRepositoryImpl extends AbstractRepository implements SpineRoleRepository {
    @Override
    public Role findOne(String uuid) throws SQLException {
        return (Role) this.getSession().findOne(Role.class, uuid);
    }
}

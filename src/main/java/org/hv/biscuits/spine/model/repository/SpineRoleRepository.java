package org.hv.biscuits.spine.model.repository;

import org.hv.biscuits.spine.model.Role;

import java.sql.SQLException;

/**
 * @author leyan95 2019/2/21
 */
public interface SpineRoleRepository {

    Role findOne(String uuid) throws SQLException;
}

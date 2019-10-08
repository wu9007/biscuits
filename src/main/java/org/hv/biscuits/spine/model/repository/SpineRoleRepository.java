package org.hv.biscuits.spine.model.repository;

import org.hv.biscuits.repository.Repository;
import org.hv.biscuits.spine.model.Role;

import java.sql.SQLException;

/**
 * @author wujianchuan 2019/2/21
 */
public interface SpineRoleRepository extends Repository {

    Role findOne(String uuid) throws SQLException;
}

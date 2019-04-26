package org.hunter.skeleton.spine.model.repository;

import org.hunter.skeleton.repository.Repository;
import org.hunter.skeleton.spine.model.Role;

import java.sql.SQLException;

/**
 * @author wujianchuan 2019/2/21
 */
public interface RoleRepository extends Repository {

    Role findOne(String uuid);

    Role findById(String id) throws SQLException;
}

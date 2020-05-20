package org.hv.demo.bundles.authority.repository;

import org.hv.biscuits.repository.CommonRepository;
import org.hv.biscuits.spine.model.User;

import java.sql.SQLException;

/**
 * @author leyan95
 */
public interface UserRepository extends CommonRepository<User> {

    User findByAvatarAndPassword(String avatar, String password) throws SQLException;
}

package org.hv.demo.repository;

import org.hv.biscuit.repository.CommonRepository;
import org.hv.biscuit.spine.model.User;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
public interface UserRepository extends CommonRepository<User> {

    User findByAvatarAndPassword(String avatar, String password) throws SQLException;
}

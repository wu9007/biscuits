package org.hunter.demo.repository;

import org.hunter.skeleton.repository.CommonRepository;
import org.hunter.skeleton.spine.model.User;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
public interface UserRepository extends CommonRepository<User> {

    User findByAvatarAndPassword(String avatar, String password) throws SQLException;
}

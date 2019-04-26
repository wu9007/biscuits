package org.hunter.skeleton.spine.model.repository;

import org.hunter.skeleton.repository.Repository;
import org.hunter.skeleton.spine.model.User;

import java.sql.SQLException;

/**
 * @author wujianchuan 2019/1/30
 */
public interface UserRepository extends Repository {

    /**
     * find user by uuid.
     *
     * @param uuid uuid.
     * @return user.
     */
    User findOne(String uuid);

    /**
     * save user.
     *
     * @param user   user.
     * @param avatar avatar.
     * @return effect row number.
     */
    int save(User user, String avatar) throws SQLException;

    /**
     * find user by avatar.
     *
     * @param avatar user avatar.
     * @return user.
     */
    User findByAvatar(String avatar) throws SQLException;

    /**
     * find user by avatar and password.
     *
     * @param avatar   user avatar.
     * @param password user password.
     * @return user.
     */
    User findByAvatarAndPassword(String avatar, String password) throws SQLException;

    boolean canPass(String userCode, String serverId, String bundleId, String actionId) throws SQLException;
}

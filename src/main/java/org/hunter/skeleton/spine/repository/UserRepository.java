package org.hunter.skeleton.spine.repository;

import org.hunter.skeleton.repository.Repository;
import org.hunter.skeleton.spine.model.User;

/**
 * @author wujianchuan 2019/1/30
 */
public interface UserRepository extends Repository {

    /**
     * save user.
     *
     * @param user user.
     * @return effect row number.
     */
    int saveUser(User user);

    /**
     * find user by avatar.
     *
     * @param avatar user avatar.
     * @return user model.
     */
    User findByAvatar(String avatar);

    /**
     * find user by avatar and password.
     *
     * @param avatar   user avatar.
     * @param password user password.
     * @return user.
     */
    User findByAvatarAndPassword(String avatar, String password);
}

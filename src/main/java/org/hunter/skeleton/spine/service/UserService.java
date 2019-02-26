package org.hunter.skeleton.spine.service;

import org.hunter.skeleton.spine.model.User;

/**
 * @author wujianchuan 2019/1/30
 */
public interface UserService {

    /**
     * load user.
     *
     * @param avatar user avatar.
     * @return user model.
     */
    User loadUser(String avatar);

    /**
     * load user.
     *
     * @param avatar   user avatar.
     * @param password user password.
     * @return token.
     */
    User loadUser(String avatar, String password);

    /**
     * save user.
     *
     * @param user user.
     * @return token.
     */
    User register(User user);

    /**
     * 验证信息是否可注册
     *
     * @param user 用户信息
     * @return 是否可注册
     */
    boolean verify(User user);
}
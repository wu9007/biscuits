package org.hunter.skeleton.spine.service;

import org.hunter.skeleton.spine.model.User;
import reactor.core.publisher.Mono;

/**
 * @author wujianchuan 2019/1/30
 */
public interface UserService {

    /**
     * load user.
     *
     * @param avatar user avatar.
     * @return user model.
     * @throws Exception e.
     */
    User loadUser(String avatar) throws Exception;

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
}

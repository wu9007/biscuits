package org.hunter.skeleton.spine.service.impl;

import org.hunter.skeleton.annotation.Affairs;
import org.hunter.skeleton.annotation.Service;
import org.hunter.skeleton.service.AbstractService;
import org.hunter.skeleton.spine.model.User;
import org.hunter.skeleton.spine.repository.UserRepository;
import org.hunter.skeleton.spine.service.UserService;
import org.hunter.skeleton.spine.utils.EncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wujianchuan 2019/1/30
 */
@Service(session = "skeleton")
public class UserServiceImpl extends AbstractService implements UserService {

    private final UserRepository userRepository;

    private final EncodeUtil encodeUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, EncodeUtil encodeUtil) {
        this.userRepository = userRepository;
        this.encodeUtil = encodeUtil;
        this.userRepository.construct(this.sessionLocal);
    }

    @Override
    public User loadUser(String avatar) {
        return this.userRepository.findByAvatar(avatar);
    }

    @Override
    @Affairs
    public User loadUser(String avatar, String password) {
        if (avatar == null || password == null) {
            return null;
        } else {
            return this.userRepository.findByAvatarAndPassword(avatar, encodeUtil.encoderByMd5(password));
        }
    }

    @Override
    public User register(User user) {
        if (user != null && user.getAvatar() != null && user.getPassword() != null) {
            user.setPassword(encodeUtil.encoderByMd5(user.getPassword()));
            int effectRowNum = this.userRepository.saveUser(user);
            if (effectRowNum == 1) {
                return user;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean verify(User user) {
        return this.userRepository.findByAvatar(user.getAvatar()) == null;
    }
}

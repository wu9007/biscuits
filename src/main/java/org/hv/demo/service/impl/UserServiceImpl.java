package org.hv.demo.service.impl;

import org.hv.biscuits.annotation.Service;
import org.hv.biscuits.service.AbstractService;
import org.hv.biscuits.spine.model.User;
import org.hv.demo.service.UserService;
import org.hv.biscuits.spine.utils.EncodeUtil;
import org.hv.biscuits.utils.TokenUtil;
import org.hv.demo.repository.UserRepository;

import java.sql.SQLException;
import java.util.Date;

/**
 * @author wujianchuan
 */
@Service(session = "demo")
public class UserServiceImpl extends AbstractService implements UserService {
    private final EncodeUtil encodeUtil;
    private final UserRepository userRepository;

    public UserServiceImpl(EncodeUtil encodeUtil, UserRepository userRepository) {
        this.encodeUtil = encodeUtil;
        this.userRepository = userRepository;
    }

    @Override
    public User register(String avatar, String name, String password) throws SQLException, IllegalAccessException {
        User user = new User();
        user.setAvatar(avatar);
        user.setName(name);
        user.setEnable(true);
        user.setPassword(encodeUtil.abcEncoder(password));
        user.setLastPasswordResetDate(new Date());
        user.setLastRoleModifyDate(new Date());
        this.userRepository.save(user, false);
        return user;
    }
}

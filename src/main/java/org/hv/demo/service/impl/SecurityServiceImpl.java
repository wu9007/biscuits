package org.hv.demo.service.impl;

import org.hv.biscuit.annotation.Service;
import org.hv.biscuit.service.AbstractService;
import org.hv.biscuit.spine.model.User;
import org.hv.biscuit.spine.utils.EncodeUtil;
import org.hv.biscuit.utils.TokenUtil;
import org.hv.demo.repository.UserRepository;
import org.hv.demo.service.SecurityService;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
@Service(session = "demo")
public class SecurityServiceImpl extends AbstractService implements SecurityService {
    private final TokenUtil tokenUtil;
    private final EncodeUtil encodeUtil;
    private final UserRepository userRepository;

    public SecurityServiceImpl(TokenUtil tokenUtil, EncodeUtil encodeUtil, UserRepository userRepository) {
        this.tokenUtil = tokenUtil;
        this.encodeUtil = encodeUtil;
        this.userRepository = userRepository;
    }

    @Override
    public String generateToken(String avatar, String password) throws SQLException {
        User user = this.userRepository.findByAvatarAndPassword(avatar, this.encodeUtil.abcEncoder(password));
        if (user == null) {
            throw new IllegalArgumentException("The username or password is incorrect. Please fill in again.");
        }
        return this.tokenUtil.generateToken(user);
    }

    @Override
    public User register(String avatar, String name, String password) throws SQLException, IllegalAccessException {
        User user = new User();
        user.setAvatar(avatar);
        user.setName(name);
        user.setEnable(true);
        user.setPassword(encodeUtil.abcEncoder(password));
        this.userRepository.save(user, false);
        return user;
    }
}

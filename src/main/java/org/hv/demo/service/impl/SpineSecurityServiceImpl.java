package org.hv.demo.service.impl;

import org.hv.biscuits.annotation.Service;
import org.hv.biscuits.service.AbstractService;
import org.hv.biscuits.spine.model.User;
import org.hv.demo.service.SpineSecurityService;
import org.hv.biscuits.spine.utils.EncodeUtil;
import org.hv.biscuits.utils.TokenUtil;
import org.hv.demo.repository.UserRepository;

import java.sql.SQLException;
import java.util.Date;

/**
 * @author wujianchuan
 */
@Service(session = "demo")
public class SpineSecurityServiceImpl extends AbstractService implements SpineSecurityService {
    private final TokenUtil tokenUtil;
    private final EncodeUtil encodeUtil;
    private final UserRepository userRepository;

    public SpineSecurityServiceImpl(TokenUtil tokenUtil, EncodeUtil encodeUtil, UserRepository userRepository) {
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
        user.setLastPasswordResetDate(new Date());
        user.setLastRoleModifyDate(new Date());
        this.userRepository.shallowSave(user, false);
        return user;
    }
}

package org.hv.demo.bundles.authority.service;

import org.hv.biscuits.annotation.Service;
import org.hv.biscuits.controller.UserView;
import org.hv.biscuits.service.AbstractService;
import org.hv.biscuits.spine.model.User;
import org.hv.biscuits.spine.utils.EncodeUtil;
import org.hv.biscuits.utils.TokenUtil;
import org.hv.demo.bundles.authority.repository.UserRepository;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author leyan95
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
        UserView userView = UserView.newInstance().setUuid(user.getUuid()).setAvatar(user.getAvatar()).setName(user.getName()).setAuthIds(new ArrayList());
        return this.tokenUtil.generateToken(userView);
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

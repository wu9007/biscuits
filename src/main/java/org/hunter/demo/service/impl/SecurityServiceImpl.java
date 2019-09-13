package org.hunter.demo.service.impl;

import org.hunter.demo.repository.UserRepository;
import org.hunter.demo.service.SecurityService;
import org.hunter.skeleton.annotation.Service;
import org.hunter.skeleton.service.AbstractService;
import org.hunter.skeleton.spine.model.User;
import org.hunter.skeleton.spine.utils.EncodeUtil;
import org.hunter.skeleton.utils.TokenUtil;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
@Service(session = "skeleton")
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
        return this.tokenUtil.generateToken(user);
    }
}

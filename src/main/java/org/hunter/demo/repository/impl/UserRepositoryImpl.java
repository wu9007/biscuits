package org.hunter.demo.repository.impl;

import org.hunter.demo.repository.UserRepository;
import org.hunter.pocket.criteria.Criteria;
import org.hunter.pocket.criteria.Restrictions;
import org.hunter.skeleton.repository.AbstractCommonRepository;
import org.hunter.skeleton.spine.model.User;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
@Component
public class UserRepositoryImpl extends AbstractCommonRepository<User> implements UserRepository {
    @Override
    public User findByAvatarAndPassword(String avatar, String password) throws SQLException {
        Criteria criteria = super.getSession().createCriteria(User.class);
        criteria.add(Restrictions.equ("avatar", avatar))
                .add(Restrictions.equ("password", password));
        return (User) criteria.unique();
    }
}

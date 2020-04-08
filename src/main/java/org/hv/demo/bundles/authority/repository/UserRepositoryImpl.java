package org.hv.demo.bundles.authority.repository;

import org.hv.biscuits.repository.AbstractCommonRepository;
import org.hv.biscuits.spine.model.User;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.criteria.Restrictions;
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

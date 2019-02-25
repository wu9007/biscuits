package org.hunter.skeleton.spine.repository.impl;

import org.hunter.pocket.criteria.Criteria;
import org.hunter.pocket.criteria.Restrictions;
import org.hunter.skeleton.repository.AbstractRepository;
import org.hunter.skeleton.spine.model.User;
import org.hunter.skeleton.spine.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author wujianchuan 2019/1/30
 */
@Repository
public class UserRepositoryImpl extends AbstractRepository implements UserRepository {

    @Override
    public int saveUser(User user) {
        user.setLastPasswordResetDate(new Date());
        user.setLastRoleModifyDate(new Date());
        user.setState(true);
        return this.session.save(user);
    }

    @Override
    public User findByAvatar(String avatar) {
        Criteria criteria = this.session.creatCriteria(User.class);
        criteria.add(Restrictions.equ("avatar", avatar));
        return (User) criteria.unique(true);
    }

    @Override
    public User findByAvatarAndPassword(String avatar, String password) {
        Criteria criteria = this.session.creatCriteria(User.class);
        criteria.add(Restrictions.and(Restrictions.equ("avatar", avatar), Restrictions.equ("password", password)));
        return (User) criteria.unique(true);
    }
}

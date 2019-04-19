package org.hunter.skeleton.spine.model.repository.impl;

import org.hunter.pocket.criteria.Criteria;
import org.hunter.pocket.criteria.Restrictions;
import org.hunter.skeleton.annotation.Track;
import org.hunter.skeleton.constant.OperateEnum;
import org.hunter.skeleton.repository.AbstractRepository;
import org.hunter.skeleton.spine.model.User;
import org.hunter.skeleton.spine.model.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author wujianchuan 2019/1/30
 */
@Repository
public class UserRepositoryImpl extends AbstractRepository implements UserRepository {

    @Override
    public User findOne(String uuid) {
        return (User) this.getSession().findOne(User.class, uuid);
    }

    @Override
    @Track(data = "#user", operator = "#avatar", operate = OperateEnum.ADD)
    public int save(User user, String avatar) {
        user.setLastPasswordResetDate(new Date());
        user.setLastRoleModifyDate(new Date());
        user.setEnable(true);
        return this.getSession().save(user);
    }

    @Override
    public User findByAvatar(String avatar) {
        Criteria criteria = this.getSession().createCriteria(User.class);
        criteria.add(Restrictions.equ("avatar", avatar));
        return (User) criteria.unique(true);
    }

    @Override
    public User findByAvatarAndPassword(String avatar, String password) {
        Criteria criteria = this.getSession().createCriteria(User.class);
        criteria.add(Restrictions.and(Restrictions.equ("avatar", avatar), Restrictions.equ("password", password)));
        return (User) criteria.unique(true);
    }

    @Override
    public boolean canPass(String userCode, String serverId, String bundleId, String actionId) {
        String sql = "SELECT  " +
                "    COUNT( 0 )   " +
                "FROM  " +
                "    TBL_MAPPER T1  " +
                "    LEFT JOIN tbl_auth_mapper T2 ON T1.UUID = T2.mapper_uuid  " +
                "    LEFT JOIN tbl_authority T3 ON T2.auth_uuid = T3.UUID  " +
                "    LEFT JOIN tbl_role_auth T4 ON T3.UUID = T4.auth_uuid  " +
                "    LEFT JOIN TBL_ROLE T5 ON T4.role_uuid = T5.UUID  " +
                "    LEFT JOIN tbl_bundle T6 ON T1.bundle_uuid = T6.UUID  " +
                "    LEFT JOIN tbl_user_role T7 ON T5.UUID = T7.ROLE_UUID  " +
                "    LEFT JOIN TBL_USER T8 ON T7.USER_UUID = T8.UUID   " +
                "WHERE  " +
                "    ( T1.server_id = :SERVER_ID AND T1.bundle_id = :BUNDLE_ID AND t1.action_id = :ACTION_ID )   " +
                "    AND ( t8.CODE = :USER_CODE OR T6.with_auth = 0 )";
        return this.getSession().createSQLQuery(sql)
                .setParameter("SERVER_ID", serverId)
                .setParameter("BUNDLE_ID", bundleId)
                .setParameter("ACTION_ID", actionId)
                .setParameter("USER_CODE", userCode)
                .unique() != null;
    }
}

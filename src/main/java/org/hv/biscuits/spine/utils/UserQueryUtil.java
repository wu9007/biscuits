package org.hv.biscuits.spine.utils;

import org.hv.biscuits.annotation.Service;
import org.hv.biscuits.service.AbstractService;
import org.hv.biscuits.spine.model.Actor;
import org.hv.biscuits.spine.model.User;
import org.hv.pocket.query.SQLQuery;

import java.sql.SQLException;
import java.util.List;

/**
 * @author leyan95
 */
@Service(session = "biscuits")
public class UserQueryUtil extends AbstractService {

    /**
     * 获取指定分工下的人员
     *
     * @param actor 分工实例
     * @return 人员集合
     * @throws SQLException e
     */
    public List<User> getUserByActor(Actor actor) throws SQLException {
        String sql = "SELECT DISTINCT " +
                "    T5.AVATAR AS avatar, " +
                "    T5.STAFF_ID AS staffId, " +
                "    T5.NAME AS NAME, " +
                "    T5.SPELL AS spell, " +
                "    T5.PHONE AS phone, " +
                "    T5.DEPT_UUID AS departmentUuid, " +
                "    T5.MEMO AS memo, " +
                "    T5.ENABLE AS ENABLE, " +
                "    T5.SORT AS sort, " +
                "    T5.LAST_PASSWORD_RESET_DATE AS lastPasswordResetDate, " +
                "    T5.LAST_ROLE_MODIFY_DATE AS lastRoleModifyDate, " +
                "    T5.IS_MANAGER AS superAdmin  " +
                "FROM " +
                "    T_ACTOR T1 " +
                "    LEFT JOIN T_POST_ACTOR T2 ON T1.UUID = T2.ACTOR_UUID " +
                "    LEFT JOIN T_POST T3 ON T2.POST_UUID = T3.UUID " +
                "    LEFT JOIN T_POST_USER T4 ON T3.UUID = T4.POST_UUID " +
                "    LEFT JOIN T_USER T5 ON T4.USER_UUID = T5.UUID " +
                "    WHERE T1.SERVICE_ID = :SERVICE_ID AND T1.BUNDLE_ID = :BUNDLE_ID AND T1.ACTOR_ID = :ACTOR_ID ";
        SQLQuery sqlQuery = this.getSession().createSQLQuery(sql, User.class)
                .setParameter("SERVICE_ID", actor.getServiceId())
                .setParameter("BUNDLE_ID", actor.getBundleId())
                .setParameter("ACTOR_ID", actor.getActorId());
        return sqlQuery.list();
    }
}

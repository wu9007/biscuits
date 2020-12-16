package org.hv.biscuits.spine.utils;

import org.hv.biscuits.annotation.Affairs;
import org.hv.biscuits.annotation.Service;
import org.hv.biscuits.service.AbstractService;
import org.hv.biscuits.spine.model.Department;
import org.hv.biscuits.spine.model.Post;
import org.hv.biscuits.spine.model.User;
import org.hv.pocket.criteria.Restrictions;
import org.hv.pocket.query.SQLQuery;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author leyan95
 */
@Service(session = "biscuits")
public class UserQueryUtil extends AbstractService {
    @Value("${spring.application.name}")
    private String applicationName;
    private String serviceId;

    @PostConstruct
    public void init() {
        serviceId = applicationName.replaceAll("\\d+", "");
    }

    /**
     * 获取指定分工和部门下的人员
     *
     * @param bundleId       功能点
     * @param actorId        分工
     * @param departmentUuid 部门标识
     * @return 人员集合
     * @throws SQLException e
     */
    @Affairs(on = false)
    public List<User> getUserByActorAndDepartment(String bundleId, String actorId, String departmentUuid) throws SQLException {
        Department department = null;
        if (departmentUuid != null) {
            department = this.getSession().findOne(Department.class, departmentUuid);
            if (department == null) {
                throw new IllegalArgumentException(String.format("找不到数据标识为 %s 的部门数据", departmentUuid));
            }
        }
        String sql = "SELECT DISTINCT " +
                "    T5.UUID AS uuid, " +
                "    T5.AVATAR AS avatar, " +
                "    T5.STAFF_ID AS staffId, " +
                "    T5.NAME AS name, " +
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
                "    INNER JOIN T_POST_ACTOR T2 ON T1.UUID = T2.ACTOR_UUID " +
                "    INNER JOIN T_POST T3 ON T2.POST_UUID = T3.UUID " +
                "    INNER JOIN T_POST_USER T4 ON T3.UUID = T4.POST_UUID " +
                "    INNER JOIN T_USER T5 ON T4.USER_UUID = T5.UUID " +
                "WHERE T1.SERVICE_ID = :SERVICE_ID " +
                "    AND T1.BUNDLE_ID = :BUNDLE_ID " +
                "    AND T1.ACTOR_ID = :ACTOR_ID ";
        if (departmentUuid != null) {
            sql += " AND (T5.DEPT_UUID = :DEPT_UUID OR T5.DEPT_UUID = :DEPT_PARENT_UUID)";
        }
        sql += "ORDER BY T5.SORT ";
        SQLQuery sqlQuery = this.getSession().createSQLQuery(sql, User.class)
                .setParameter("SERVICE_ID", serviceId)
                .setParameter("BUNDLE_ID", bundleId)
                .setParameter("ACTOR_ID", actorId);
        if (departmentUuid != null) {
            sqlQuery.setParameter("DEPT_UUID", departmentUuid).setParameter("DEPT_PARENT_UUID", department.getParentUuid());
        }
        return sqlQuery.list();
    }

    /**
     * 获取指定分工下的人员
     *
     * @param bundleId 功能点
     * @param actorId  分工
     * @return 人员集合
     * @throws SQLException e
     */
    @Affairs(on = false)
    public List<User> getUserByActor(String bundleId, String actorId) throws SQLException {
        return this.getUserByActorAndDepartment(bundleId, actorId, null);
    }

    /**
     * 获取人员拥有的岗位
     *
     * @param avatar 人员
     * @return 岗位集合
     * @throws SQLException e
     */
    @Affairs(on = false)
    public List<Post> getPostByUserAvatar(String avatar) throws SQLException {
        String sql = "SELECT " +
                "    T3.UUID AS uuid, " +
                "    T3.SERVICE_ID AS serviceId, " +
                "    T3.NAME AS name, " +
                "    T3.SPELL AS spell, " +
                "    T3.SORT AS sort, " +
                "    T3.ENABLE AS enable, " +
                "    T3.MEMO AS memo " +
                "FROM " +
                "    T_USER T1 " +
                "    INNER JOIN T_POST_USER T2 ON T1.UUID = T2.USER_UUID " +
                "    INNER JOIN T_POST T3 ON T2.POST_UUID = T3.UUID  " +
                "WHERE " +
                "    T3.UUID IS NOT NULL  " +
                "    AND T1.AVATAR = :AVATAR " +
                "ORDER BY T3.SORT ";
        SQLQuery query = this.getSession().createSQLQuery(sql, Post.class)
                .setParameter("AVATAR", avatar);
        return query.list();
    }

    /**
     * 获取所有人员
     *
     * @return 人员集合
     */
    @Affairs(on = false)
    public List<User> getAllUser() {
        return this.getSession().list(User.class, false);
    }

    /**
     * 获取部门下的所有人员
     *
     * @return 人员集合
     */
    @Affairs(on = false)
    public List<User> getUserByDeptUuid(String deptUuid) {
        List<User> users = new LinkedList<>();
        this.appendUser(users, deptUuid);
        return users;
    }

    private void appendUser(List<User> users, String deptUuid) {
        users.addAll(this.getSession().createCriteria(User.class).add(Restrictions.equ("departmentUuid", deptUuid)).list(false));
        List<Department> departments = this.getSession().createCriteria(Department.class).add(Restrictions.equ("parentUuid", deptUuid)).list(false);
        if (!departments.isEmpty()) {
            for (Department item : departments) {
                this.appendUser(users, item.getUuid());
            }
        }
    }
}

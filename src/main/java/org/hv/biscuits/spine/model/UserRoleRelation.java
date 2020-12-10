package org.hv.biscuits.spine.model;

import org.hv.biscuits.spine.AbstractBisEntity;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.annotation.ManyToOne;

/**
 * @author leyan95 2019/1/30
 */
@Entity(table = "T_USER_ROLE", businessName = "用户和角色的关联")
public class UserRoleRelation extends AbstractBisEntity {
    private static final long serialVersionUID = 509835395399250891L;
    @ManyToOne(columnName = "USER_UUID", clazz = User.class, upBridgeField = "uuid")
    private String userUuid;
    @ManyToOne(columnName = "ROLE_UUID", clazz = Role.class, upBridgeField = "uuid")
    private String roleUuid;

    public UserRoleRelation() {
    }

    public UserRoleRelation(String userUuid, String roleUuid) {
        this.userUuid = userUuid;
        this.roleUuid = roleUuid;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getRoleUuid() {
        return roleUuid;
    }

    public void setRoleUuid(String roleUuid) {
        this.roleUuid = roleUuid;
    }
}

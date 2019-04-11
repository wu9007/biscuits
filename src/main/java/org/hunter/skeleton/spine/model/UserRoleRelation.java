package org.hunter.skeleton.spine.model;

import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.annotation.ManyToOne;
import org.hunter.pocket.model.BaseEntity;

/**
 * @author wujianchuan 2019/1/30
 */
@Entity(table = "TBL_USER_ROLE", tableId = 107)
public class UserRoleRelation extends BaseEntity {
    private static final long serialVersionUID = 509835395399250891L;
    @ManyToOne(name = "USER_UUID")
    private String userUuid;
    @ManyToOne(name = "ROLE_UUID")
    private String roleUuid;

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

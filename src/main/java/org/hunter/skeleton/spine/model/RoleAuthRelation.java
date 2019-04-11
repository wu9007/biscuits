package org.hunter.skeleton.spine.model;

import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.annotation.ManyToOne;
import org.hunter.pocket.model.BaseEntity;

/**
 * @author wujianchuan 2019/2/11
 */
@Entity(table = "TBL_ROLE_AUTH", tableId = 105)
public class RoleAuthRelation extends BaseEntity {
    private static final long serialVersionUID = -3881819865492714900L;
    @ManyToOne(name = "ROLE_UUID")
    private String roleUuid;
    @ManyToOne(name = "AUTH_UUID")
    private String authUuid;

    public RoleAuthRelation() {
    }

    public String getRoleUuid() {
        return roleUuid;
    }

    public void setRoleUuid(String roleUuid) {
        this.roleUuid = roleUuid;
    }

    public String getAuthUuid() {
        return authUuid;
    }

    public void setAuthUuid(String authUuid) {
        this.authUuid = authUuid;
    }
}

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
    @ManyToOne(columnName = "ROLE_UUID", clazz = Role.class, upBridgeField = "uuid")
    private String roleUuid;
    @ManyToOne(columnName = "AUTH_UUID", clazz = Authority.class, upBridgeField = "uuid")
    private String authUuid;

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

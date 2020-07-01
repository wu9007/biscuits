package org.hv.biscuits.spine.model;

import org.hv.biscuits.spine.AbstractBisEntity;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.annotation.ManyToOne;

/**
 * @author leyan95 2019/2/11
 */
@Entity(table = "T_ROLE_AUTH")
public class RoleAuthRelation extends AbstractBisEntity {
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

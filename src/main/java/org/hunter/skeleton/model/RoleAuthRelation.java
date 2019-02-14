package org.hunter.skeleton.model;

import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.annotation.ManyToOne;
import org.hunter.pocket.model.BaseEntity;

/**
 * @author wujianchuan 2019/2/11
 */
@Entity(table = "TBL_ROLE_AUTH", tableId = 105)
public class RoleAuthRelation extends BaseEntity {
    @ManyToOne(name = "ROLE_UUID")
    private Long roleUuid;
    @ManyToOne(name = "AUTH_UUID")
    private Long authUuid;

    public RoleAuthRelation() {
    }

    public Long getRoleUuid() {
        return roleUuid;
    }

    public void setRoleUuid(Long roleUuid) {
        this.roleUuid = roleUuid;
    }

    public Long getAuthUuid() {
        return authUuid;
    }

    public void setAuthUuid(Long authUuid) {
        this.authUuid = authUuid;
    }
}

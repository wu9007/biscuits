package org.hunter.skeleton.spine.model;

import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.model.BaseEntity;

/**
 * @author wujianchuan 2019/2/3
 */
@Entity(table = "TBL_AUTH_MAPPER", tableId = 101, history = false)
public class AuthMapperRelation extends BaseEntity {
    private static final long serialVersionUID = 3635488973894838870L;
    @Column(name = "SERVER_ID")
    private String serverId;
    @Column(name = "AUTH_UUID")
    private Long authUuid;
    @Column(name = "MAPPER_UUID")
    private Long mapperUuid;

    public AuthMapperRelation() {
    }

    public AuthMapperRelation(String serverId, Long authUuid, Long mapperUuid) {
        this.serverId = serverId;
        this.authUuid = authUuid;
        this.mapperUuid = mapperUuid;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Long getAuthUuid() {
        return authUuid;
    }

    public void setAuthUuid(Long authUuid) {
        this.authUuid = authUuid;
    }

    public Long getMapperUuid() {
        return mapperUuid;
    }

    public void setMapperUuid(Long mapperUuid) {
        this.mapperUuid = mapperUuid;
    }
}

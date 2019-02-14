package org.hunter.skeleton.model;

import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.model.BaseEntity;

/**
 * @author wujianchuan 2019/2/3
 */
@Entity(table = "TBL_AUTH_MAPPER", tableId = 101)
public class AuthMapperRelation extends BaseEntity {
    @Column(name = "SERVER_NAME")
    private String serverName;
    @Column(name = "AUTH_UUID")
    private Long authUuid;
    @Column(name = "MAPPER_UUID")
    private Long mapperUuid;

    public AuthMapperRelation() {
    }

    public AuthMapperRelation(String serverName, Long authUuid, Long mapperUuid) {
        this.serverName = serverName;
        this.authUuid = authUuid;
        this.mapperUuid = mapperUuid;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
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

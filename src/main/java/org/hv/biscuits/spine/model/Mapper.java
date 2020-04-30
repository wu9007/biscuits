package org.hv.biscuits.spine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.annotation.ManyToOne;
import org.hv.pocket.model.BaseEntity;

/**
 * 路径映射实体类
 *
 * @author wujianchuan 2019/2/3
 */
@Entity(table = "T_MAPPER", tableId = 103)
public class Mapper extends BaseEntity {
    private static final long serialVersionUID = -2091601211891427181L;
    @Column(name = "REQUEST_METHOD", businessName = "请求方式")
    private String requestMethod;
    @ManyToOne(columnName = "BUNDLE_UUID", clazz = Bundle.class, upBridgeField = "uuid")
    private String bundleUuid;
    @Column(name = "SERVER_ID")
    private String serverId;
    @Column(name = "BUNDLE_ID")
    private String bundleId;
    @Column(name = "ACTION_ID", businessName = "映射")
    private String actionId;
    @Column
    private String authId;

    public Mapper() {
    }

    public Mapper(String serverId, String requestMethod, String bundleId, String actionId, String authId) {
        this.serverId = serverId;
        this.requestMethod = requestMethod;
        this.bundleId = bundleId;
        this.actionId = actionId;
        this.authId = authId;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getBundleUuid() {
        return bundleUuid;
    }

    public void setBundleUuid(String bundleUuid) {
        this.bundleUuid = bundleUuid;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    @JsonIgnore
    public String getPath() {
        return "/" + this.getServerId() + "/" + this.getBundleId() + this.getActionId();
    }
}

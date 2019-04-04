package org.hunter.skeleton.spine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.annotation.ManyToOne;
import org.hunter.pocket.model.BaseEntity;

/**
 * 路径映射实体类
 *
 * @author wujianchuan 2019/2/3
 */
@Entity(table = "TBL_MAPPER", tableId = 103, history = false)
public class Mapper extends BaseEntity {
    private static final long serialVersionUID = -2091601211891427181L;
    @Column(name = "REQUEST_METHOD", businessName = "请求方式")
    private String requestMethod;
    @ManyToOne(name = "BUNDLE_UUID")
    private Long bundleUuid;
    @Column(name = "SERVER_ID")
    private String serverId;
    @Column(name = "BUNDLE_ID")
    private String bundleId;
    @Column(name = "ACTION_ID", businessName = "映射")
    private String actionId;

    public Mapper() {
    }

    public Mapper(String serverId, String requestMethod, String bundleId, String actionId) {
        this.serverId = serverId;
        this.requestMethod = requestMethod;
        this.bundleId = bundleId;
        this.actionId = actionId;
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

    public Long getBundleUuid() {
        return bundleUuid;
    }

    public void setBundleUuid(Long bundleUuid) {
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

    @JsonIgnore
    public String getPath() {
        return "/" + this.getServerId() + "/" + this.getBundleId() + this.getActionId();
    }
}

package org.hunter.skeleton.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.model.BaseEntity;

/**
 * 路径映射实体类
 *
 * @author wujianchuan 2019/2/3
 */
@Entity(table = "TBL_MAPPER", tableId = 103)
public class Mapper extends BaseEntity {
    @Column(name = "SERVER_NAME", businessName = "服务名")
    private String serverName;
    @Column(name = "REQUEST_METHOD", businessName = "请求方式")
    private String requestMethod;
    @Column(name = "BUNDLE_ID", businessName = "绑定菜单")
    private String bundleId;
    @Column(name = "ACTION_ID", businessName = "映射")
    private String actionId;

    public Mapper() {
    }

    public Mapper(String serverName, String requestMethod,String bundleId, String actionId) {
        this.serverName = serverName;
        this.requestMethod = requestMethod;
        this.bundleId = bundleId;
        this.actionId = actionId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    @JsonIgnore
    public String getPath() {
        return "/" + this.getServerName() + "/" + this.getBundleId() + this.getActionId();
    }
}

package org.hv.biscuits.spine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hv.biscuits.spine.AbstractBisEntity;
import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.annotation.ManyToOne;

/**
 * 路径映射实体类
 *
 * @author leyan95 2019/2/3
 */
@Entity(table = "T_MAPPER", businessName = "接口")
public class Mapper extends AbstractBisEntity {
    private static final long serialVersionUID = -2091601211891427181L;
    @Column(name = "REQUEST_METHOD", businessName = "请求方式")
    private String requestMethod;
    @ManyToOne(columnName = "BUNDLE_UUID", clazz = Bundle.class, upBridgeField = "uuid")
    private String bundleUuid;
    @Column(name = "SERVICE_ID")
    private String serviceId;
    @Column(name = "BUNDLE_ID")
    private String bundleId;
    @Column(name = "ACTION_ID", businessName = "映射")
    private String actionId;
    @Column
    private String authId;

    public Mapper() {
    }

    public Mapper(String serviceId, String requestMethod, String bundleId, String actionId, String authId) {
        this.serviceId = serviceId;
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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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
        return "/" + this.getServiceId() + "/" + this.getBundleId() + this.getActionId();
    }
}

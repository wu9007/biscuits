package org.hv.biscuits.core.views;

import org.hv.biscuits.core.ActionHolder;

import java.io.Serializable;
import java.util.Map;

/**
 * @author wujianchuan 2020/7/9 10:55
 */
public class ActionHolderView implements Serializable {
    private static final long serialVersionUID = -4197111078997146757L;
    private String serviceId;
    private Map<String, BundleView> bundleViewMap;
    private Map<String, PermissionView> permissionViewMap;

    public ActionHolderView() {
    }

    public ActionHolderView(String serviceId, Map<String, BundleView> bundleViewMap, Map<String, PermissionView> permissionViewMap) {
        this.serviceId = serviceId;
        this.bundleViewMap = bundleViewMap;
        this.permissionViewMap = permissionViewMap;
    }

    public static ActionHolderView newInstance(ActionHolder actionHolder) {
        return new ActionHolderView(actionHolder.getOwnServiceId(), actionHolder.getActionMap(), actionHolder.getPermissionMap());
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Map<String, BundleView> getBundleViewMap() {
        return bundleViewMap;
    }

    public void setBundleViewMap(Map<String, BundleView> bundleViewMap) {
        this.bundleViewMap = bundleViewMap;
    }

    public Map<String, PermissionView> getPermissionViewMap() {
        return permissionViewMap;
    }

    public void setPermissionViewMap(Map<String, PermissionView> permissionViewMap) {
        this.permissionViewMap = permissionViewMap;
    }
}

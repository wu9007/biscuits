package org.hv.biscuits.core.views;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leyan95
 */
public class BundleView implements Serializable {
    private static final long serialVersionUID = 8327647556636949498L;
    private String bundleId;
    private String bundleName;
    private boolean withAuth;
    private String targetClient;
    private Map<String, ActionView> actionViews;

    public BundleView() {
    }

    private BundleView(String bundleId, String bundleName, boolean withAuth, String targetClient) {
        this.bundleId = bundleId;
        this.bundleName = bundleName;
        this.withAuth = withAuth;
        this.targetClient = targetClient;
        this.actionViews = new HashMap<>();
    }

    public static BundleView newInstance(String bundleId, String bundleName, boolean withAuth, String targetClient) {
        return new BundleView(bundleId, bundleName, withAuth, targetClient);
    }

    public void appendActionView(ActionView actionView) {
        if (this.actionViews.putIfAbsent(actionView.getActionId(), actionView) != null) {
            throw new IllegalArgumentException(String.format("存在拥有相同映射的Action %s/%s", this.getBundleId(), actionView.getActionId()));
        }
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getBundleName() {
        return bundleName;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    public boolean isWithAuth() {
        return withAuth;
    }

    public void setWithAuth(boolean withAuth) {
        this.withAuth = withAuth;
    }

    public String getTargetClient() {
        return targetClient;
    }

    public void setTargetClient(String targetClient) {
        this.targetClient = targetClient;
    }

    public Map<String, ActionView> getActionViews() {
        return actionViews;
    }

    public void setActionViews(Map<String, ActionView> actionViews) {
        this.actionViews = actionViews;
    }
}

package org.hv.biscuits.core.views;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wujianchuan
 */
public class BundleView {
    private final String bundleId;
    private final String bundleName;
    private final boolean withAuth;
    private final Map<String, ActionView> actionViews;

    private BundleView(String bundleId, String bundleName, boolean withAuth) {
        this.bundleId = bundleId;
        this.bundleName = bundleName;
        this.withAuth = withAuth;
        this.actionViews = new HashMap<>();
    }

    public static BundleView newInstance(String bundleId, String bundleName, boolean withAuth) {
        return new BundleView(bundleId, bundleName, withAuth);
    }

    public void appendActionView(ActionView actionView) {
        if (this.actionViews.putIfAbsent(actionView.getActionId(), actionView) != null) {
            throw new IllegalArgumentException(String.format("存在拥有相同映射的Action %s/%s", this.getBundleId(), actionView.getActionId()));
        }
    }

    public String getBundleId() {
        return bundleId;
    }

    public String getBundleName() {
        return bundleName;
    }

    public boolean isWithAuth() {
        return withAuth;
    }

    public Map<String, ActionView> getActionViews() {
        return actionViews;
    }
}

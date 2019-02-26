package org.hunter.skeleton.bundle;

import org.hunter.skeleton.spine.model.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
public class BundleFactory {

    private static final Map<String, String> BUNDLE_NAME_POOL = new HashMap<>(6);
    private static final Map<String, Bundle> BUNDLE_POOL = new HashMap<>(6);

    static void register(String serverId, String bundleId, String name) {
        if (!BUNDLE_NAME_POOL.containsKey(serverId + "_" + bundleId)) {
            BUNDLE_NAME_POOL.putIfAbsent(serverId + "_" + bundleId, name);
        }
    }

    public static void registerBundle(Bundle bundle) {
        if (bundle != null && !BUNDLE_POOL.containsKey(bundle.getServerId() + "_" + bundle.getBundleId())) {
            BUNDLE_POOL.putIfAbsent(bundle.getServerId() + "_" + bundle.getServerId(), bundle);
        }
    }

    public static String getBundleName(String serverId, String bundleId) {
        return BUNDLE_NAME_POOL.getOrDefault(serverId + "_" + bundleId, null);
    }

    public static Bundle getBundle(String serverId, String bundleId) {
        return BUNDLE_POOL.getOrDefault(serverId + "_" + bundleId, null);
    }

}

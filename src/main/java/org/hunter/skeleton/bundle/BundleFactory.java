package org.hunter.skeleton.bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
public class BundleFactory {

    private static final Map<String, String> BUNDLE_NAME_POOL = new HashMap<>(6);

    protected static void register(String bundleId, String name) {
        if (!BUNDLE_NAME_POOL.containsKey(bundleId)) {
            BUNDLE_NAME_POOL.putIfAbsent(bundleId, name);
        }
    }

    public static String getBundleName(String bundleId) {
        return BUNDLE_NAME_POOL.getOrDefault(bundleId, null);
    }

}

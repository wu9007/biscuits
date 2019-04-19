package org.hunter.skeleton.permission;

import org.hunter.skeleton.spine.model.Authority;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wujianchuan 2019/2/2
 */
public class PermissionFactory {

    private static Map<String, Authority> permissionMap = new ConcurrentHashMap<>(10);

    public static void init(Map<String, Authority> permissionMap) {
        PermissionFactory.permissionMap = permissionMap;
    }

    static void register(String serverId, String id, String name, String common) {
        if (!permissionMap.containsKey(id)) {
            Authority authority = new Authority(serverId, id, name, common);
            permissionMap.putIfAbsent(id, authority);
        }
    }

    public static Authority get(String id) {
        return permissionMap.getOrDefault(id, null);
    }
}

package org.hunter.skeleton.permission;

import org.hunter.pocket.session.Session;
import org.hunter.skeleton.spine.model.Authority;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wujianchuan 2019/2/2
 */
public class PermissionFactory {

    private static Map<String, Authority> permissionMap = new ConcurrentHashMap<>(10);
    private static final Map<String, Boolean> necessaryKeyMap = new ConcurrentHashMap<>(10);

    public static void init(Map<String, Authority> permissionMap) {
        PermissionFactory.permissionMap = permissionMap;
    }

    static void register(Session session, String serverId, String id, String name, String common) {
        necessaryKeyMap.putIfAbsent(serverId + "_" + id, true);
        if (!permissionMap.containsKey(serverId + "_" + id)) {
            Authority authority = new Authority(serverId, id, name, common);
            session.save(authority);
            permissionMap.putIfAbsent(serverId + "_" + id, authority);
        }
    }

    public static void removeUselessPermission(Session session) {
        permissionMap.forEach((k, v) -> {
            if (!necessaryKeyMap.containsKey(k)) {
                session.delete(v);
            }
        });
    }

    public static Authority get(String serverId, String id) {
        return permissionMap.getOrDefault(serverId + "_" + id, null);
    }
}

package org.hunter.skeleton.permission;

import org.hunter.pocket.session.Session;
import org.hunter.pocket.session.SessionFactory;
import org.hunter.skeleton.model.Authority;

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

    public static void register(String serverName, String id, String name, String common) {
        if (!permissionMap.containsKey(serverName + id)) {
            try {
                Authority authority = new Authority(serverName, id, name, common);
                Session session = SessionFactory.getSession("skeleton");
                session.open();
                session.save(authority);
                session.close();
                permissionMap.put(serverName + id, authority);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Authority get(String serverName, String id) {
        return permissionMap.getOrDefault(serverName + id, null);
    }
}

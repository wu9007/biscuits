package org.hunter.skeleton.permission;

import org.hunter.pocket.criteria.Restrictions;
import org.hunter.pocket.session.Session;
import org.hunter.skeleton.spine.model.AuthMapperRelation;
import org.hunter.skeleton.spine.model.Authority;
import org.hunter.skeleton.spine.model.RoleAuthRelation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wujianchuan 2019/2/2
 */
public class PermissionFactory {

    private static Map<String, Authority> permissionMap = new ConcurrentHashMap<>(10);
    private static final Map<String, Boolean> NECESSARY_KEY_MAP = new ConcurrentHashMap<>(10);

    public static void init(Map<String, Authority> permissionMap) {
        PermissionFactory.permissionMap = permissionMap;
    }

    static void register(Session session, String serverId, String id, String name, String common) {
        NECESSARY_KEY_MAP.putIfAbsent(serverId + "_" + id, true);
        if (!permissionMap.containsKey(serverId + "_" + id)) {
            Authority authority = new Authority(serverId, id, name, common);
            session.save(authority);
            permissionMap.putIfAbsent(serverId + "_" + id, authority);
        }
    }

    public static void removeUselessPermission(Session session) {
        Map<String, Authority> copyMap = new HashMap<>(permissionMap);

        copyMap.forEach((k, authority) -> {
            if (!NECESSARY_KEY_MAP.containsKey(k)) {
                long authMapperRow = session.createCriteria(AuthMapperRelation.class)
                        .add(Restrictions.equ("authUuid", authority.getUuid()))
                        .delete();
                System.out.println(authMapperRow);
                long roleAuthRow = session.createCriteria(RoleAuthRelation.class)
                        .add(Restrictions.equ("authUuid", authority.getUuid()))
                        .delete();
                System.out.println(roleAuthRow);
                session.delete(authority);
                permissionMap.remove(k);
            }
        });
    }

    public static Authority get(String serverId, String id) {
        return permissionMap.getOrDefault(serverId + "_" + id, null);
    }
}

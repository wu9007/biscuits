package org.hunter.skeleton.permission;


import org.hunter.pocket.session.Session;

/**
 * @author wujianchuan 2019/2/25
 */
public abstract class AbstractPermission implements Permission {
    private String serverId;
    private Session session;

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void register(String id, String name, String common) {
        PermissionFactory.register(this.session, serverId, id, name, common);
    }
}

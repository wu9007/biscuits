package org.hunter.skeleton.permission;


/**
 * @author wujianchuan 2019/2/25
 */
public abstract class AbstractPermission implements Permission {
    private String serverId;

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public void register(String id, String name, String common) {
        PermissionFactory.register(serverId, id, name, common);
    }
}

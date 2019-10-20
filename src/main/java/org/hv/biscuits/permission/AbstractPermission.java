package org.hv.biscuits.permission;


/**
 * @author wujianchuan 2019/2/25
 */
public abstract class AbstractPermission implements Permission {
    private String serverId;

    public Permission setServerId(String serverId) {
        this.serverId = serverId;
        return this;
    }

    protected void register(String bundleId, String authId, String name, String common) {
        PermissionFactory.register(serverId, bundleId, authId, name, common);
    }
}

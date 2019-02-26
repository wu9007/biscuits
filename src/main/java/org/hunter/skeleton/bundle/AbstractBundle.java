package org.hunter.skeleton.bundle;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
public abstract class AbstractBundle implements Bundle {
    private String serverId;

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public void register(String id, String name) {
        BundleFactory.register(serverId, id, name);
    }
}

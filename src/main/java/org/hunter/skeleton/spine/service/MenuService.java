package org.hunter.skeleton.spine.service;

import org.hunter.skeleton.spine.model.Bundle;

import java.util.List;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
public interface MenuService {

    /**
     * get accessible services.
     *
     * @param roles roles.
     * @return get accessible services.
     */
    String getServers(String roles);

    /**
     * get accessible bundles.
     *
     * @param serverId serverId.
     * @param roles    roles.
     * @return get accessible bundles.
     */
    List<Bundle> getBundles(String serverId, String roles);
}

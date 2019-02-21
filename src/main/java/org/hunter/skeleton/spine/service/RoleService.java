package org.hunter.skeleton.spine.service;

import org.hunter.skeleton.spine.model.Role;
import org.hunter.skeleton.spine.model.User;

import java.util.List;

/**
 * @author wujianchuan 2019/2/21
 */
public interface RoleService {

    /**
     * load role by role uuid.
     *
     * @param uuid role uuid.
     * @return role.
     */
    Role loadByUuId(long uuid);

    /**
     * load role by role id,
     *
     * @param id role id,
     * @return role.
     */
    Role loadById(String id);

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
    String getBundles(String serverId, String roles);

    /**
     * load user roles.
     *
     * @param user user.
     * @return roles.
     */
    List<Role> loadByUser(User user);


}

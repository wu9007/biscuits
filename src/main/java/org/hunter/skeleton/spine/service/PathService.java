package org.hunter.skeleton.spine.service;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
public interface PathService {

    /**
     * load accessible path by role ids.
     *
     * @param roleIds role ids (String)
     * @return all accessible path (String).
     */
    String loadAccessiblePathByRoleIds(String roleIds);

    String loadNoAuthPath();
}

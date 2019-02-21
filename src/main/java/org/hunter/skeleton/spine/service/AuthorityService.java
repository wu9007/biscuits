package org.hunter.skeleton.spine.service;

import org.hunter.skeleton.spine.model.Authority;
import org.hunter.skeleton.spine.model.Role;

import java.util.List;

/**
 * @author wujianchuan 2019/2/21
 */
public interface AuthorityService {

    List<Authority> loadByRole(Role role);

    List<Authority> loadByServerRole(String serverId, Role role);
}

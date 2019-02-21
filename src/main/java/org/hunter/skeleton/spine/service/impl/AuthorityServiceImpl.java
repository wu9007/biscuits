package org.hunter.skeleton.spine.service.impl;

import org.hunter.skeleton.annotation.Service;
import org.hunter.skeleton.service.AbstractService;
import org.hunter.skeleton.spine.model.Authority;
import org.hunter.skeleton.spine.model.Role;
import org.hunter.skeleton.spine.repository.AuthorityRepository;
import org.hunter.skeleton.spine.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wujianchuan 2019/2/21
 */
@Service(session = "skeleton")
public class AuthorityServiceImpl extends AbstractService implements AuthorityService {
    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
        this.authorityRepository.setSession(this.sessionThreadLocal);
    }

    @Override
    public List<Authority> loadByRole(Role role) {
        return role.getRoleAuthRelationList().stream()
                .map(roleAuthRelation -> this.authorityRepository.findOne(roleAuthRelation.getAuthUuid()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Authority> loadByServerRole(String serverId, Role role) {
        return role.getRoleAuthRelationList().stream()
                .map(roleAuthRelation -> this.authorityRepository.findOne(roleAuthRelation.getAuthUuid()))
                .filter(authority -> authority != null && serverId.equals(authority.getServerId()))
                .collect(Collectors.toList());
    }
}

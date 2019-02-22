package org.hunter.skeleton.spine.service.impl;

import org.hunter.skeleton.annotation.Service;
import org.hunter.skeleton.service.AbstractService;
import org.hunter.skeleton.spine.model.Authority;
import org.hunter.skeleton.spine.model.Role;
import org.hunter.skeleton.spine.model.RoleAuthRelation;
import org.hunter.skeleton.spine.repository.AuthorityRepository;
import org.hunter.skeleton.spine.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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
        this.authorityRepository.setSession(this.session);
    }

    @Override
    public List<Authority> loadByRole(Role role) {
        List<Authority> authorities = new ArrayList<>();
        List<RoleAuthRelation> roleAuthRelations = role.getRoleAuthRelationList();
        if (roleAuthRelations != null && roleAuthRelations.size() > 0) {
            authorities = roleAuthRelations.stream()
                    .map(roleAuthRelation -> this.authorityRepository.findOne(roleAuthRelation.getAuthUuid()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return authorities;
    }

    @Override
    public List<Authority> loadByServerRole(String serverId, Role role) {
        List<Authority> authorities = new ArrayList<>();
        List<RoleAuthRelation> roleAuthRelations = role.getRoleAuthRelationList();
        if (roleAuthRelations != null && roleAuthRelations.size() > 0) {
            authorities = roleAuthRelations.stream()
                    .map(roleAuthRelation -> this.authorityRepository.findOne(roleAuthRelation.getAuthUuid()))
                    .filter(authority -> authority != null && serverId.equals(authority.getServerId()))
                    .collect(Collectors.toList());
        }
        return authorities;
    }
}

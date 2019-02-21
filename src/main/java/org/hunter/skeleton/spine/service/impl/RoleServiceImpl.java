package org.hunter.skeleton.spine.service.impl;

import org.hunter.skeleton.annotation.Service;
import org.hunter.skeleton.service.AbstractService;
import org.hunter.skeleton.spine.model.Authority;
import org.hunter.skeleton.spine.model.Mapper;
import org.hunter.skeleton.spine.model.Role;
import org.hunter.skeleton.spine.model.User;
import org.hunter.skeleton.spine.repository.RoleRepository;
import org.hunter.skeleton.spine.service.AuthorityService;
import org.hunter.skeleton.spine.service.MapperService;
import org.hunter.skeleton.spine.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wujianchuan 2019/2/21
 */
@Service(session = "skeleton")
public class RoleServiceImpl extends AbstractService implements RoleService {

    private final RoleRepository roleRepository;
    private final AuthorityService authorityService;
    private final MapperService mapperService;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, AuthorityService authorityService, MapperService mapperService) {
        this.roleRepository = roleRepository;
        this.authorityService = authorityService;
        this.mapperService = mapperService;
        this.roleRepository.setSession(this.sessionThreadLocal);
    }

    @Override
    public Role loadByUuId(long uuid) {
        return this.roleRepository.findOne(uuid);
    }

    @Override
    public Role loadById(String id) {
        return this.roleRepository.findById(id);
    }

    @Override
    public String getServers(String roleIds) {
        return Arrays.stream(roleIds.split(","))
                .map(this::loadById)
                .filter(role -> role.getRoleAuthRelationList() != null)
                .map(this.authorityService::loadByRole)
                .flatMap(Collection::stream)
                .map(Authority::getServerId)
                .distinct()
                .collect(Collectors.joining(","));
    }

    @Override
    public String getBundles(String serverId, String roleIds) {
        return Arrays.stream(roleIds.split(","))
                .map(this::loadById)
                .filter(role -> role.getRoleAuthRelationList() != null)
                .map(role -> this.authorityService.loadByServerRole(serverId, role))
                .flatMap(Collection::parallelStream)
                .filter(authority -> authority.getAuthMapperRelationList() != null)
                .map(this.mapperService::loadByAuthority)
                .flatMap(Collection::parallelStream)
                .map(Mapper::getBundleId)
                .distinct()
                .collect(Collectors.joining(","));

    }

    @Override
    public List<Role> loadByUser(User user) {
        return user.getUserRoleRelations().stream()
                .map(userRoleRelation -> this.loadByUuId(userRoleRelation.getRoleUuid()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}

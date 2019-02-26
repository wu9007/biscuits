package org.hunter.skeleton.spine.service.impl;

import org.hunter.skeleton.annotation.Service;
import org.hunter.skeleton.service.AbstractService;
import org.hunter.skeleton.spine.model.Authority;
import org.hunter.skeleton.spine.model.Bundle;
import org.hunter.skeleton.spine.model.Mapper;
import org.hunter.skeleton.spine.service.AuthorityService;
import org.hunter.skeleton.spine.service.BundleService;
import org.hunter.skeleton.spine.service.MapperService;
import org.hunter.skeleton.spine.service.MenuService;
import org.hunter.skeleton.spine.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
@Service(session = "skeleton")
public class MenuServiceImpl extends AbstractService implements MenuService {

    private final RoleService roleService;
    private final AuthorityService authorityService;
    private final MapperService mapperService;
    private final BundleService bundleService;

    @Autowired
    public MenuServiceImpl(RoleService roleService, AuthorityService authorityService, MapperService mapperService, BundleService bundleService) {
        this.roleService = roleService;
        this.authorityService = authorityService;
        this.mapperService = mapperService;
        this.bundleService = bundleService;
    }

    @Override
    public String getServers(String roleIds) {
        return Arrays.stream(roleIds.split(","))
                .map(this.roleService::loadById)
                .map(this.authorityService::loadByRole)
                .flatMap(Collection::stream)
                .map(Authority::getServerId)
                .distinct()
                .collect(Collectors.joining(","));
    }

    @Override
    public List<Bundle> getBundles(String serverId, String roleIds) {
        return Arrays.stream(roleIds.split(","))
                .map(this.roleService::loadById)
                .map(role -> this.authorityService.loadByServerRole(serverId, role))
                .flatMap(Collection::parallelStream)
                .map(this.mapperService::loadByAuthority)
                .flatMap(Collection::parallelStream)
                .map(Mapper::getBundleUuid)
                .distinct()
                .map(this.bundleService::findOne)
                .collect(Collectors.toList());

    }
}

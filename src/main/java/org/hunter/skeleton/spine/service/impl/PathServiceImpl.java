package org.hunter.skeleton.spine.service.impl;

import org.hunter.skeleton.annotation.Service;
import org.hunter.skeleton.service.AbstractService;
import org.hunter.skeleton.spine.model.Mapper;
import org.hunter.skeleton.spine.service.AuthorityService;
import org.hunter.skeleton.spine.service.BundleService;
import org.hunter.skeleton.spine.service.MapperService;
import org.hunter.skeleton.spine.service.PathService;
import org.hunter.skeleton.spine.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
@Service(session = "skeleton")
public class PathServiceImpl extends AbstractService implements PathService {
    private final AuthorityService authorityService;
    private final RoleService roleService;
    private final BundleService bundleService;
    private final MapperService mapperService;

    @Autowired
    public PathServiceImpl(AuthorityService authorityService, RoleService roleService, BundleService bundleService, MapperService mapperService) {
        this.authorityService = authorityService;
        this.roleService = roleService;
        this.bundleService = bundleService;
        this.mapperService = mapperService;
    }

    @Override
    public String loadAccessiblePathByRoleIds(String roleIds) {
        return Arrays.stream(roleIds.split(","))
                .map(this.roleService::loadById)
                .filter(Objects::nonNull)
                .map(this.authorityService::loadByRole)
                .flatMap(Collection::parallelStream)
                .map(this.mapperService::loadByAuthority)
                .flatMap(Collection::parallelStream)
                .map(Mapper::getPath)
                .collect(Collectors.joining(","));
    }

    @Override
    public String loadNoAuthPath() {
        return this.bundleService.getNoAuthBundle().stream()
                .map(bundle -> this.mapperService.loadByBundle(bundle.getUuid()))
                .flatMap(Collection::stream)
                .map(Mapper::getPath)
                .collect(Collectors.joining(","));
    }
}

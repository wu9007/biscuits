package org.hunter.skeleton.spine.service.impl;

import org.hunter.skeleton.annotation.Service;
import org.hunter.skeleton.service.AbstractService;
import org.hunter.skeleton.spine.model.Role;
import org.hunter.skeleton.spine.model.User;
import org.hunter.skeleton.spine.model.UserRoleRelation;
import org.hunter.skeleton.spine.repository.RoleRepository;
import org.hunter.skeleton.spine.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wujianchuan 2019/2/21
 */
@Service(session = "skeleton")
public class RoleServiceImpl extends AbstractService implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.roleRepository.setSession(this.session);
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
    public List<Role> loadByUser(User user) {
        List<Role> roles = new ArrayList<>();
        List<UserRoleRelation> userRoleRelations = user.getUserRoleRelations();
        if (userRoleRelations != null && userRoleRelations.size() > 0) {
            roles = userRoleRelations.stream()
                    .map(userRoleRelation -> this.loadByUuId(userRoleRelation.getRoleUuid()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return roles;
    }
}

package org.hunter.skeleton.spine.model;

import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.annotation.OneToMany;
import org.hunter.pocket.model.BaseEntity;
import org.hunter.skeleton.spine.model.repository.AuthorityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wujianchuan 2019/1/30
 */
@Entity(table = "TBL_ROLE", tableId = 104)
public class Role extends BaseEntity {
    private static final long serialVersionUID = -1762486150168555776L;
    @Column(name = "SPELL", businessName = "标识")
    private String id;
    @Column(name = "NAME", businessName = "名称")
    private String name;
    @Column(name = "SORT", businessName = "排序")
    private Double sort;
    @Column(name = "ENABLE", businessName = "是否可用")
    private Boolean enable;
    @Column(name = "MEMO", businessName = "备注")
    private String memo;
    @OneToMany(clazz = UserRoleRelation.class, name = "ROLE_UUID")
    private List<UserRoleRelation> userRoleRelations;
    @OneToMany(clazz = RoleAuthRelation.class, name = "ROLE_UUID")
    private List<RoleAuthRelation> roleAuthRelationList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSort() {
        return sort;
    }

    public void setSort(Double sort) {
        this.sort = sort;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<UserRoleRelation> getUserRoleRelations() {
        return userRoleRelations;
    }

    public void setUserRoleRelations(List<UserRoleRelation> userRoleRelations) {
        this.userRoleRelations = userRoleRelations;
    }

    public List<RoleAuthRelation> getRoleAuthRelationList() {
        return roleAuthRelationList;
    }

    public void setRoleAuthRelationList(List<RoleAuthRelation> roleAuthRelationList) {
        this.roleAuthRelationList = roleAuthRelationList;
    }

    public List<Authority> getAuths(AuthorityRepository authorityRepository) {
        List<Authority> authorities = new ArrayList<>();
        List<RoleAuthRelation> roleAuthRelations = this.getRoleAuthRelationList();
        if (roleAuthRelations != null && roleAuthRelations.size() > 0) {
            authorities = roleAuthRelations.stream()
                    .map(roleAuthRelation -> authorityRepository.findOne(roleAuthRelation.getAuthUuid()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return authorities;
    }
}

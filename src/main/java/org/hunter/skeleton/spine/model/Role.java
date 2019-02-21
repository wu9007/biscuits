package org.hunter.skeleton.spine.model;

import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.annotation.OneToMany;
import org.hunter.pocket.model.BaseEntity;

import java.util.List;

/**
 * @author wujianchuan 2019/1/30
 */
@Entity(table = "TBL_ROLE", tableId = 104)
public class Role extends BaseEntity {
    @Column(name = "ID")
    private String id;
    @Column(name = "NAME")
    private String name;
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
}

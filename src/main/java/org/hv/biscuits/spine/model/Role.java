package org.hv.biscuits.spine.model;

import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.annotation.OneToMany;
import org.hv.pocket.model.BaseEntity;

import java.util.List;

/**
 * @author wujianchuan 2019/1/30
 */
@Entity(table = "T_ROLE", tableId = 104)
public class Role extends BaseEntity {
    private static final long serialVersionUID = -1762486150168555776L;
    @Column(name = "SPELL", businessName = "标识")
    private String spell;
    @Column(name = "SERVER_ID", businessName = "服务标识")
    private String serverId;
    @Column(name = "NAME", businessName = "名称")
    private String name;
    @Column(name = "SORT", businessName = "排序")
    private Double sort;
    @Column(name = "ENABLE", businessName = "是否可用")
    private Boolean enable;
    @Column(name = "MEMO", businessName = "备注")
    private String memo;
    @OneToMany(clazz = UserRoleRelation.class, bridgeField = "roleUuid")
    private List<UserRoleRelation> userRoleRelations;
    @OneToMany(clazz = RoleAuthRelation.class, bridgeField = "roleUuid")
    private List<RoleAuthRelation> roleAuthRelationList;

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
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
}

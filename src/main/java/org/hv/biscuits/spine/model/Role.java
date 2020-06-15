package org.hv.biscuits.spine.model;

import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.annotation.Join;
import org.hv.pocket.annotation.OneToMany;
import org.hv.pocket.constant.JoinMethod;
import org.hv.pocket.model.BaseEntity;

import java.util.List;

/**
 * @author leyan95 2019/1/30
 */
@Entity(table = "T_ROLE", tableId = 104)
public class Role extends BaseEntity {
    private static final long serialVersionUID = -1762486150168555776L;
    @Column(businessName = "标识")
    private String spell;
    @Column(businessName = "服务标识")
    private String serviceId;
    @Column(businessName = "名称")
    private String name;
    @Column(businessName = "排序")
    private Double sort;
    @Column(businessName = "是否可用")
    private Boolean enable;
    @Column(businessName = "备注")
    private String memo;
    @Column
    private String departmentUuid;
    @Join(columnName = "DEPARTMENT_UUID", columnSurname = "DEPARTMENT_NAME", businessName = "部门", joinTable = "T_DEPARTMENT", joinTableSurname = "T1", joinMethod = JoinMethod.LEFT, bridgeColumn = "UUID", destinationColumn = "NAME")
    private String departmentName;
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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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

    public String getDepartmentUuid() {
        return departmentUuid;
    }

    public void setDepartmentUuid(String departmentUuid) {
        this.departmentUuid = departmentUuid;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

package org.hv.biscuit.spine.model;

import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.annotation.OneToMany;
import org.hv.pocket.model.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * @author wujianchuan 2019/1/30
 */
@Entity(table = "T_USER", tableId = 106, businessName = "人员")
public class User extends BaseEntity {
    private static final long serialVersionUID = 9034066443646846844L;
    @Column(name = "CODE", businessName = "昵称")
    private String avatar;
    @Column(name = "NAME", businessName = "姓名")
    private String name;
    @Column(name = "PHONE", businessName = "手机号")
    private String phone;
    @Column(name = "PASSWORD", businessName = "密码")
    private String password;
    @Column(name = "DEPT_UUID", businessName = "部门")
    private String departmentUuid;
    @Column(name = "MEMO", businessName = "备注")
    private String memo;
    @Column(name = "ENABLE", businessName = "状态")
    private Boolean enable;
    @Column(name = "SORT", businessName = "排序码")
    private Integer sort;
    @Column(name = "LAST_PASSWORD_RESET_DATE", businessName = "密码最后更新时间")
    private Date lastPasswordResetDate;
    /**
     * TODO 调整角色时需要将 token 置为过期，谢谢
     */
    @Column(name = "LAST_ROLE_MODIFY_DATE", businessName = "角色最后分配时间")
    private Date lastRoleModifyDate;

    @OneToMany(clazz = UserRoleRelation.class, bridgeField = "userUuid")
    private List<UserRoleRelation> userRoleRelations;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartmentUuid() {
        return departmentUuid;
    }

    public void setDepartmentUuid(String departmentUuid) {
        this.departmentUuid = departmentUuid;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public Date getLastRoleModifyDate() {
        return lastRoleModifyDate;
    }

    public void setLastRoleModifyDate(Date lastRoleModifyDate) {
        this.lastRoleModifyDate = lastRoleModifyDate;
    }

    public List<UserRoleRelation> getUserRoleRelations() {
        return userRoleRelations;
    }

    public void setUserRoleRelations(List<UserRoleRelation> userRoleRelations) {
        this.userRoleRelations = userRoleRelations;
    }
}

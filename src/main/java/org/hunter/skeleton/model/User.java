package org.hunter.skeleton.model;

import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.annotation.OneToMany;
import org.hunter.pocket.model.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * @author wujianchuan 2019/1/30
 */
@Entity(table = "TBL_USER", tableId = 106, businessName = "人员")
public class User extends BaseEntity {
    @Column(name = "AVATAR", businessName = "昵称")
    private String avatar;
    @Column(name = "PASSWORD", businessName = "密码")
    private String password;
    @Column(name = "NAME", businessName = "姓名")
    private String name;
    @Column(name = "DEPARTMENT_UUID", businessName = "部门")
    private Long departmentUuid;
    @OneToMany(clazz = UserRoleRelation.class, name = "USER_UUID")
    private List<UserRoleRelation> userRoleRelations;
    @Column(name = "LAST_PASSWORD_RESET_DATE", businessName = "密码最后更新时间")
    private Date lastPasswordResetDate;

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

    public Long getDepartmentUuid() {
        return departmentUuid;
    }

    public void setDepartmentUuid(Long departmentUuid) {
        this.departmentUuid = departmentUuid;
    }

    public List<UserRoleRelation> getUserRoleRelations() {
        return userRoleRelations;
    }

    public void setUserRoleRelations(List<UserRoleRelation> userRoleRelations) {
        this.userRoleRelations = userRoleRelations;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }
}

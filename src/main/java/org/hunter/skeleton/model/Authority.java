package org.hunter.skeleton.model;

import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.annotation.OneToMany;
import org.hunter.pocket.model.BaseEntity;

import java.util.List;

/**
 * @author wujianchuan 2019/1/30
 */
@Entity(table = "TBL_AUTHORITY", tableId = 102)
public class Authority extends BaseEntity {
    @Column(name = "SERVER_NAME")
    private String serverName;
    @Column(name = "ID")
    private String id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "COMMENT")
    private String comment;
    @OneToMany(clazz = AuthMapperRelation.class, name = "AUTH_UUID")
    private List<AuthMapperRelation> authMapperRelationList;
    @OneToMany(clazz = RoleAuthRelation.class, name = "AUTH_UUID")
    private List<RoleAuthRelation> roleAuthRelationList;

    public Authority() {
    }

    public Authority(String serverName, String id, String name, String comment) {
        this.serverName = serverName;
        this.id = id;
        this.name = name;
        this.comment = comment;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<AuthMapperRelation> getAuthMapperRelationList() {
        return authMapperRelationList;
    }

    public void setAuthMapperRelationList(List<AuthMapperRelation> authMapperRelationList) {
        this.authMapperRelationList = authMapperRelationList;
    }

    public List<RoleAuthRelation> getRoleAuthRelationList() {
        return roleAuthRelationList;
    }

    public void setRoleAuthRelationList(List<RoleAuthRelation> roleAuthRelationList) {
        this.roleAuthRelationList = roleAuthRelationList;
    }
}

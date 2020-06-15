package org.hv.biscuits.spine.model;

import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.annotation.OneToMany;
import org.hv.pocket.model.BaseEntity;

import java.util.List;

/**
 * @author leyan95 2019/1/30
 */
@Entity(table = "T_AUTHORITY", tableId = 102)
public class Authority extends BaseEntity {
    private static final long serialVersionUID = 8811730322368476299L;
    @Column(name = "SERVICE_ID")
    private String serviceId;
    @Column(name = "BUNDLE_ID")
    private String bundleId;
    @Column(name = "ID")
    private String id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "COMMENT")
    private String comment;
    @OneToMany(clazz = RoleAuthRelation.class, bridgeField = "authUuid")
    private List<RoleAuthRelation> roleAuthRelationList;

    public Authority() {
    }

    public Authority(String serviceId, String bundleId, String id, String name, String comment) {
        this.serviceId = serviceId;
        this.bundleId = bundleId;
        this.id = id;
        this.name = name;
        this.comment = comment;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
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

    public List<RoleAuthRelation> getRoleAuthRelationList() {
        return roleAuthRelationList;
    }

    public void setRoleAuthRelationList(List<RoleAuthRelation> roleAuthRelationList) {
        this.roleAuthRelationList = roleAuthRelationList;
    }
}

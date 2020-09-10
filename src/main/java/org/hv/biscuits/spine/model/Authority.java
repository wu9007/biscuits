package org.hv.biscuits.spine.model;

import org.hv.biscuits.spine.AbstractBisEntity;
import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.annotation.OneToMany;

import java.util.List;

/**
 * @author leyan95 2019/1/30
 */
@Entity(table = "T_AUTHORITY", businessName = "权限")
public class Authority extends AbstractBisEntity {
    private static final long serialVersionUID = 8811730322368476299L;
    @Column(name = "SERVICE_ID")
    private String serviceId;
    @Column(name = "BUNDLE_ID")
    private String bundleId;
    @Column(name = "ID")
    private String id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @OneToMany(clazz = RoleAuthRelation.class, bridgeField = "authUuid")
    private List<RoleAuthRelation> roleAuthRelationList;

    public Authority() {
    }

    public Authority(String serviceId, String bundleId, String id, String name, String description) {
        this.serviceId = serviceId;
        this.bundleId = bundleId;
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RoleAuthRelation> getRoleAuthRelationList() {
        return roleAuthRelationList;
    }

    public void setRoleAuthRelationList(List<RoleAuthRelation> roleAuthRelationList) {
        this.roleAuthRelationList = roleAuthRelationList;
    }
}

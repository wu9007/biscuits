package org.hunter.skeleton.spine.model;

import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.annotation.ManyToOne;
import org.hunter.pocket.annotation.OneToMany;
import org.hunter.pocket.model.BaseEntity;

import java.util.List;

/**
 * @author wujianchuan 2019/2/25
 */
@Entity(table = "TBL_BUNDLE", tableId = 108, businessName = "菜单")
public class Bundle extends BaseEntity {
    private static final long serialVersionUID = -5562305365699924057L;

    @Column(name = "BUNDLE_ID")
    private String bundleId;
    @Column(name = "BUNDLE_NAME")
    private String bundleName;
    @Column(name = "SERVER_ID")
    private String serverId;
    @Column(name = "WITH_AUTH")
    private Boolean withAuth;
    @ManyToOne(name = "GROUP_UUID")
    private Long groupUuid;
    @OneToMany(clazz = Mapper.class, name = "BUNDLE_UUID")
    private List<Mapper> mappers;

    public Bundle() {
    }

    public Bundle(String bundleId, String bundleName, String serverId, Boolean withAuth) {
        this.bundleId = bundleId;
        this.bundleName = bundleName;
        this.serverId = serverId;
        this.withAuth = withAuth;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getBundleName() {
        return bundleName;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Boolean getWithAuth() {
        return withAuth;
    }

    public void setWithAuth(Boolean withAuth) {
        this.withAuth = withAuth;
    }

    public Long getGroupUuid() {
        return groupUuid;
    }

    public void setGroupUuid(Long groupUuid) {
        this.groupUuid = groupUuid;
    }

    public List<Mapper> getMappers() {
        return mappers;
    }

    public void setMappers(List<Mapper> mappers) {
        this.mappers = mappers;
    }
}

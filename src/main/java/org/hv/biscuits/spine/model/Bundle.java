package org.hv.biscuits.spine.model;

import org.hv.biscuits.spine.AbstractBisEntity;
import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.annotation.OneToMany;

import java.util.List;

/**
 * @author leyan95 2019/2/25
 */
@Entity(table = "T_BUNDLE", businessName = "功能点")
public class Bundle extends AbstractBisEntity {
    private static final long serialVersionUID = -5562305365699924057L;

    @Column
    private String bundleId;
    @Column
    private String bundleName;
    @Column
    private String serviceId;
    @Column
    private Boolean withAuth;
    @Column
    private Double sort;
    @OneToMany(clazz = Mapper.class, bridgeField = "bundleUuid")
    private List<Mapper> mappers;

    public Bundle() {
    }

    public Bundle(String bundleId, String bundleName, String serviceId, Boolean withAuth) {
        this.bundleId = bundleId;
        this.bundleName = bundleName;
        this.serviceId = serviceId;
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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Boolean getWithAuth() {
        return withAuth;
    }

    public void setWithAuth(Boolean withAuth) {
        this.withAuth = withAuth;
    }

    public List<Mapper> getMappers() {
        return mappers;
    }

    public void setMappers(List<Mapper> mappers) {
        this.mappers = mappers;
    }

    public Double getSort() {
        return sort;
    }

    public void setSort(Double sort) {
        this.sort = sort;
    }
}

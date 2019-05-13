package org.hunter.skeleton.spine.model;

import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.annotation.OneToMany;
import org.hunter.pocket.model.BaseEntity;
import org.hunter.skeleton.spine.model.repository.SpineBundleRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wujianchuan 2019/2/25
 */
@Entity(table = "TBL_BUNDLE_GROUP", tableId = 109, businessName = "菜单分组")
public class BundleGroup extends BaseEntity {
    private static final long serialVersionUID = 4032798805062223157L;

    @Column(name = "GROUP_NAME")
    private String groupName;
    @Column(name = "SORT")
    private Double sort;
    @Column(name = "SERVER_ID")
    private String serverId;
    @OneToMany(clazz = BundleGroupRelation.class, bridgeField = "groupUuid")
    private List<BundleGroupRelation> bundleGroupRelations;

    private List<Bundle> bundles;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Double getSort() {
        return sort;
    }

    public void setSort(Double sort) {
        this.sort = sort;
    }

    public List<Bundle> getBundles() {
        return bundles;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public void setBundles(List<Bundle> bundles) {
        this.bundles = bundles;
    }

    public List<BundleGroupRelation> getBundleGroupRelations() {
        return bundleGroupRelations;
    }

    public void setBundleGroupRelations(List<BundleGroupRelation> bundleGroupRelations) {
        this.bundleGroupRelations = bundleGroupRelations;
    }

    public List<Bundle> getBundles(SpineBundleRepository bundleRepository) throws SQLException {
        List<Bundle> bundles = new ArrayList<>();
        List<BundleGroupRelation> bundleGroupRelations = this.getBundleGroupRelations();
        if (bundleGroupRelations != null && bundleGroupRelations.size() > 0) {
            Bundle bundle;
            for (BundleGroupRelation bundleGroupRelation : bundleGroupRelations) {
                bundle = bundleRepository.findOne(bundleGroupRelation.getBundleUuid());
                if (bundle != null) {
                    bundles.add(bundle);
                }
            }
        }
        return bundles;
    }
}

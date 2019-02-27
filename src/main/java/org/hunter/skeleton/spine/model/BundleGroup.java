package org.hunter.skeleton.spine.model;

import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.annotation.OneToMany;
import org.hunter.pocket.model.BaseEntity;

import java.util.List;

/**
 * @author wujianchuan 2019/2/25
 */
@Entity(table = "TBL_BUNDLE_GROUP", tableId = 109, businessName = "菜单分组")
public class BundleGroup extends BaseEntity {
    private static final long serialVersionUID = 4032798805062223157L;

    @Column(name = "GROUP_ID")
    private String groupId;
    @Column(name = "GROUP_NAME")
    private String groupName;
    @Column(name = "SORT")
    private Integer sort;
    @OneToMany(clazz = BundleGroupRelation.class, name = "GROUP_UUID")
    private List<BundleGroupRelation> bundleGroupRelations;

    private List<Bundle> bundles;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<Bundle> getBundles() {
        return bundles;
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
}

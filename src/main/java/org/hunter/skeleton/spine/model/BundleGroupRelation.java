package org.hunter.skeleton.spine.model;

import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.annotation.ManyToOne;
import org.hunter.pocket.model.BaseEntity;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
@Entity(table = "TBL_BUNDLE_GROUP_RELATION", tableId = 110)
public class BundleGroupRelation extends BaseEntity {
    private static final long serialVersionUID = 1870767494450334886L;

    @ManyToOne(columnName = "BUNDLE_UUID", clazz = Bundle.class, upBridgeField = "uuid")
    private String bundleUuid;
    @ManyToOne(columnName = "GROUP_UUID", clazz = BundleGroup.class, upBridgeField = "uuid")
    private String groupUuid;
    @Column(name = "SORT")
    private Double sort;

    public BundleGroupRelation() {
    }

    public String getBundleUuid() {
        return bundleUuid;
    }

    public void setBundleUuid(String bundleUuid) {
        this.bundleUuid = bundleUuid;
    }

    public String getGroupUuid() {
        return groupUuid;
    }

    public void setGroupUuid(String groupUuid) {
        this.groupUuid = groupUuid;
    }

    public Double getSort() {
        return sort;
    }

    public void setSort(Double sort) {
        this.sort = sort;
    }
}

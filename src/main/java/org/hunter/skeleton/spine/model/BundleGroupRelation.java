package org.hunter.skeleton.spine.model;

import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.annotation.ManyToOne;
import org.hunter.pocket.model.BaseEntity;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
@Entity(table = "TBL_BUNDLE_GROUP_RELATION", tableId = 110, history = false)
public class BundleGroupRelation extends BaseEntity {
    private static final long serialVersionUID = 1870767494450334886L;

    @ManyToOne(name = "BUNDLE_UUID")
    private Long bundleUuid;
    @ManyToOne(name = "GROUP_UUID")
    private Long groupUuid;
    @Column(name = "SORT")
    private Double sort;

    public BundleGroupRelation() {
    }

    public Long getBundleUuid() {
        return bundleUuid;
    }

    public void setBundleUuid(Long bundleUuid) {
        this.bundleUuid = bundleUuid;
    }

    public Long getGroupUuid() {
        return groupUuid;
    }

    public void setGroupUuid(Long groupUuid) {
        this.groupUuid = groupUuid;
    }

    public Double getSort() {
        return sort;
    }

    public void setSort(Double sort) {
        this.sort = sort;
    }
}

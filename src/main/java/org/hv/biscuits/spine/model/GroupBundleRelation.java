package org.hv.biscuits.spine.model;

import org.hv.biscuits.spine.AbstractBisEntity;
import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.annotation.ManyToOne;

/**
 * @author wujianchuan 2020/7/7 08:53
 */
@Entity(table = "T_GROUP_BUNDLE", businessName = "菜单分组")
public class GroupBundleRelation extends AbstractBisEntity {
    private static final long serialVersionUID = -1087289338536769116L;

    @ManyToOne(columnName = "GROUP_UUID", clazz = Group.class, upBridgeField = "uuid")
    private String groupUuid;
    @ManyToOne(columnName = "BUNDLE_UUID", clazz = Bundle.class, upBridgeField = "uuid")
    private String bundleUuid;
    @Column
    private Double sort;

    public String getGroupUuid() {
        return groupUuid;
    }

    public void setGroupUuid(String groupUuid) {
        this.groupUuid = groupUuid;
    }

    public String getBundleUuid() {
        return bundleUuid;
    }

    public void setBundleUuid(String bundleUuid) {
        this.bundleUuid = bundleUuid;
    }

    public Double getSort() {
        return sort;
    }

    public void setSort(Double sort) {
        this.sort = sort;
    }
}

package org.hv.biscuits.spine.model;

import org.hv.biscuits.spine.AbstractBisEntity;
import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;

import java.util.List;

/**
 * @author leyan95 2019/2/25
 */
@Entity(table = "T_GROUP", businessName = "菜单分组")
public class Group extends AbstractBisEntity {
    private static final long serialVersionUID = -5562305365699924057L;

    @Column
    private String groupId;
    @Column
    private String groupName;
    @Column
    private String serviceId;
    @Column
    private Double sort;
    @Column
    private String additionalServiceId;
    @Column
    private String parentUuid;
    @Column
    private byte[] img;
    private String imgBase64;

    private Boolean leafNode;

    public Boolean getLeafNode() {
        return leafNode;
    }

    public void setLeafNode(Boolean leafNode) {
        this.leafNode = leafNode;
    }

    private List<Bundle> bundles;

    public Group() {
    }

    public Group(String groupId, String groupName, String serviceId, Double sort, String parentUuid) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.serviceId = serviceId;
        this.sort = sort;
        this.parentUuid = parentUuid;
    }

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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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

    public void setBundles(List<Bundle> bundles) {
        this.bundles = bundles;
    }

    public String getAdditionalServiceId() {
        return additionalServiceId;
    }

    public void setAdditionalServiceId(String additionalServiceId) {
        this.additionalServiceId = additionalServiceId;
    }

    public String getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(String parentUuid) {
        this.parentUuid = parentUuid;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }
}

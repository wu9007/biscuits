package org.hv.biscuits.spine.model;

import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.model.BaseEntity;

/**
 * @author leyan95 2020/6/26
 */
@Entity(table = "T_POST", tableId = 116)
public class Post extends BaseEntity {
    private static final long serialVersionUID = -1762486150168555776L;
    @Column(businessName = "服务标识")
    private String serviceId;
    @Column(businessName = "名称")
    private String name;
    @Column(businessName = "拼音码")
    private String spell;
    @Column(businessName = "排序")
    private Double sort;
    @Column(businessName = "是否可用")
    private Boolean enable;
    @Column(businessName = "备注")
    private String memo;

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSort() {
        return sort;
    }

    public void setSort(Double sort) {
        this.sort = sort;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}

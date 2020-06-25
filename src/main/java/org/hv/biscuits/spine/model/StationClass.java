package org.hv.biscuits.spine.model;

import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.model.BaseEntity;

/**
 * @author wujianchuan
 */
@Entity(table = "T_STATION_CLASS", tableId = 115, businessName = "站级别")
public class StationClass extends BaseEntity {
    private static final long serialVersionUID = -1685713399689632879L;
    @Column
    private String name;
    @Column
    private String code;
    @Column
    private String spell;
    @Column
    private Integer sort;
    @Column
    private Boolean enable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}

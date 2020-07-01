package org.hv.biscuits.spine.model;

import org.hv.biscuits.spine.AbstractBisEntity;
import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.annotation.Join;
import org.hv.pocket.constant.JoinMethod;

/**
 * @author wujianchuan
 */
@Entity(table = "T_STATION", businessName = "站")
public class Station extends AbstractBisEntity {
    private static final long serialVersionUID = -5525761142462824878L;
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
    @Column
    private String parentUuid;
    @Column
    private String classUuid;
    @Join(columnName = "CLASS_UUID", columnSurname = "CLASS_NAME", businessName = "血站级别名称", joinTable = "T_STATION_CLASS", joinTableSurname = "T1", joinMethod = JoinMethod.LEFT, bridgeColumn = "UUID", destinationColumn = "NAME")
    private String className;

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

    public String getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(String parentUuid) {
        this.parentUuid = parentUuid;
    }

    public String getClassUuid() {
        return classUuid;
    }

    public void setClassUuid(String classUuid) {
        this.classUuid = classUuid;
    }
}

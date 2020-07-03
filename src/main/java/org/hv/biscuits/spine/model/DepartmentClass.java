package org.hv.biscuits.spine.model;

import org.hv.biscuits.spine.AbstractBisEntity;
import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.annotation.OneToMany;

import java.util.List;

/**
 * @author leyan95
 */
@Entity(table = "T_DEPARTMENT_CLASS", businessName = "部门分类")
public class DepartmentClass extends AbstractBisEntity {
    private static final long serialVersionUID = 9222338165115905198L;
    @Column
    private String name;
    @Column
    private String spell;
    @Column
    private Integer sort;
    @Column
    private Boolean enable;
    @OneToMany(clazz = DepartmentClassRelation.class, bridgeField = "classUuid")
    private List<DepartmentClassRelation> departmentClassRelations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<DepartmentClassRelation> getDepartmentClassRelations() {
        return departmentClassRelations;
    }

    public void setDepartmentClassRelations(List<DepartmentClassRelation> departmentClassRelations) {
        this.departmentClassRelations = departmentClassRelations;
    }
}

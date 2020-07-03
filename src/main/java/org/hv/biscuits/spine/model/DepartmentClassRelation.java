package org.hv.biscuits.spine.model;

import org.hv.biscuits.spine.AbstractBisEntity;
import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.annotation.ManyToOne;

/**
 * @author leyan95
 */
@Entity(table = "T_DEPARTMENT_CLASS_RELATION", businessName = "部门和部门分类的关联")
public class DepartmentClassRelation extends AbstractBisEntity {
    private static final long serialVersionUID = -1404449942640363728L;

    @ManyToOne(columnName = "DEPARTMENT_UUID", clazz = Department.class, upBridgeField = "uuid")
    private String departmentUuid;
    @ManyToOne(columnName = "CLASS_UUID", clazz = DepartmentClass.class, upBridgeField = "uuid")
    private String classUuid;

    public String getDepartmentUuid() {
        return departmentUuid;
    }

    public void setDepartmentUuid(String departmentUuid) {
        this.departmentUuid = departmentUuid;
    }

    public String getClassUuid() {
        return classUuid;
    }

    public void setClassUuid(String classUuid) {
        this.classUuid = classUuid;
    }
}

package org.hv.biscuits.spine.model;

import org.hv.biscuits.spine.AbstractBisEntity;
import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;

/**
 * @author wujianchuan 2020/12/1 08:55
 */
@Entity(table = "T_BILL_TYPE", businessName = "单据类型")
public class BillType extends AbstractBisEntity {
    private static final long serialVersionUID = -790695207598746313L;
    @Column
    private String code;
    @Column
    private String name;
    @Column
    private String serviceId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}

package org.hv.biscuits.spine.model;

import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.model.BaseEntity;

/**
 * @author wujianchuan 2020/11/8 16:05
 */
@Entity(table = "T_ID_RULE", businessName = "单号规则")
public class IdRule extends BaseEntity {

    private static final long serialVersionUID = -209429860243408646L;
    @Column
    private String serviceId;
    @Column
    private String clientType;
    @Column
    private String billType;
    @Column
    private String billCode;
    @Column
    private Boolean diffByDept;
    @Column
    private String dateFormatter;
    @Column
    private int serialLength;
    @Column
    private String resetRule;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public Boolean getDiffByDept() {
        return diffByDept;
    }

    public void setDiffByDept(Boolean diffByDept) {
        this.diffByDept = diffByDept;
    }

    public String getDateFormatter() {
        return dateFormatter;
    }

    public void setDateFormatter(String dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public int getSerialLength() {
        return serialLength;
    }

    public void setSerialLength(int serialLength) {
        this.serialLength = serialLength;
    }

    public String getResetRule() {
        return resetRule;
    }

    public void setResetRule(String resetRule) {
        this.resetRule = resetRule;
    }
}

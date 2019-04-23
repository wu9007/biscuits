package org.hunter.skeleton.spine.model;

import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.model.BaseEntity;

import java.util.Date;

/**
 * @author wujianchuan 2019/3/23
 * @version 1.0
 */
@Entity(table = "TBL_HISTORY", tableId = 111)
public class History extends BaseEntity {
    private static final long serialVersionUID = 1783568186627352468L;

    @Column(name = "OPERATE")
    private String operate;
    @Column(name = "OPERATE_TIME")
    private Date operateTime;
    @Column(name = "OPERATOR")
    private String operator;
    @Column(name = "BUSINESS_UUID")
    private String businessUuid;
    @Column(name = "OPERATE_CONTENT")
    private String operateContent;

    public History() {
    }

    public History(String operate, Date operateTime, String operator, String businessUuid, String operateContent) {
        this.operate = operate;
        this.operateTime = operateTime;
        this.operator = operator;
        this.businessUuid = businessUuid;
        this.operateContent = operateContent;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getBusinessUuid() {
        return businessUuid;
    }

    public void setBusinessUuid(String businessUuid) {
        this.businessUuid = businessUuid;
    }

    public String getOperateContent() {
        return operateContent;
    }

    public void setOperateContent(String operateContent) {
        this.operateContent = operateContent;
    }
}

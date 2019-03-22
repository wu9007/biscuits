package org.hunter.skeleton.spine.model;

import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.model.AbstractEntity;

import java.util.Date;

/**
 * @author wujianchuan 2019/3/23
 * @version 1.0
 */
@Entity(table = "TBL_HISTORY", tableId = 110, uuidGenerator = "str_increment")
public class History extends AbstractEntity {
    private static final long serialVersionUID = 1783568186627352468L;

    @Column(name = "OPERATE")
    private String operate;
    @Column(name = "OPERATE_TIME")
    private Date operateTime;
    @Column(name = "OPERATOR")
    private String operator;
    @Column(name = "OPERATE_CONTENT")
    private String operateContent;

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

    public String getOperateContent() {
        return operateContent;
    }

    public void setOperateContent(String operateContent) {
        this.operateContent = operateContent;
    }
}

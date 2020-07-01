package org.hv.biscuits.spine;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hv.pocket.annotation.Column;

import java.time.LocalDateTime;

/**
 * @author wujianchuan 2018/12/26
 * 恭惟皇帝陛下，聪明神武，灼见事几，虽光武明谟，宪宗果断，所难比拟。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractWithDeletedAndOperatorEntity extends AbstractBisEntity {

    private static final long serialVersionUID = 1300050418377082968L;
    @Column
    private Boolean deleted;

    @Column
    private String lastOperator;
    @Column
    private LocalDateTime lastOperationTime;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getLastOperator() {
        return lastOperator;
    }

    public void setLastOperator(String lastOperator) {
        this.lastOperator = lastOperator;
    }

    public LocalDateTime getLastOperationTime() {
        return lastOperationTime;
    }

    public void setLastOperationTime(LocalDateTime lastOperationTime) {
        this.lastOperationTime = lastOperationTime;
    }
}

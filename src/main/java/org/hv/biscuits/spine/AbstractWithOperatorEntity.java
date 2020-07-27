package org.hv.biscuits.spine;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hv.pocket.annotation.Column;

import java.time.LocalDateTime;

/**
 * @author wujianchuan 2018/12/26
 * 臣虽至愚至陋，何能有知，徒以忠愤所激，不能自已。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractWithOperatorEntity extends AbstractBisEntity {

    private static final long serialVersionUID = 1300050418377082968L;

    @Column
    private String lastOperator;
    @Column
    private LocalDateTime lastOperationTime;

    public String getLastOperator() {
        return lastOperator;
    }

    public AbstractWithOperatorEntity setLastOperator(String lastOperator) {
        this.lastOperator = lastOperator;
        return this;
    }

    public LocalDateTime getLastOperationTime() {
        return lastOperationTime;
    }

    public AbstractWithOperatorEntity setLastOperationTime(LocalDateTime lastOperationTime) {
        this.lastOperationTime = lastOperationTime;
        return this;
    }
}

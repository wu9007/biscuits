package org.hv.biscuits.spine;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hv.pocket.annotation.Column;

/**
 * @author wujianchuan 2018/12/26
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractWithDeletedEntity extends AbstractBisEntity {

    private static final long serialVersionUID = 1300050418377082968L;
    @Column
    private Boolean deleted;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}

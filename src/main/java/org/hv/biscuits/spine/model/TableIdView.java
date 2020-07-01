package org.hv.biscuits.spine.model;

import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.View;

import java.io.Serializable;

/**
 * @author wujianchuan
 */
@View
public class TableIdView implements Serializable {
    private static final long serialVersionUID = -154153554980594157L;
    @Column
    private String tableName;
    @Column
    private String tableId;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
}

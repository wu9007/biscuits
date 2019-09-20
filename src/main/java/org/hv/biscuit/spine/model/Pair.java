package org.hv.biscuit.spine.model;

import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;

import java.io.Serializable;

/**
 * @author wujianchuan
 */
@Entity(table = "", tableId = -1)
public class Pair implements Serializable {
    private static final long serialVersionUID = 5656710925928861884L;
    @Column(name = "label")
    private String label;
    @Column(name = "value")
    private String value;

    public Pair() {
    }

    public Pair(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

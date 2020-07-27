package org.hv.biscuits.spine.model;

import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.View;

import java.io.Serializable;

/**
 * @author leyan95
 */
@View
public class Pair implements Serializable {
    private static final long serialVersionUID = 5656710925928861884L;
    @Column
    private String label;
    @Column
    private String value;
    @Column
    private Boolean deleted;

    public Pair() {
    }

    public Pair(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public Pair(String label, String value, Boolean deleted) {
        this.label = label;
        this.value = value;
        this.deleted = deleted;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}

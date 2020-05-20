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

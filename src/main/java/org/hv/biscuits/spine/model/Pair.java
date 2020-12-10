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

    @Override
    public int hashCode() {
        int hashCode = 0;
        if (label != null) {
            hashCode = 31 * hashCode + label.hashCode();
        }
        if (value != null) {
            hashCode = 31 * hashCode + value.hashCode();
        }
        if (deleted != null) {
            hashCode = 31 * hashCode + deleted.hashCode();
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Pair pair = (Pair) obj;
            if (label == null) {
                if (pair.label != null) {
                    return false;
                }
            } else {
                if (pair.label == null) {
                    return false;
                }
                if (!label.equals(pair.label)) {
                    return false;
                }
            }
            if (value == null) {
                if (pair.value != null) {
                    return false;
                }
            } else {
                if (pair.value == null) {
                    return false;
                }
                if (!value.equals(pair.value)) {
                    return false;
                }
            }
            if (deleted == null) {
                return pair.deleted == null;
            } else {
                if (pair.deleted == null) {
                    return false;
                }
                if (!deleted.equals(pair.deleted)) {
                    return false;
                }
            }
            return true;
        }
    }
}

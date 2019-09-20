package org.hv.biscuit.controller;

import java.io.Serializable;

/**
 * @author wujianchuan
 */
public class Filter implements Serializable {
    private static final long serialVersionUID = -7107210286814248680L;
    private String key;
    private Object value;
    private String operate;

    Filter() {
    }

    Filter(String key, Object value, String operate) {
        this.key = key;
        this.value = value;
        this.operate = operate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }
}
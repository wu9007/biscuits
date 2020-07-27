package org.hv.biscuits.controller;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author leyan95
 */
public class Filter implements Serializable {
    private static final long serialVersionUID = -7107210286814248680L;
    private static final String ZULU = "Z";
    private String key;
    private Object value;
    private String operate;
    private String valueType;

    Filter() {
    }

    Filter(String key, Object value, String operate, String valueType) {
        this.key = key;
        this.value = value;
        this.operate = operate;
        this.valueType = valueType;
    }

    public static Filter newEquInstance(String key, Object value) {
        return new Filter(key, value, FilterView.Operate.EQU, null);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        if (this.value instanceof String) {
            String strValue = (String) this.value;
            if (ValueType.DATE.equals(this.valueType)) {
                if (strValue.endsWith(ZULU)) {
                    return LocalDateTime.ofInstant(Instant.parse(strValue), ZoneId.systemDefault()).toLocalDate();
                } else {
                    return LocalDateTime.parse(strValue, DateTimeFormatter.ISO_LOCAL_DATE).toLocalDate();
                }
            } else if (ValueType.DATE_TIME.equals(this.getValueType())) {
                if (strValue.endsWith(ZULU)) {
                    return LocalDateTime.ofInstant(Instant.parse(strValue), ZoneId.systemDefault());
                } else {
                    return LocalDateTime.parse(strValue, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                }
            }
        }
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

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public interface ValueType {
        String DATE = "date";
        String DATE_TIME = "dateTime";
    }
}
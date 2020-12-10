package org.hv.biscuits.repository;

/**
 * 包含字段名称及新旧数据
 *
 * @author wujianchuan 2020/9/14 09:14
 */
public class AtomUpdateBox {
    private final String fieldName;
    private final Object oldValue;
    private final Object newValue;

    public AtomUpdateBox(String fieldName, Object oldValue, Object newValue) {
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public static AtomUpdateBox newInstance(String fieldName, Object oldValue, Object newValue) {
        return new AtomUpdateBox(fieldName, oldValue, newValue);
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }
}

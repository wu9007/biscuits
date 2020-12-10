package org.hv.biscuits.core.history;

import org.hv.biscuits.constant.OperateEnum;
import org.hv.pocket.model.AbstractEntity;

import java.lang.reflect.Field;

/**
 * @author wujianchuan 2020/9/21 10:01
 */
public class HistoryData {
    private final AbstractEntity newEntity;
    private AbstractEntity oldEntity;
    private final Field field;
    private OperateEnum operateEnum;

    HistoryData(AbstractEntity newEntity, AbstractEntity oldEntity, Field field, OperateEnum operateEnum) {
        this.newEntity = newEntity;
        this.oldEntity = oldEntity;
        this.field = field;
        this.operateEnum = operateEnum;
    }

    HistoryData(AbstractEntity newEntity, Field field) {
        this.newEntity = newEntity;
        this.field = field;
    }

    AbstractEntity getNewEntity() {
        return newEntity;
    }


    AbstractEntity getOldEntity() {
        return oldEntity;
    }

    Field getField() {
        return field;
    }

    OperateEnum getOperateEnum() {
        return operateEnum;
    }
}

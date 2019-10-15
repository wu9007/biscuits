package org.hv.biscuits.annotation;

import org.hv.biscuits.constant.OperateEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wujianchuan
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Track {
    /**
     * 被操作的实体类型
     *
     * @return String
     */
    String data();

    /**
     * 操作的业务名称 例：修改 *** 动态字典
     *
     * @return operate name
     */
    String operateName();

    /**
     * 操作人
     *
     * @return avatar
     */
    String operator();

    /**
     * 操作类型
     *
     * @return OperateEnum
     */
    OperateEnum operate();
}

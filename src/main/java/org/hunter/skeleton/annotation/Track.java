package org.hunter.skeleton.annotation;

import org.hunter.skeleton.constant.OperateEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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

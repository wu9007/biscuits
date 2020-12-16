package org.hv.biscuits.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author leyan95 2019/1/31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Affairs {
    /**
     * 要建立的数据库会话名称，默认为空字符时则取通过其他方式获取sessionName
     *
     * @return 要建立的数据库会话名称
     */
    String sessionName() default "";

    /**
     * 是否开启事务，默认开启
     *
     * @return 是否开启事务
     */
    boolean on() default true;
}

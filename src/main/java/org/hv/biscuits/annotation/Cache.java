package org.hv.biscuits.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wujianchuan
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
    /**
     * This is the cached index keyword
     *
     * @return keyword
     */
    String key();

    /**
     * The unit is milliseconds
     *
     * @return the suggested duration 10 milliseconds, if any (or empty value otherwise)
     */
    long duration() default 10;
}

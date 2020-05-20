package org.hv.biscuits.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author leyan95 2019/2/20
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping
public @interface Action {

    @AliasFor(value = "path", annotation = RequestMapping.class)
    String[] actionId();

    @AliasFor(annotation = RequestMapping.class)
    RequestMethod[] method() default {RequestMethod.GET};

    String authId() default "";

    @AliasFor(annotation = RequestMapping.class)
    String[] produces() default {"application/json; charset=UTF-8"};

    boolean responseEncrypt() default false;
}

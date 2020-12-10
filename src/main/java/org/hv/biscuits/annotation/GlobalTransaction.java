package org.hv.biscuits.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解api方法，凡是该方法中的持久化操作均进行镜像日志以及sql语句记录，
 * 以便出现分布式事务异常时进行人工干预，
 * 日志可通过 TraceId + TransactionId 进行定位
 *
 * @author leyan95 2019/2/20
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GlobalTransaction {
}

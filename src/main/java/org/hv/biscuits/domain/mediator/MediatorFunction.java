package org.hv.biscuits.domain.mediator;

import org.hv.biscuits.service.Service;

/**
 * @author wujianchuan
 */
@FunctionalInterface
public interface MediatorFunction<T extends Service, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     * @throws Exception e
     */
    R apply(T t, Object... u) throws Exception;
}
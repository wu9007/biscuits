package org.hv.biscuit.mediator;

import org.hv.biscuit.service.Service;

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
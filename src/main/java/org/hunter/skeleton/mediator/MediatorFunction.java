package org.hunter.skeleton.mediator;

import org.hunter.skeleton.service.AbstractService;

import java.util.Map;

@FunctionalInterface
public interface MediatorFunction<T extends AbstractService, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     * @throws Exception e
     */
    R apply(T t, Map<String, Object> u) throws Exception;
}
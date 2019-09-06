package org.hunter.skeleton.mediator;

import org.hunter.skeleton.service.AbstractService;

import java.util.List;
import java.util.Map;

/**
 * @author wujianchuan
 */
public interface Mediator {
    /**
     * Get service clazz list.
     *
     * @return Class array.
     */
    List<Class<AbstractService>> getServiceClazzList();

    /**
     * Register service
     *
     * @param service Service instance who extend {@link AbstractService}
     */
    void registerService(AbstractService service) throws Exception;

    /**
     * call another service method
     *
     * @param businessName business name
     * @param args         arguments
     * @return obj
     * @throws Exception e
     */
    Object call(String businessName, Map<String, Object> args) throws Exception;
}

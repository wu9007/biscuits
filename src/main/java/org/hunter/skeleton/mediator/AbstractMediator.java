package org.hunter.skeleton.mediator;

import org.hunter.skeleton.service.AbstractService;
import org.hunter.skeleton.utils.AopTargetUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractMediator implements Mediator {
    private List<Class<AbstractService>> serviceClazzList;
    private Map<String, AbstractService> serviceMap;
    private Map<String, MediatorFunction<AbstractService, Object>> functionMap = new ConcurrentHashMap<>();

    protected AbstractMediator() {
        this.serviceClazzList = new ArrayList<>();
        this.serviceMap = new ConcurrentHashMap<>();
        this.init();
    }

    /**
     * install service clazz
     * install business function
     */
    protected abstract void init();

    protected <T extends AbstractService> void installService(Class<T> tClass) {
        this.serviceClazzList.add((Class<AbstractService>) tClass);
    }

    protected <T extends AbstractService> void installBusiness(String businessName, MediatorFunction<T, Object> businessFunction) {
        this.functionMap.putIfAbsent(businessName, (MediatorFunction<AbstractService, Object>) businessFunction);
    }

    protected <T extends AbstractService> Object apply(String businessName, Class<T> serviceClazz, Map<String, Object> args) throws Exception {
        MediatorFunction<T, Object> businessFunction = (MediatorFunction<T, Object>) this.functionMap.get(businessName);
        if (businessFunction == null) {
            throw new NoSuchMediatorFunctionException(String.format("can not found the mediator function named %s ", businessName));
        }
        return businessFunction.apply((T) this.serviceMap.get(serviceClazz.getName()), args);
    }

    @Override
    public List<Class<AbstractService>> getServiceClazzList() {
        return this.serviceClazzList;
    }

    @Override
    public void registerService(AbstractService service) throws Exception {
        this.serviceMap.putIfAbsent(AopTargetUtils.getTarget(service).getClass().getName(), service);
    }
}

package org.hunter.skeleton.mediator;

import org.hunter.skeleton.service.Service;
import org.hunter.skeleton.utils.AopTargetUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wujianchuan
 */
public abstract class AbstractMediator implements Mediator {
    private List<Class<Service>> serviceClazzList;
    private Map<String, Service> serviceMap;
    private Map<String, MediatorFunction<Service, Object>> functionMap = new ConcurrentHashMap<>();

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

    protected <T extends Service> void installService(Class<T> tClass) {
        this.serviceClazzList.add((Class<Service>) tClass);
    }

    protected <T extends Service> void installBusiness(String businessName, MediatorFunction<T, Object> businessFunction) {
        this.functionMap.putIfAbsent(businessName, (MediatorFunction<Service, Object>) businessFunction);
    }

    protected <T extends Service> Object apply(String businessName, Class<T> serviceClazz, Map<String, Object> args) throws Exception {
        MediatorFunction<T, Object> businessFunction = (MediatorFunction<T, Object>) this.functionMap.get(businessName);
        if (businessFunction == null) {
            throw new NoSuchMediatorFunctionException(String.format("can not found the mediator function named %s ", businessName));
        }
        return businessFunction.apply((T) this.serviceMap.get(serviceClazz.getName()), args);
    }

    @Override
    public List<Class<Service>> getServiceClazzList() {
        return this.serviceClazzList;
    }

    @Override
    public void registerService(Service service) throws Exception {
        this.serviceMap.putIfAbsent(AopTargetUtils.getTarget(service).getClass().getName(), service);
    }
}

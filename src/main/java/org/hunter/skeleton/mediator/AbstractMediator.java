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
    private Map<String, String> businessServiceClazzNameMap = new ConcurrentHashMap<>();

    protected AbstractMediator() {
        this.serviceClazzList = new ArrayList<>();
        this.serviceMap = new ConcurrentHashMap<>();
        this.init();
    }

    /**
     * install business function witch you need in the future.
     */
    protected abstract void init();

    protected <T extends Service> void installBusiness(String businessName, Class<T> serviceClazz, MediatorFunction<T, Object> businessFunction) {
        this.functionMap.putIfAbsent(businessName, (MediatorFunction<Service, Object>) businessFunction);
        this.businessServiceClazzNameMap.putIfAbsent(businessName, serviceClazz.getName());
        if (!this.serviceClazzList.contains(serviceClazz)) {
            this.serviceClazzList.add((Class<Service>) serviceClazz);
        }
    }

    protected <T extends Service> Object apply(String businessName, Map<String, Object> args) throws Exception {
        MediatorFunction<T, Object> businessFunction = (MediatorFunction<T, Object>) this.functionMap.get(businessName);
        if (businessFunction == null) {
            throw new NoSuchMediatorFunctionException(String.format("can not found the mediator function named %s ", businessName));
        }
        String serviceClazzName = this.businessServiceClazzNameMap.get(businessName);
        Service service = this.serviceMap.get(serviceClazzName);
        return businessFunction.apply((T) service, args);
    }

    @Override
    public List<Class<Service>> getServiceClazzList() {
        return this.serviceClazzList;
    }

    @Override
    public void registerService(Service service) throws Exception {
        this.serviceMap.putIfAbsent(AopTargetUtils.getTarget(service).getClass().getInterfaces()[0].getName(), service);
    }
}

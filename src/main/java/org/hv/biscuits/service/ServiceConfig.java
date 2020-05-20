package org.hv.biscuits.service;

import org.hv.biscuits.repository.AbstractRepository;
import org.hv.biscuits.repository.RepositoryFactory;
import org.hv.biscuits.service.AbstractService;
import org.hv.biscuits.utils.AopTargetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author leyan95
 */
@Component
public class ServiceConfig {

    private final ApplicationContext context;
    private final List<AbstractService> serviceList;

    @Autowired
    public ServiceConfig(ApplicationContext context, @Nullable List<AbstractService> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    public void init() throws Exception {
        Class<?> clazz;
        String clazzName;
        if (serviceList != null && serviceList.size() > 0) {
            for (AbstractService serviceProxy : serviceList) {
                Object target = AopTargetUtils.getTarget(serviceProxy);
                // install repository list
                clazz = target.getClass();
                clazzName = clazz.getName();
                for (Field field : clazz.getDeclaredFields()) {
                    Map<String, ?> beanMap = this.context.getBeansOfType(field.getType());
                    if (beanMap.size() > 0) {
                        Object bean;
                        if (beanMap.size() == 1) {
                            bean = beanMap.values().toArray()[0];
                        } else {
                            bean = beanMap.get(field.getName());
                            if (bean == null) {
                                throw new NullPointerException(String.format("找不到 %s 中需要的Bean —— %s", clazzName, field.getName()));
                            }
                        }
                        if (bean instanceof AbstractRepository) {
                            RepositoryFactory.register(clazzName, (AbstractRepository) bean);
                        }
                    }
                }
            }
        }
    }
}

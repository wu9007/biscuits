package org.hv.biscuit.launcher;

import org.hv.biscuit.repository.Repository;
import org.hv.biscuit.repository.RepositoryFactory;
import org.hv.biscuit.service.AbstractService;
import org.hv.biscuit.utils.AopTargetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author wujianchuan
 */
@Component
@Order(2)
public class ServiceLauncher implements CommandLineRunner {

    private final List<AbstractService> serviceList;

    @Autowired
    public ServiceLauncher(@Nullable List<AbstractService> serviceList) {
        this.serviceList = serviceList;
    }

    @Override
    public void run(String... args) throws Exception {
        Class clazz;
        String clazzName;
        if (serviceList != null && serviceList.size() > 0) {
            for (AbstractService serviceProxy : serviceList) {
                Object target = AopTargetUtils.getTarget(serviceProxy);
                // install repository list
                clazz = target.getClass();
                clazzName = clazz.getName();
                Field repositoryListField = clazz.getSuperclass().getDeclaredField("repositoryFieldList");
                repositoryListField.setAccessible(true);
                List<Field> serviceFieldList = (List<Field>) repositoryListField.get(target);
                for (Field field : serviceFieldList) {
                    field.setAccessible(true);
                    Repository repository = (Repository) field.get(target);
                    RepositoryFactory.register(clazzName, repository);
                }
            }
        }
    }
}

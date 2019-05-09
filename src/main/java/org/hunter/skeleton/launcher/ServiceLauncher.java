package org.hunter.skeleton.launcher;

import org.hunter.skeleton.repository.Repository;
import org.hunter.skeleton.repository.RepositoryFactory;
import org.hunter.skeleton.service.AbstractService;
import org.hunter.skeleton.utils.AopTargetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
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
    public ServiceLauncher(List<AbstractService> serviceList) {
        this.serviceList = serviceList;
    }

    @Override
    public void run(String... args) throws Exception {
        Class clazz;
        String clazzName;
        for (AbstractService serviceProxy : serviceList) {
            Object target = AopTargetUtils.getTarget(serviceProxy);
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

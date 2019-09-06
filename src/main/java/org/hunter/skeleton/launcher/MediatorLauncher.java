package org.hunter.skeleton.launcher;

import org.hunter.skeleton.mediator.Mediator;
import org.hunter.skeleton.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wujianchuan
 */
@Component
@Order(2)
public class MediatorLauncher implements CommandLineRunner {
    private final
    List<Mediator> mediators;
    private final ApplicationContext context;

    @Autowired
    public MediatorLauncher(List<Mediator> mediators, ApplicationContext context) {
        this.mediators = mediators;
        this.context = context;
    }

    @Override
    public void run(String... args) throws Exception {
        for (Mediator mediator : mediators) {
            for (Class<? extends AbstractService> serviceClazz : mediator.getServiceClazzList()) {
                mediator.registerService(context.getBean(serviceClazz));
            }
        }
    }
}

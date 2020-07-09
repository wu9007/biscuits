package org.hv.biscuits.core;

import org.hv.biscuits.annotation.Controller;
import org.hv.biscuits.domain.process.ProcessNodeConfig;
import org.hv.biscuits.permission.Permission;
import org.hv.biscuits.service.ServiceConfig;
import org.hv.pocket.lunch.PocketConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author leyan95
 */
@Component
public class BiscuitsConfigDefault implements BiscuitsConfig {
    @Value("${spring.application.name}")
    private String applicationName;
    private final PocketConfig pocketConfig;
    private final ProcessNodeConfig processNodeConfig;
    private final ServiceConfig serviceConfig;

    private final List<Object> bundleList;
    private List<Permission> permissions = new ArrayList<>();

    public BiscuitsConfigDefault(ApplicationContext applicationContext, @Nullable List<Permission> permissions, PocketConfig pocketConfig, ProcessNodeConfig processNodeConfig, ServiceConfig serviceConfig) {
        this.pocketConfig = pocketConfig;
        this.processNodeConfig = processNodeConfig;
        this.serviceConfig = serviceConfig;
        bundleList = Arrays.asList(applicationContext.getBeansWithAnnotation(Controller.class).values().toArray());
        if (permissions != null) {
            this.permissions = permissions;
        }
    }

    @Override
    public BiscuitsConfig init() throws Exception {
        String serviceId = applicationName.replaceAll("\\d+", "");
        this.pocketConfig.init();
        this.processNodeConfig.init();
        this.serviceConfig.init();
        ActionHolder.getInstance().init(serviceId, bundleList, permissions);
        return this;
    }
}

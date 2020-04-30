package org.hv.biscuits.core;

import org.hv.biscuits.core.views.BundleView;
import org.hv.biscuits.core.views.PermissionView;
import org.hv.biscuits.domain.process.ProcessNodeConfig;
import org.hv.biscuits.service.ServiceConfig;
import org.hv.pocket.lunch.PocketConfig;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author wujianchuan
 */
@Component
public class BiscuitsConfigDefault implements BiscuitsConfig {

    private final PocketConfig pocketConfig;
    private final ActionFactory actionFactory;
    private final ProcessNodeConfig processNodeConfig;
    private final ServiceConfig serviceConfig;
    private boolean withPersistence;
    private boolean runWithDevelopEnvironment;

    public BiscuitsConfigDefault(PocketConfig pocketConfig, ActionFactory actionFactory, ProcessNodeConfig processNodeConfig, ServiceConfig serviceConfig) {
        this.pocketConfig = pocketConfig;
        this.actionFactory = actionFactory;
        this.processNodeConfig = processNodeConfig;
        this.serviceConfig = serviceConfig;
    }

    @Override
    public BiscuitsConfig init(boolean withPersistence) throws Exception {
        this.withPersistence = withPersistence;
        if (this.withPersistence) {
            this.pocketConfig.init();
            this.processNodeConfig.init();
        }
        this.serviceConfig.init();
        this.actionFactory.init();
        return this;
    }

    @Override
    public Map<String, BundleView> getActionMap() {
        return this.actionFactory.getActionMap();
    }

    @Override
    public Map<String, PermissionView> getPermissionMap() {
        return this.actionFactory.getPermissionMap();
    }

    @Override
    public BiscuitsConfig persistenceMapper(String serviceId, Map<String, BundleView> actionMap) throws SQLException {
        if (!this.withPersistence) {
            throw new IllegalAccessError("程序未开启持久化功能");
        }
        if (runWithDevelopEnvironment) {
            System.out.println("============");
        }
        return this;
    }

    @Override
    public BiscuitsConfig resetPersistencePermission(String serviceId, Map<String, PermissionView> permissionMap) throws SQLException {
        if (!this.withPersistence) {
            throw new IllegalAccessError("程序未开启持久化功能");
        }
        if (runWithDevelopEnvironment) {
            System.out.println("============");
        }
        return this;
    }

    @Override
    public BiscuitsConfig runWithDevelopEnvironment() {
        this.runWithDevelopEnvironment = true;
        return this;
    }
}

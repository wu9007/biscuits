package org.hv.biscuits;

import org.hv.biscuits.core.BiscuitsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author leyan95
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class Launcher implements CommandLineRunner {

    private final BiscuitsConfig biscuitsConfig;
    @Value("${spring.application.name}")
    private String serverId;
    @Value("${biscuits.withPersistence:true}")
    private boolean withPersistence;

    public Launcher(BiscuitsConfig biscuitsConfig) {
        this.biscuitsConfig = biscuitsConfig;
    }

    @Override
    public void run(String... args) throws Exception {
        BiscuitsConfig biscuitsConfig = this.biscuitsConfig.init(withPersistence);
        if (withPersistence) {
            biscuitsConfig
                    .runWithDevelopEnvironment()
                    .setBiscuitsDatabaseSessionId("biscuits")
                    .resetPersistencePermission(serverId.toUpperCase(), this.biscuitsConfig.getPermissionMap())
                    .persistenceMapper(serverId.toUpperCase(), this.biscuitsConfig.getActionMap());
        }
    }
}

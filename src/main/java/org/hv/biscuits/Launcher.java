package org.hv.biscuits;

import org.hv.biscuits.core.BiscuitsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author wujianchuan
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class Launcher implements CommandLineRunner {

    private final BiscuitsConfig biscuitsConfig;
    @Value("${spring.application.name}")
    private String serverId;

    public Launcher(BiscuitsConfig biscuitsConfig) {
        this.biscuitsConfig = biscuitsConfig;
    }

    @Override
    public void run(String... args) throws Exception {
        this.biscuitsConfig.init(true)
                .runWithDevelopEnvironment()
                .setBiscuitsDatabaseSessionId("biscuits")
                .resetPersistencePermission(serverId, this.biscuitsConfig.getPermissionMap())
                .persistenceMapper(serverId, this.biscuitsConfig.getActionMap());
    }
}

package org.hv.biscuits;

import org.hv.biscuits.core.BiscuitsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author leyan95
 */
@Component
public class PersistenceConfig {

    private final BiscuitsConfig biscuitsConfig;
    @Value("${spring.application.name}")
    private String serverId;
    @Value("${biscuits.withPersistence:true}")
    private boolean withPersistence;

    public PersistenceConfig(BiscuitsConfig biscuitsConfig) {
        this.biscuitsConfig = biscuitsConfig;
    }

    @PostConstruct
    public void run() throws Exception {
        if (withPersistence) {
            try {
                BiscuitsConfig biscuitsConfig = this.biscuitsConfig.init(withPersistence);
                biscuitsConfig
                        .runWithDevelopEnvironment()
                        .setBiscuitsDatabaseSessionId("biscuits")
                        .resetPersistencePermission(serverId.toUpperCase(), this.biscuitsConfig.getPermissionMap())
                        .persistenceMapper(serverId.toUpperCase(), this.biscuitsConfig.getActionMap());
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(e);
            }
        }
    }
}

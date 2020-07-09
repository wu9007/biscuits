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

    @Value("${biscuits.withPersistence:true}")
    private boolean withPersistence;

    private final BiscuitsConfig biscuitsConfig;

    public PersistenceConfig(BiscuitsConfig biscuitsConfig) {
        this.biscuitsConfig = biscuitsConfig;
    }

    @PostConstruct
    public void run() throws Exception {
        if (withPersistence) {
            try {
                this.biscuitsConfig.init();
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(e);
            }
        }
    }
}

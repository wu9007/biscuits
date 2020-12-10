package org.hv;

import org.hv.biscuits.core.BiscuitsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author wujianchuan 2018/12/25
 */
@SpringBootApplication
public class Application {
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    Object getPersistenceConfig() {
        return new Object() {
            @Value("${biscuits.withPersistence:true}")
            private boolean withPersistence;
            @Resource
            private BiscuitsConfig biscuitsConfig;

            @PostConstruct
            public void run() throws Exception {
                if (withPersistence) {
                    biscuitsConfig.setDesKey("sward007").setSm4Key("sward18713839007").init();
                }
            }
        };
    }
}

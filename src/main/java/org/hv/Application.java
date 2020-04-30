package org.hv;

import org.hv.biscuits.core.BiscuitsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wujianchuan 2018/12/25
 */
@SpringBootApplication
@ComponentScan(basePackages = {"org.hv.*"})
public class Application {
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner getCommandLineRunner() {
        return new CommandLineRunner() {
            @Autowired
            private BiscuitsConfig biscuitsConfig;

            @Override
            public void run(String... args) throws Exception {
                this.biscuitsConfig.init(true)
                        .runWithDevelopEnvironment()
                        .setBiscuitsDatabaseSessionId("biscuits")
                        .resetPersistencePermission("test1", this.biscuitsConfig.getPermissionMap())
                        .persistenceMapper("test1", this.biscuitsConfig.getActionMap());
            }
        };
    }
}

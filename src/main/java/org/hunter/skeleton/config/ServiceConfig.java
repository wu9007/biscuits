package org.hunter.skeleton.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wujianchuan 2019/1/23
 */
@Component
@ConfigurationProperties(prefix = "service")
public class ServiceConfig {

    private List<ServiceNodeConfig> node;

    public List<ServiceNodeConfig> getNode() {
        return node;
    }

    public void setNode(List<ServiceNodeConfig> node) {
        this.node = node;
    }
}

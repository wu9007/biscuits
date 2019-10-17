package org.hv.biscuits.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wujianchuan
 */
@Component(value = "BiscuitJwtAccountConfig")
@ConfigurationProperties(prefix = "biscuits.filter")
public class FilterPathConfig {

    private Boolean turnOn;

    private String excludeUrlPatterns;

    public Boolean getTurnOn() {
        return turnOn;
    }

    public void setTurnOn(Boolean turnOn) {
        this.turnOn = turnOn;
    }

    public String getExcludeUrlPatterns() {
        return excludeUrlPatterns;
    }

    public void setExcludeUrlPatterns(String excludeUrlPatterns) {
        this.excludeUrlPatterns = excludeUrlPatterns;
    }
}

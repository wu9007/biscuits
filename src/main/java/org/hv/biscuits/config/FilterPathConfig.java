package org.hv.biscuits.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wujianchuan
 */
@Component(value = "BiscuitJwtAccountConfig")
@ConfigurationProperties(prefix = "biscuit.filter")
public class FilterPathConfig {

    private String excludeUrlPatterns;

    public String getExcludeUrlPatterns() {
        return excludeUrlPatterns;
    }

    public void setExcludeUrlPatterns(String excludeUrlPatterns) {
        this.excludeUrlPatterns = excludeUrlPatterns;
    }
}

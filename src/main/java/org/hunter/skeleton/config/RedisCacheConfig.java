package org.hunter.skeleton.config;

import org.hunter.skeleton.cache.CacheTemplateSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author wujianchuan
 */
@Component
@ConfigurationProperties(prefix = "skeleton")
public class RedisCacheConfig {

    @Bean
    @ConfigurationProperties(prefix = "skeleton.cache")
    public RedisStandaloneConfiguration redisConfig() {
        return new RedisStandaloneConfiguration();
    }

    @Bean
    @ConditionalOnBean(name = "redisCacheConfig")
    public LettuceConnectionFactory redisConnectionFactory(@Qualifier(value = "redisConfig") RedisStandaloneConfiguration redisConfig) {
        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean
    @ConditionalOnBean(name = "redisConnectionFactory")
    public StringRedisTemplate stringRedisTemplate(@Qualifier(value = "redisConnectionFactory") LettuceConnectionFactory redisConnectionFactory) {
        return CacheTemplateSerializer.createStringTemplate(redisConnectionFactory);
    }

    @Bean
    @ConditionalOnBean(name = "redisConnectionFactory")
    public RedisTemplate<String, Object> redisTemplate(@Qualifier(value = "redisConnectionFactory") LettuceConnectionFactory redisConnectionFactory) {
        return CacheTemplateSerializer.createObjectTemplate(redisConnectionFactory);
    }
}
package org.hunter.skeleton.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author wujianchuan 2019/1/17
 */
@Component
public class ResponseCache {

    private final RedisTemplate<String, Object> objRedisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    private final Map<String, String> mapLock = new ConcurrentHashMap<>(100);

    public ResponseCache(StringRedisTemplate stringRedisTemplate, RedisTemplate<String, Object> objRedisTemplate) {

        this.objRedisTemplate = objRedisTemplate;

        this.stringRedisTemplate = stringRedisTemplate;
    }

    public Map<String, String> getMapLock() {
        return this.mapLock;
    }

    public String generateKey(String sessionName, Class clazz, Serializable uuid) {
        return sessionName + clazz.getName() + uuid;
    }

    public void set(String key, String value) {
        this.stringRedisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value) {
        this.objRedisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, Long timeOut) {
        this.objRedisTemplate.opsForValue().set(key, value, timeOut, TimeUnit.SECONDS);
    }

    public String getValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public Object getObj(String key) {
        return this.objRedisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        this.objRedisTemplate.delete(key);
    }

    public boolean exists(String key) {
        Boolean exists = this.objRedisTemplate.hasKey(key);
        return exists != null && exists;
    }
}

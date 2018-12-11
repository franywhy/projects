package com.hq.common.interfaze;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Created by Glenn on 2017/7/11 0011.
 */
public abstract class AbstractRedisService {

    @Autowired(required = false)
    protected RedisTemplate redisTemplate;


    /**
     * when expiredTime < 0 or expiredTime = null
     * the object in redis will be permanent
     **/
    protected void pushValueCache(Object key, Object value, Long expiredTime) {
        if (expiredTime == null || expiredTime < 0) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.MILLISECONDS);
        }

    }

    protected <T> T getValueCache(Object key, Class<T> clazz) {
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj == null) {
            return null;
        }
        if (clazz == Long.class) {
            Object tmpObj = null;
            if (obj.getClass() == Integer.class) {
                tmpObj = new Long((Integer) obj);
            } else if (obj.getClass() == String.class) {
                tmpObj = Long.parseLong((String) obj);
            } else if (obj.getClass() == char.class) {
                tmpObj = new Long((char) obj);
            } else if (obj.getClass() == Byte.class) {
                tmpObj = new Long((Byte) obj);
            } else {
                tmpObj = obj;
            }
            return (T) tmpObj;

        } else {
            return (T) obj;
        }
    }

    protected void putHashCache(String mapName, Map<Object, Object> map, long idleTime) {
        redisTemplate.opsForHash().putAll(mapName, map);
        redisTemplate.expire(mapName, idleTime, TimeUnit.MILLISECONDS);
    }

    protected void putHashCache(String mapName, Object keyName, Object value, long idleTime) {
        redisTemplate.opsForHash().put(mapName, String.valueOf(keyName), value);
        redisTemplate.expire(mapName, idleTime, TimeUnit.MILLISECONDS);
    }

    protected <T> T getHashCache(String mapName, Object keyName, Class<T> clazz) {
        return (T) redisTemplate.opsForHash().get(mapName, String.valueOf(keyName));
    }

    protected void removeCache(String keyName) {
        redisTemplate.delete(keyName);
    }

    protected void extendCache(String keyName, long idleTime) {
        redisTemplate.expire(keyName, idleTime, TimeUnit.MILLISECONDS);
    }
}

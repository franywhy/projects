package com.hqjy.msg.config.redis;

import com.hqjy.msg.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by baobao on 2017/12/22 0022.
 */
@Configuration
public class RedisConfig {

    private static Log log = LogFactory.getLog(RedisConfig.class);
    @Value("${redis.hostName}")
    private String hostName;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.maxIdle}")
    private int maxIdle;
    @Value("${redis.password}")
    private String password;
    @Value("${redis.maxTotal}")
    private int maxTotal;
    @Value("${redis.timeout}")
    private int timeout;

    @Value("${redis.maxWaitMillis}")
    private long maxWaitMillis;

    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;

    @Bean(name = "redisTemplate")
    public RedisTemplate redisTemplate(@Value("${redis.index}") int index) {
        RedisTemplate temple = new RedisTemplate();
        temple.setConnectionFactory(connectionFactory(hostName, port, password,
                maxIdle, maxTotal, index, maxWaitMillis, testOnBorrow,timeout));
        return createRedisTemplate(index);
    }

    @Bean(name = "channelTemplate")
    public RedisTemplate channelTemplate(@Value("${redis.indexChannel}") int index) {
        return createRedisTemplate(index);
    }


    @Bean(name = "waitTemplate")
    public RedisTemplate waitTemplate(@Value("${redis.indexWait}") int index) {
        return createRedisTemplate(index);
    }

    @Bean(name = "msgTemplate")
    public RedisTemplate msgTemplate(@Value("${redis.indexMsg}") int index) {
        return createRedisTemplate(index);
    }
    public RedisTemplate createRedisTemplate(int index){
        RedisTemplate temple = new RedisTemplate();
        StringRedisSerializer redisSerializer = new StringRedisSerializer();//Long类型不可以会出现异常信息;
        temple.setKeySerializer(redisSerializer);

        temple.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        temple.setConnectionFactory(connectionFactory(hostName, port, password,
                maxIdle, maxTotal, index, maxWaitMillis, testOnBorrow,timeout));
        log.info("-------------------- RedisTemplate-"+index+" init ---------------------");
        return temple;
    }

    public RedisConnectionFactory connectionFactory(String hostName, int port,
                                                    String password, int maxIdle, int maxTotal, int index,
                                                    long maxWaitMillis, boolean testOnBorrow,int timeout) {
        JedisConnectionFactory jedis = new JedisConnectionFactory();
        jedis.setHostName(hostName);
        jedis.setPort(port);
        // 设置超时时间
        jedis.setTimeout(timeout);

        if (!StringUtils.isEmpty(password)) {
            jedis.setPassword(password);
        }
        if (index != 0) {
            jedis.setDatabase(index);
        }
        jedis.setPoolConfig(poolCofig(maxIdle, maxTotal, maxWaitMillis,
                testOnBorrow));
        // 初始化连接pool
        jedis.afterPropertiesSet();
        RedisConnectionFactory factory = jedis;

        return factory;
    }

    public JedisPoolConfig poolCofig(int maxIdle, int maxTotal,
                                     long maxWaitMillis, boolean testOnBorrow) {
        JedisPoolConfig poolCofig = new JedisPoolConfig();
        poolCofig.setMaxIdle(maxIdle);
        poolCofig.setMaxTotal(maxTotal);
        poolCofig.setMaxWaitMillis(maxWaitMillis);
        poolCofig.setTestOnBorrow(testOnBorrow);
        return poolCofig;
    }


}

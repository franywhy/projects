package com.hq.learningcenter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by longduyuan on 2018/11/29 0029.
 * @author hq
 */
@Configuration
public class RedisConfig {

    @Value("${redis.sentinel.group}")
    protected String sentinelGroupName;

    @Value("${redis.sentinel.nodes}")
    protected String sentinelNodes;

    @Value("${redis.maxTotal}")
    protected int maxTotal;

    @Value("${redis.minIdle}")
    protected int minIdle;

    @Value("${redis.maxIdle}")
    protected int maxIdle;

    @Value("${redis.maxWaitMillis}")
    protected long maxWaitMillis;

    @Value("${redis.testOnBorrow}")
    protected boolean testOnBorrow;

    @Value("${redis.testOnReturn}")
    protected boolean testOnReturn;

    @Value("${redis.password}")
    protected String password;

    @Value("${redis.timeout}")
    protected int timeout;

    @Bean
    public RedisSentinelConfiguration redisSentinelConfiguration() {
        String[] nodes = sentinelNodes.split(",");
        Set<String> setNodes = new HashSet<>();
        for (String n : nodes) {
            setNodes.add(n.trim());
        }
        RedisSentinelConfiguration configuration = new RedisSentinelConfiguration(sentinelGroupName, setNodes);
        return configuration;
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        poolConfig.setTestOnBorrow(testOnBorrow);
        poolConfig.setTestOnReturn(testOnReturn);
        return poolConfig;
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean(name = "mainRedis")
    @Primary
    public StringRedisTemplate mainRedis() {
        return buildRedisTemplate(buildConnectionFactory(0));
    }

    @Bean(name = "adminRedisTemplate")
    public StringRedisTemplate adminRedisTemplate() {
        return buildRedisTemplate(buildConnectionFactory(4));
    }

    private JedisConnectionFactory buildConnectionFactory(int database) {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(redisSentinelConfiguration(), jedisPoolConfig());
        connectionFactory.setUsePool(true);
        connectionFactory.setTimeout(timeout);
        connectionFactory.setDatabase(database);
        connectionFactory.setPassword(password);
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }

    protected StringRedisTemplate buildRedisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory);
        template.setValueSerializer(stringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

}
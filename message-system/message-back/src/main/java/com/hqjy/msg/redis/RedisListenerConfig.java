package com.hqjy.msg.config.redis;

import com.hqjy.msg.listener.redis.MsgMessageListener;
import com.hqjy.msg.listener.redis.WaitMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2017/11/22 0022.
 */

@Configuration
public class RedisListenerConfig {

    @Autowired
    @Qualifier("waitTemplate")
    private RedisTemplate<Object, Object> waitTemplate;

    @Autowired
    @Qualifier("msgTemplate")
    private RedisTemplate<Object, Object> msgTemplate;
    @Autowired
    private WaitMessageListener waitMessageListener;

    @Autowired
    private MsgMessageListener msgMessageListener;

    @Value("${redis.topic.wait}")
    private String topicWait;

    @Value("${redis.topic.msg}")
    private String topicMsg;

    @Value("${task.pool.corePoolSize}")
    private int corePoolSize;
    @Value("${task.pool.maxPoolSize}")
    private int maxPoolSize;
    @Value("${task.pool.keepAliveSeconds}")
    private int keepAliveSeconds;
    @Value("${task.pool.queueCapacity}")
    private int queueCapacity;
    @Value("${task.pool.threadNamePrefix}")
    private String threadNamePrefix;

    @Bean
    public RedisMessageListenerContainer configRedisMessageListenerContainerWait(@Qualifier("myTaskAsyncPool")Executor executor) {

        return setRedisMessageListenerContainer(waitMessageListener, topicWait, waitTemplate, executor);
    }

    @Bean

    public RedisMessageListenerContainer configRedisMessageListenerContainerMsg(@Qualifier("myTaskAsyncPool")Executor executor) {

        return setRedisMessageListenerContainer(msgMessageListener, topicMsg, msgTemplate, executor);
    }

    private RedisMessageListenerContainer setRedisMessageListenerContainer(MessageListener listener, String topic, RedisTemplate redisTemplate, Executor executor) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 设置Redis的连接工厂
        container.setConnectionFactory(redisTemplate.getConnectionFactory());
        // 设置监听使用的线程池
        container.setTaskExecutor(executor);
        // 设置监听的Topic
        ChannelTopic channelTopic = new ChannelTopic(topic);
        // 设置监听器
        container.addMessageListener(listener, channelTopic);
        return container;
    }

    @Bean(name = "myTaskAsyncPool")// 配置线程池
    public Executor myTaskAsyncPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix(threadNamePrefix);

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
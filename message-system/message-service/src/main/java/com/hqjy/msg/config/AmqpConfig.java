package com.hqjy.msg.config;

import com.hqjy.msg.util.Constant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created by baobao on 2017/12/23 0023.
 */
@Configuration
public class AmqpConfig {
    public static final String EXCHANGE   = "spring-msg-exchange";
    public static final String ROUTINGKEY = "spring-msg-rount";


    @Value("${mq.hostName}")
    private String hostName;
    @Value("${mq.userName}")
    private String userName;
    @Value("${mq.port}")
    private int port;
    @Value("${mq.pwd}")
    private String pwd;
    @Value("${mq.virtualHost}")
    private String virtualHost;
    @Value("${mq.confirms}")
    private boolean confirms;

    @Bean
    public ConnectionFactory connectionFactory(){

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        String host = hostName +":"+ String.valueOf(port);
        cachingConnectionFactory.setAddresses(host);
        cachingConnectionFactory.setUsername(userName);
        cachingConnectionFactory.setPassword(pwd);
        cachingConnectionFactory.setVirtualHost(virtualHost);
        cachingConnectionFactory.setPublisherConfirms(confirms);//必须要设置
        return cachingConnectionFactory;

    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(jsonMessageConverter());
        return  template;
    }
    @Bean
    public MessageConverter jsonMessageConverter(){
        return new JsonMessageConverter();
    }



    /*****--------------------------------------------------------------*/
    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     *
     *
     FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     HeadersExchange ：通过添加属性key-value匹配
     DirectExchange:按照routingkey分发到指定队列
     TopicExchange:多关键字匹配
     */
    @Bean
    public DirectExchange defaultExchange(){
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue queueSyncRedisToMysql(){
        return new Queue(Constant.QUEUE_SYNC_REDIS_TO_MYSQL,true);//队列持久
    }


    @Bean
    public Queue queueReadedRedis(){
        return new Queue(Constant.QUEUE_CONSUMER_READED_REDIS,true);//队列持久
    }

    @Bean
    public Queue queueReadedDb(){
        return new Queue(Constant.QUEUE_CONSUMER_READED_DB,true);//队列持久
    }

    @Bean
    public Queue queueExpireMsg(){
        return new Queue(Constant.QUEUE_WAIT_EXPIRE,true);//队列持久
    }

 /*   @Bean
    public Queue queueRedisMsgWithUser(){
        return new Queue(Constant.QUEUE_CONSUMER_READED_REDIS,true);//队列持久
    }
*/
    /**
     * 处理待消费消息的队列
     * @return
     */
    @Bean
    public Queue queueWait(){
        return new Queue(Constant.QUEUE_WAIT,true);//队列持久
    }
    /**
     * 处理消息的队列
     * @return
     */
    @Bean
    public Queue queueSendMsg(){
        return new Queue(Constant.QUEUE_SEND_MSG,true);//队列持久
    }

    /**
     * 处理消息的队列
     * @return
     */
    @Bean
    public Queue queueMsg(){
        return new Queue(Constant.QUEUE_MSG,true);//队列持久
    }

    @Bean
    public Queue queueMsgExpire(){
        return new Queue(Constant.QUEUE_MSG_EXPIRE,true);//队列持久
    }

    @Bean
    public Queue queueConsumer(){
        return new Queue(Constant.QUEUE_CONSUMER,true);//队列持久
    }
    /**
     * 执行缓存消息的队列1
     * @return
     */
    @Bean
    public Queue queueMsgDetail(){
        return new Queue(Constant.QUEUE_MSG_DETAIL,true);//队列持久
    }

    /**
     * 执行缓存消息的队列2
     * @return
     */
    /*@Bean
    public Queue queueConsumer2(){
        return new Queue(Constant.QUEUE_CONSUMER_TWO,true);//队列持久
    }
    *//**
     * 处理用户的缓存消息的队列
     * @return
     */
    @Bean
    public Queue queueConsumerHandler(){
        return new Queue(Constant.QUEUE_CONSUMER_HANDLER,true);//队列持久
    }


    @Bean
    public Binding bindingReadedRedis(){
        return BindingBuilder.bind(queueReadedRedis()).to(defaultExchange()).with(Constant.QUEUE_CONSUMER_READED_REDIS);
    }

    @Bean
    public Binding bindingSyncRedisToMysql(){
        return BindingBuilder.bind(queueSyncRedisToMysql()).to(defaultExchange()).with(Constant.QUEUE_SYNC_REDIS_TO_MYSQL);
    }



    @Bean
    public Binding bindingReadedDb(){
        return BindingBuilder.bind(queueReadedDb()).to(defaultExchange()).with(Constant.QUEUE_CONSUMER_READED_DB);
    }

    @Bean
    public Binding bindingWait(){
        return BindingBuilder.bind(queueWait()).to(defaultExchange()).with(Constant.QUEUE_WAIT);
    }

    @Bean
    public Binding bindingConsumer(){
        return BindingBuilder.bind(queueConsumer()).to(defaultExchange()).with(Constant.QUEUE_CONSUMER);
    }
    @Bean
    public Binding bindingMsgDetail(){
        return BindingBuilder.bind(queueMsgDetail()).to(defaultExchange()).with(Constant.QUEUE_MSG_DETAIL);
    }
    /*@Bean
    public Binding bindingConsumer2(){
        return BindingBuilder.bind(queueConsumer2()).to(defaultExchange()).with(Constant.QUEUE_CONSUMER_TWO);
    }*/
    @Bean
    public Binding bindingConsumerHandler(){
        return BindingBuilder.bind(queueConsumerHandler()).to(defaultExchange()).with(Constant.QUEUE_CONSUMER_HANDLER);
    }
    @Bean
    public Binding bindingSendMsg(){
        return BindingBuilder.bind(queueSendMsg()).to(defaultExchange()).with(Constant.QUEUE_SEND_MSG);
    }

    @Bean
    public Binding bindingMsg(){
        return BindingBuilder.bind(queueMsg()).to(defaultExchange()).with(Constant.QUEUE_MSG);
    }
    @Bean
    public Binding bindingMsgExpire(){
        return BindingBuilder.bind(queueMsgExpire()).to(defaultExchange()).with(Constant.QUEUE_MSG_EXPIRE);
    }

    @Bean
    public Binding bindingExpireMsg(){
        return BindingBuilder.bind(queueExpireMsg()).to(defaultExchange()).with(Constant.QUEUE_WAIT_EXPIRE);
    }

    @Bean
    public Binding bindingDelWaitMsg(){
        return BindingBuilder.bind(queueDelWaitMsg()).to(defaultExchange()).with(Constant.QUEUE_WAIT_DEL);
    }

    @Bean
    public Queue queueDelWaitMsg(){
        return new Queue(Constant.QUEUE_WAIT_DEL,true);//队列持久
    }

    @Bean
    public Queue queueUpdateGroup(){
        return new Queue(Constant.QUEUE_UPDATE_GROUP,true);//队列持久
    }

    @Bean
    public Binding bindingUpdateGroup(){
        return BindingBuilder.bind(queueUpdateGroup()).to(defaultExchange()).with(Constant.QUEUE_UPDATE_GROUP);
    }
    @Bean
    public Queue queueUpdateGroupRecord(){
        return new Queue(Constant.QUEUE_UPDATE_GROUP_RECORD,true);//队列持久
    }

    @Bean
    public Binding bindingUpdateRecord(){
        return BindingBuilder.bind(queueUpdateGroupRecord()).to(defaultExchange()).with(Constant.QUEUE_UPDATE_GROUP_RECORD);
    }

    /*@Bean
    public SimpleMessageListenerContainer messageContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        container.setQueues(queue(),queue1());
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
        //container.setMessageListener(new MqRedisListener() );
        return container;
    }*/


}

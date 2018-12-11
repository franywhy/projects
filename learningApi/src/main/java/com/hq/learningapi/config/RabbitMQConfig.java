package com.hq.learningapi.config;

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
 * Created by DL on 2018/9/11.
 */
@Configuration
public class RabbitMQConfig {

    @Value("${mq.host}")
    private String host;
    @Value("${mq.username}")
    private String userName;
    @Value("${mq.port}")
    private int port;
    @Value("${mq.password}")
    private String password;



    @Bean
    public ConnectionFactory connectionFactory(){

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        String hostName = host +":"+ String.valueOf(port);
        cachingConnectionFactory.setAddresses(hostName);
        cachingConnectionFactory.setUsername(userName);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;

    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return  template;
    }

}

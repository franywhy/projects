package com.hqjy.msg.provide.operation;

import com.hqjy.msg.config.AmqpConfig;
import com.hqjy.msg.provide.MsgSend;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by baobao on 2017/12/23 0023.
 */
@Service("sendHaveAck")
public class SendHaveAck implements RabbitTemplate.ConfirmCallback,MsgSend {
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public SendHaveAck(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this);//rabbitTemplate如果为单例的话，那回调就是最后设置的内容
    }
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println(" 回调id:" + correlationData);
        if (ack) {
            System.out.println("消息成功消费");
        } else {
            System.out.println("消息消费失败:" + cause);
        }
    }

    @Override
    public void sendMsg(String channel, Object content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(AmqpConfig.EXCHANGE, channel, content, correlationId);
        
    }
}

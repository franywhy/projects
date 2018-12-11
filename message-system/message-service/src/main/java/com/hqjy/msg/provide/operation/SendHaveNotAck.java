package com.hqjy.msg.provide.operation;

import com.hqjy.msg.config.AmqpConfig;
import com.hqjy.msg.provide.MsgSend;
import com.hqjy.msg.util.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/12/27 0027.
 */
@Service("sendHaveNotAck")
public class SendHaveNotAck implements MsgSend {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public void sendMsg(String channel, Object content) {
        rabbitTemplate.convertAndSend(AmqpConfig.EXCHANGE, channel, StringUtils.objToJsonStr(content));
    }


}
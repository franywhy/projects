package com.hqjy.msg.listener.amqp;


import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.model.Objects;
import com.hqjy.msg.provide.MessageRunService;
import com.hqjy.msg.provide.MsgSend;
import com.hqjy.msg.provide.RedisMsgService;
import com.hqjy.msg.service.MsgMessageDetailService;
import com.hqjy.msg.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by baobao on 2017/12/26 0026.
 *
 * @author baobao
 * @desc hello 消息队列消费者
 */

@Component
public class ConsumerHandlerProcessor {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisMsgService<Objects> redisMsgService;

    /*@Autowired
    @Qualifier("redis")
    private MessageRunService messageRunService;
*/
    @Autowired
    @Qualifier("sendHaveNotAck")
    private MsgSend msgSend;


    /*@RabbitListener(queues = Constant.QUEUE_CONSUMER)
    public void consumer1(String message)throws Exception {
        invoke((String) message);
    }*/
    /*@RabbitListener(queues = Constant.QUEUE_CONSUMER_TWO)
    public void consumer2(String message)throws Exception {
        invoke((String) message);
    }*/
    @RabbitListener(queues = Constant.QUEUE_CONSUMER_HANDLER)
    public void consumerHandler(@Payload String message) throws Exception {
        Map messageMap = (Map) StringUtils.strToObj(message, Map.class);
        String msgId = (String) messageMap.get("msg_id");

        String sendTime = (String) messageMap.get("send_time");
        List list = (List) messageMap.get("list");

        Map map = null;
        String userId = "";
        Map result = null;
        List lists = ListUtils.listsToArray(list, Constant.SPLIT_LIST_SIZE);
        List listItem = null;

        for (int i = 0; i < lists.size(); i++) {
            result = new HashMap();
            result.put("msg_id", msgId);
            result.put("send_time", sendTime);
            listItem = (List) lists.get(i);
            result.put("list", listItem);

            msgSend.sendMsg(Constant.QUEUE_CONSUMER, result);
        }
    }

    @RabbitListener(queues = Constant.QUEUE_CONSUMER_READED_REDIS)
    public void SetConsumerReadedRedis(String message) throws Exception {
        Map map = (Map) StringUtils.strToObj(message, Map.class);
        List msgIds = (List) StringUtils.strToObj((String) map.get("msg_id"), List.class);
        String userId = (String) map.get("user_id");

        //ThreadPoolExecutorUtils.getRedisThreadPoolExecutor().execute(new Runnable() {
        //    @Override
        //    public void run() {
        msgIds.stream().forEach(p -> {
            String msgId = (String) p;
            redisMsgService.setZsetCacheUpdate(userId, msgId);
            redisMsgService.setCacheZSet("readed" + Constant.CHANNEL_REDIS, userId + "#" + msgId, DateUtils.getNowDate().getTime());
        });
        //   }
        //});
    }

}

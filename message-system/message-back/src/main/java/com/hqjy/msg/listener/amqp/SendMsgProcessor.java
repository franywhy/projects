package com.hqjy.msg.listener.amqp;

import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.model.Objects;
import com.hqjy.msg.provide.MessageRunService;
import com.hqjy.msg.provide.MsgManagerService;
import com.hqjy.msg.provide.RedisMsgService;
import com.hqjy.msg.provide.interfaces.MessageTypeInterface;
import com.hqjy.msg.util.Constant;
import com.hqjy.msg.util.DateUtils;
import com.hqjy.msg.util.ListUtils;
import com.hqjy.msg.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/2/5 0005.
 */
@Component
//@RabbitListener(queues = Constant.QUEUE_SEND_MSG)
public class SendMsgProcessor {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MsgManagerService msgManagerService;

    @Autowired
    private RedisMsgService<Objects> redisMsgService;

    @Value("${common.group}")
    private String groupChannel;

    @Autowired
    @Qualifier("redis")
    private MessageRunService messageRunService;
    @RabbitListener(queues = Constant.QUEUE_SEND_MSG)
    public void process1(String result) {
        sendMsg(result);
    }
    @RabbitListener(queues = Constant.QUEUE_SEND_MSG)
    public void process2(String result) {
        sendMsg(result);
    }


    /*@RabbitListener(queues = Constant.QUEUE_SEND_MSG)
    //@RabbitHandler
    public void process3(String result) {
        sendMsg(result);
    }*/

    private void sendMsg(String result) {
        Map map = (Map) StringUtils.strToObj(result, Map.class);
        List leaguers = null;
        try{
            leaguers = (List) map.get("leaguers");
        }catch (Exception e){}
        String message = (String) map.get("message");
        message = message.replaceAll("\\\\", "");
        String msg = (String) map.get("msg");
        msg = msg.replaceAll("\\\\", "");

        try {
            MsgMessage msgMessage = (MsgMessage) StringUtils.strToObj(message, MsgMessage.class);
            msgMessage.setMessage(msg);
            //msgSend.sendMsg(Constant.QUEUE_MSG_DETAIL,map);
            redisMsgService.setCacheZSet("detail" + Constant.CHANNEL_REDIS, msgMessage.getCode(), DateUtils.getNowDate().getTime());

//            Set set = messageRunService.setsIntoCacheZSet(msgMessage.getCode() + Constant.MSG_USER_REDIS + Constant.CHANNEL_REDIS, (List) ListUtils.strToListBySplit(msgMessage.getRecBy(), ",").stream().map(p -> {
//                return p + Constant.CHANNEL_REDIS;
//            }).collect(Collectors.toList()));
            Set set = messageRunService.setMsgChannel(msgMessage);
            List list = (List) set.stream().map(p -> {
                Map maps = new HashMap();
                maps.put(Constant.REDIS_KEY, (String) p);
                return maps;
            }).collect(Collectors.toList());
            if (msgMessage.getMsgType() == Constant.MSG_TYPE_COMMON) {
                redisMsgService.setCacheZSet("common" + Constant.MESSAGE_REDIS, msgMessage.getCode(), DateUtils.getNowDate().getTime());
            }
            messageRunService.saveMsg(msgMessage);
            if (msgMessage.getSendType() == Constant.MSG_SEND_TYPE_NOW) {
                if (ListUtils.listIsExists(leaguers)) {
                    //exists
                    msgManagerService.runing(leaguers, msgMessage);
                } else {
                    msgManagerService.runing(list, msgMessage);
                }
            }
        }catch (Exception e ){
            //logger.error(result,e);
        }


    }
}

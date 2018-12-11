package com.hqjy.msg.listener.amqp;

import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.model.Objects;
import com.hqjy.msg.provide.MessageRunService;
import com.hqjy.msg.provide.MsgManagerService;
import com.hqjy.msg.provide.MsgSend;
import com.hqjy.msg.provide.RedisMsgService;
import com.hqjy.msg.provide.interfaces.MessageTypeInterface;
import com.hqjy.msg.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by baobao on 2017/12/28 0028.
 */
@Component
public class MsgProcessor {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisMsgService<Objects> redisMsgService;

    @Autowired
    @Qualifier("redis")
    private MessageRunService messageRunService;

    @Autowired
    @Qualifier("sendHaveNotAck")
    private MsgSend msgSend;

    @RabbitListener(queues = Constant.QUEUE_MSG)
    public void msg1(String message)throws Exception {

        msg(message);

    }

    @RabbitListener(queues = Constant.QUEUE_MSG_DETAIL)
    public void msgDetail1(String message)throws Exception {

        msgDetail(message);

    }

    @RabbitListener(queues = Constant.QUEUE_MSG_DETAIL)
    public void msgDetail2(String message)throws Exception {

        msgDetail(message);

    }

    private void msgDetail(String result) {
        Map map = (Map) StringUtils.strToObj(result, Map.class);
        try {
        String message = (String) map.get("message");
        String msg = (String) map.get("msg");
        message = message.replaceAll("\\\\", "");
        msg = msg.replaceAll("\\\\", "");
        //message = message.substring(1,message.length()-1);
        MsgMessage msgMessage = (MsgMessage) StringUtils.strToObj(message, MsgMessage.class);
        msgMessage.setMessage(msg);

            redisMsgService.setCacheZSet("detail"+Constant.CHANNEL_REDIS,msgMessage.getCode(),DateUtils.getNowDate().getTime());
            messageRunService.removeKey(msgMessage.getCode()+Constant.MSG_USER_REDIS+Constant.CHANNEL_REDIS);
            Thread.sleep(500);
            messageRunService.setsIntoCacheZSet(msgMessage.getCode()+Constant.MSG_USER_REDIS+Constant.CHANNEL_REDIS, (List) ListUtils.strToListBySplit(msgMessage.getRecBy(),",").stream().map(p->{
                return p+Constant.CHANNEL_REDIS;
            }).collect(Collectors.toList()));
            Thread.sleep(500);
        }catch (Exception e){
            logger.error(result,e);
        }

    }

    public void msg(String result)throws Exception {
        Map map = (Map) StringUtils.strToObj(result, Map.class);
        String message = (String) map.get("message");
        String msg = (String) map.get("msg");
        message = message.replaceAll("\\\\", "");
        msg = msg.replaceAll("\\\\", "");
        try {
            MsgMessage msgMessage = (MsgMessage) StringUtils.strToObj(message, MsgMessage.class);
            msgMessage.setMessage(msg);

            messageRunService.saveMsg(msgMessage);
        }catch (Exception e){
            logger.error(result,e);
        }


    }

    @RabbitListener(queues = Constant.QUEUE_MSG_EXPIRE)
    public void msgExpire(String message)throws Exception {
        Map map = (Map) StringUtils.strToObj(message, Map.class);
        List list = (List) map.get("list");
        String msgId = (String)map.get("msg_id");
        redisMsgService.remove(msgId+Constant.USER_REDIS,msgId,list);
        redisMsgService.removeKey(msgId+Constant.MSG_USER_REDIS+Constant.CHANNEL_REDIS);
        messageRunService.removeHashMap("msg"+Constant.MESSAGE_REDIS,msgId);
        messageRunService.removeKey(msgId+Constant.MSG_USER_REDIS+Constant.CHANNEL_REDIS);
    }


}

package com.hqjy.msg.listener.amqp;

import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.model.Objects;
import com.hqjy.msg.provide.MessageRunService;
import com.hqjy.msg.provide.RedisMsgService;
import com.hqjy.msg.util.Constant;
import com.hqjy.msg.util.DateUtils;
import com.hqjy.msg.util.ListUtils;
import com.hqjy.msg.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by baobao on 2017/12/28 0028.
 */
@Component
public class WaitProcessor {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisMsgService<Objects> redisMsgService;

    @Autowired
    @Qualifier("redis")
    private MessageRunService messageRunService;

    @RabbitListener(queues = Constant.QUEUE_WAIT)
    public void wait(String result)throws Exception {
        Map map = (Map) StringUtils.strToObj(result, Map.class);
        String msgId = (String) map.get("msg_id");

        int compare = Double.valueOf(map.get("compare").toString()).intValue();
        redisMsgService.setCacheObject(msgId+ Constant.WAIT_REDIS,compare,compare);

        //
        String message = (String) map.get("message");
        String msg = (String) map.get("msg");
        message = message.replaceAll("\\\\", "");
        msg = msg.replaceAll("\\\\", "");
        //message = message.substring(1,message.length()-1);
        MsgMessage msgMessage = (MsgMessage) StringUtils.strToObj(message, MsgMessage.class);
        msgMessage.setMessage(msg);
        try {
            redisMsgService.setCacheZSet("detail"+Constant.CHANNEL_REDIS,msgMessage.getCode(), DateUtils.getNowDate().getTime());
            messageRunService.removeKey(msgMessage.getCode()+Constant.MSG_USER_REDIS+Constant.CHANNEL_REDIS);

//            messageRunService.setsIntoCacheZSet(msgMessage.getCode()+Constant.MSG_USER_REDIS+Constant.CHANNEL_REDIS, (List) ListUtils.strToListBySplit(msgMessage.getRecBy(),",").stream().map(p->{
//                return p+Constant.CHANNEL_REDIS;
//            }).collect(Collectors.toList()));
            messageRunService.setMsgChannel(msgMessage);
            Thread.sleep(500);
            messageRunService.saveMsg(msgMessage);
        }catch (Exception e){

        }

    }

    @RabbitListener(queues = Constant.QUEUE_WAIT_EXPIRE)
    public void waitMsgExpire(String message)throws Exception {
        Map map = (Map) StringUtils.strToObj(message, Map.class);
        String msgId = (String) map.get("msg_id");
        int compare = ((Double)map.get("compare")).intValue();
        redisMsgService.setExpire(msgId+ Constant.WAIT_REDIS,compare);
    }
    @RabbitListener(queues = Constant.QUEUE_WAIT_DEL)
    public void delWaitMsgExpire(String message)throws Exception {
        Map map = (Map) StringUtils.strToObj(message, Map.class);
        String msgId = (String) map.get("msg_id");
        redisMsgService.removeKey(msgId+ Constant.WAIT_REDIS);
    }

}

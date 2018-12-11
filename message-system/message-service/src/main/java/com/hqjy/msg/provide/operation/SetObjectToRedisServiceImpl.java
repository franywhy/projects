package com.hqjy.msg.provide.operation;

import com.hqjy.msg.provide.MsgSend;
import com.hqjy.msg.provide.SetObjectToRedisService;
import com.hqjy.msg.util.Constant;
import com.hqjy.msg.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by baobao on 2017/12/29 0029.
 */
@Service
public class SetObjectToRedisServiceImpl implements SetObjectToRedisService {

    @Autowired
    @Qualifier("sendHaveNotAck")
    private MsgSend msgSend;
    /**
     * 写入到待消费队列
     * @param msgId
     * @param sendTime
     * @param nowDate
     */
    public  void setWaitMsg(String msgId,String  sendTime,Date nowDate){
        Date sendDate = DateUtils.stringToDate(sendTime,DateUtils.DATE_FORMAT);
        Map map = new HashMap();
        map.put("msg_id",msgId);
        map.put("compare", DateUtils.compare(nowDate,sendDate));
        msgSend.sendMsg(Constant.QUEUE_WAIT, map);
    }

    /**
     * 设置已读信息（Redis）
     * @param msgId
     * @param userId
     */
    public void setReadedConsumerMsgToRedis(String msgId, String userId) {
        Map map = new HashMap();
        map.put("msg_id",msgId);
        map.put("user_id", userId);
        msgSend.sendMsg(Constant.QUEUE_CONSUMER_READED_REDIS, map);
        msgSend.sendMsg(Constant.QUEUE_CONSUMER_READED_DB, map);
    }

    /**
     * 消息体写入到Redis
     * @param msgId
     */
    public void setMsgToRedis(String msgId,String msg){
        Map map = new HashMap();
        map.put("msg_id",msgId);
        map.put("msg",msg);
        msgSend.sendMsg(Constant.QUEUE_MSG,map);
    }
    /**
     * 把消息写入每个用户的消息列表里
     * @param leaguers 成员数组
     * @param msgId 消息ID
     * @param sendTime 发送时间
     */
    public void  setConsumerMsgToRedis(List leaguers, String msgId, String sendTime){
        Map map = new HashMap();

        map.put("list",leaguers);
        map.put("msg_id",msgId);
        map.put("send_time",sendTime);
        msgSend.sendMsg(Constant.QUEUE_CONSUMER_HANDLER,map);
    }

    @Override
    public void delWaitMsgToRedis(String msgId) {
        Map map = new HashMap();
        map.put("msg_id",msgId);

        msgSend.sendMsg(Constant.QUEUE_WAIT_DEL,map);
    }

    /**
     *
     * @param msgId
     * @param compare
     */
    public void  setMsgExpireToRedis(String msgId, int compare){
        Map map = new HashMap();
        map.put("msg_id",msgId);
        map.put("compare", compare);
        msgSend.sendMsg(Constant.QUEUE_WAIT_EXPIRE,map);
    }
}

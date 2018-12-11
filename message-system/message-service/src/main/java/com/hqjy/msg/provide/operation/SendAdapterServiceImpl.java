package com.hqjy.msg.provide.operation;

import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.provide.MessageRunService;
import com.hqjy.msg.provide.MsgSend;
import com.hqjy.msg.provide.SendAdapterService;
import com.hqjy.msg.provide.interfaces.MessageTypeInterface;
import com.hqjy.msg.util.Constant;
import com.hqjy.msg.util.DateUtils;
import com.hqjy.msg.util.StringUtils;
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
public class SendAdapterServiceImpl implements SendAdapterService {

    @Autowired
    @Qualifier("sendHaveNotAck")
    private MsgSend msgSend;

    /**
     * 写入到待消费队列
     *
     * @param msgId
     * @param sendTime
     * @param nowDate
     */
    public void setWaitMsg(String msgId, String sendTime, Date nowDate,MsgMessage message,String msg) {
        Date sendDate = DateUtils.stringToDate(sendTime, DateUtils.DATE_FORMAT);
        Map map = new HashMap();
        map.put("msg_id", msgId);
        map.put("compare", DateUtils.compare(nowDate, sendDate));
        map.put("message", StringUtils.objToJsonStr(message));
        map.put("msg", msg);
        msgSend.sendMsg(Constant.QUEUE_WAIT, map);
    }

    /**
     * 设置已读信息（Redis）
     *
     * @param msgIds
     * @param userId
     */
    public void setReadedConsumerMsgToRedis(List msgIds, String userId) {
        Map map = new HashMap();
        map.put("msg_id", StringUtils.objToJsonStr(msgIds));
        map.put("user_id", userId);
        msgSend.sendMsg(Constant.QUEUE_CONSUMER_READED_REDIS, map);
        //msgSend.sendMsg(Constant.QUEUE_CONSUMER_READED_DB, map);
    }

    @Override
    public void setSendMsgToRedis(List leaguers, MsgMessage message, String msg) {
        Map map = new HashMap();
        String result = StringUtils.objToJsonStr(message);
        map.put("message", result);
        map.put("leaguers", leaguers);
        map.put("msg", msg);
        msgSend.sendMsg(Constant.QUEUE_SEND_MSG, map);

    }

    /**
     * 消息体写入到Redis
     *
     * @param message
     */
    public void setMsgToRedis(MsgMessage message, String msg) {
        Map map = new HashMap();
        map.put("message", StringUtils.objToJsonStr(message));
        map.put("msg", msg);
        //msgSend.sendMsg(Constant.QUEUE_MSG,map);
        //String result = StringUtils.objToJsonStr(message);
        //System.out.println(result);
        msgSend.sendMsg(Constant.QUEUE_MSG, map);

    }

    /**
     * 消息体写入到Redis
     *
     * @param message
     */
    public void setMsgDetailToRedis(MsgMessage message, String msg) {
        Map map = new HashMap();
        map.put("message", StringUtils.objToJsonStr(message));
        map.put("msg", msg);
        //msgSend.sendMsg(Constant.QUEUE_MSG,map);
        //String result = StringUtils.objToJsonStr(message);
        //System.out.println(result);
        msgSend.sendMsg(Constant.QUEUE_MSG_DETAIL, map);

    }

    /**
     * 把消息写入每个用户的消息列表里
     *
     * @param leaguers 成员数组
     * @param msgId    消息ID
     * @param sendTime 发送时间
     */
    public void setConsumerMsgToRedis(List leaguers, String msgId, String sendTime) {
        Map map = new HashMap();

        map.put("list", leaguers);
        map.put("msg_id", msgId);
        map.put("send_time", sendTime);
        msgSend.sendMsg(Constant.QUEUE_CONSUMER_HANDLER, map);
    }

    @Override
    public void delWaitMsgToRedis(String msgId) {
        Map map = new HashMap();
        map.put("msg_id", msgId);

        msgSend.sendMsg(Constant.QUEUE_WAIT_DEL, map);
    }

    /**
     * @param msgId
     * @param compare
     */
    public void setMsgExpireToRedis(String msgId, int compare) {
        Map map = new HashMap();
        map.put("msg_id", msgId);
        map.put("compare", compare);
        msgSend.sendMsg(Constant.QUEUE_WAIT_EXPIRE, map);
    }

    @Override
    public void setMsgToMysql(String msgId) {
        Map map = new HashMap();

        map.put("msg_id", msgId);
        msgSend.sendMsg(Constant.QUEUE_SYNC_REDIS_TO_MYSQL, map);
    }

    @Override
    public void setMsgGroupInfoToRedis(String channel, List groups) {
        Map map = new HashMap();
        map.put("groups", StringUtils.objToJsonStr(groups));
        map.put("channel", channel);
        msgSend.sendMsg(Constant.QUEUE_UPDATE_GROUP_RECORD, map);
    }

    @Override
    public void setUpdateMsgGroupInfo(String userId, List groups) {
        Map map = new HashMap();
        map.put("groups", StringUtils.objToJsonStr(groups));
        map.put("user_id", userId);
        msgSend.sendMsg(Constant.QUEUE_UPDATE_GROUP, map);
    }
}

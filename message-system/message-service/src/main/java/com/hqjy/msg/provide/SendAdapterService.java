package com.hqjy.msg.provide;

import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.provide.interfaces.MessageTypeInterface;

import java.util.Date;
import java.util.List;

/**
 * Created by baobao on 2017/12/29 0029.
 */
public interface SendAdapterService {

    /**
     *
     * @param msgId
     */
    public void  delWaitMsgToRedis(String msgId);

    /**
     *
     * @param msgId
     * @param compare
     */
    public void  setMsgExpireToRedis(String msgId, int compare);

    /**
     *  缓存消息体（Redis）
     * @param message
     */
    public void setMsgToRedis(MsgMessage message,String msg);

    public void setMsgDetailToRedis(MsgMessage message,String msg);


    /**
     * 缓存消息到消费者的列表（Redis）
     * @param leaguers
     * @param msgId
     * @param sendTime
     */
    public void setConsumerMsgToRedis(List leaguers, String msgId, String sendTime);

    /**
     * 写入到待消费队列
     * @param msgId
     * @param sendTime
     * @param nowDate
     */
    public  void setWaitMsg(String msgId, String sendTime, Date nowDate,MsgMessage message,String msg);

    /**
     * 设置已读信息（Redis）
     * @param msgIds
     * @param userId
     */
    public void setReadedConsumerMsgToRedis(List msgIds, String userId);


    public void setSendMsgToRedis(List leaguers,MsgMessage message,String msg);

    public void setMsgToMysql(String msgId);

    public void setUpdateMsgGroupInfo(String userId, List groups);

    public void setMsgGroupInfoToRedis(String channel,List groups);
}

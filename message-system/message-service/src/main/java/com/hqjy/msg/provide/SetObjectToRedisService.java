package com.hqjy.msg.provide;

import java.util.Date;
import java.util.List;

/**
 * Created by baobao on 2017/12/29 0029.
 */
public interface SetObjectToRedisService {

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
     * @param msgId
     * @param msg
     */
    public void setMsgToRedis(String msgId,String msg);


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
    public  void setWaitMsg(String msgId, String sendTime, Date nowDate);

    /**
     * 设置已读信息（Redis）
     * @param msgId
     * @param userId
     */
    public void setReadedConsumerMsgToRedis(String msgId, String userId);
}

package com.hqjy.msg.provide;

import java.util.List;

/**
 * Created by baobao on 2017/12/29 0029.
 */
public interface GetObjectFromRedisService {
    /**
     *  缓存消息体（Redis）
     * @param msgId
     * @return
     */
    public Object getMsgFromRedis(String msgId);


    /**
     * 获取缓存消息到消费者的列表（Redis）
     * @param userId
     * @param startTime
     * @param sendTime
     * @return
     */
    public List getConsumerMsgFromRedis(String userId, String startTime, String endTime);


}

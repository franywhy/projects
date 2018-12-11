package com.hqjy.msg.provide;

import java.util.Date;
import java.util.List;

/**
 * Created by baobao on 2017/12/29 0029.
 */
public interface RemoveObjectToRedisService {
    /**
     *  移除用户的消息（Redis）
     * @param msgId
     * @param list
     */
    public void removeMsgToRedis(String msgId, List list);



}

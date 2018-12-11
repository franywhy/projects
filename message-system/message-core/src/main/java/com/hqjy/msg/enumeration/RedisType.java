package com.hqjy.msg.enumeration;

/**
 * Created by Administrator on 2017/12/21 0021.
 */
public enum  RedisType {
    WAIT_REDIS("waitTemplate"),MESSAGE_REDIS("msgTemplate"),USER_REDIS("redisTemplate"),CHANNEL_REDIS("channelTemplate");
    private String value;
    RedisType(String s) {
        this.value = s;
    }
    public String getValue() {
        return value;
    }
}

package com.hqjy.msg.provide;

/**
 * Created by Administrator on 2018/2/1 0001.
 */
public interface AyncRedisToDbService {

    public void syncMsg();

    public void syncChannel();

    public void syncReadedMsg();

    public void syncDetail();
}

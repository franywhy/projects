package com.hq.learningapi.service.impl;

import com.hq.common.interfaze.AbstractRedisService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RedisServiceImpl extends AbstractRedisService {


    public String buildKValue(String uid,String keyValue,Long expiredTime) {
        //用#将liveId,uid和毫秒级时间戳拼接成k值原码
        Long timestamp = new Date().getTime();
        String keyStr = uid + "#" + timestamp;
        //用md5加密后得到k值
        String k = DigestUtils.md5Hex(keyStr);
        String mapKey = keyValue + k;
        //保存k值到redis
        /*this.putHashCache("k_value:"+k,"uid",uid);
        this.putHashCache("k_value:"+k,"timestamp",timestamp);*/
        this.pushValueCache(mapKey, timestamp, expiredTime);

        return k;
    }

    public String verifyGenseeKey(String mapKey,Long expiredTime){
        Long timestamp = this.getValueCache(mapKey, Long.class);
        if (null == timestamp
                || timestamp + expiredTime < System.currentTimeMillis()) {
            return "fail";
        }
        else {
            this.removeCache(mapKey);
            return "pass";
        }
    }

    public String getAvatar(String mapToken){
        String avatar = this.getHashCache(mapToken,"avatar",String.class);
        return avatar;
    }

    public String getUserId(String mapToken){
        String userId = this.getHashCache(mapToken, "userId", String.class);
        return userId;
    }

}

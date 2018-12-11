package com.elise.userinfocenter.service.impl;

import com.hq.common.interfaze.AbstractRedisService;
import com.elise.userinfocenter.entity.LocalConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaptchaServiceImpl extends AbstractRedisService {

    @Autowired
    private LocalConfigProperties config;

    public void saveCaptchaCode(String schoolId, String sessionId,String code){
        this.pushValueCache(getRedisKey(schoolId,sessionId),code,config.getCaptchaTimeout());
    }


    public String loadCaptchaCode(String schoolId, String sessionId){
         return  this.getValueCache(getRedisKey(schoolId,sessionId),String.class);
    }

    public void clear(String schoolId, String sessionId){
        this.removeCache(getRedisKey(schoolId,sessionId));
    }

    private String getRedisKey (String schoolId, String sessionId){
        StringBuilder sb = new StringBuilder();
        sb.append("captcha:");
        sb.append(schoolId);
        sb.append(":");
        sb.append(sessionId);
        return sb.toString();
    }

}

package com.elise.userinfocenter.service.impl;

import com.hq.common.interfaze.AbstractRedisService;
import org.springframework.stereotype.Service;

@Service
public class QuickResLoginServiceImpl extends AbstractRedisService {

    public void saveUUID(String uuid, String redirectUrl, String schoolId) {
        String key = generateKey(schoolId, uuid);
        this.putHashCache(key, "redirectUrl", redirectUrl, 60000);
    }

    public Boolean saveToken(String uuid, String token, String schoolId) {
        String key = generateKey(schoolId, uuid);
        if(loadUrl(uuid,schoolId) == null){
            return false;
        }else{
            this.putHashCache(key, "token", token, 60000);
            return true;
        }
    }

    public String loadToken(String uuid, String schoolId){
       return this.getHashCache(generateKey(schoolId, uuid),"token",String.class);
    }

    public String loadUrl(String uuid, String schoolId){
        return this.getHashCache(generateKey(schoolId, uuid),"redirectUrl",String.class);
    }

    public void clear(String uuid, String schoolId) {
        this.removeCache(generateKey(schoolId, uuid));
    }

    private String generateKey(String schoolId, String uuid) {
        StringBuilder sb = new StringBuilder();
        sb.append("qrLogin:");
        sb.append(schoolId);
        sb.append(":");
        sb.append(uuid);
        return sb.toString();
    }

}

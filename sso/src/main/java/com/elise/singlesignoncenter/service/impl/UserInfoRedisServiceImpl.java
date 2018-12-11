package com.elise.singlesignoncenter.service.impl;

import com.elise.singlesignoncenter.config.LocalConfigEntity;
import com.elise.singlesignoncenter.entity.UserInfoEntity;
import com.elise.singlesignoncenter.token.TokenRedisKeyUtil;
import com.hq.common.enumeration.ClientTypeEnum;
import com.hq.common.interfaze.AbstractRedisService;
import org.springframework.stereotype.Service;

import java.util.HashMap;


/**
 * Created by Glenn on 2017/4/26 0026.
 */

@Service("UserInfoRedisServiceImpl")
public class UserInfoRedisServiceImpl extends AbstractRedisService {

    public void UpdateUserInfo(Integer userId,String businessId,String avatar,String nickName,String email,Integer gender,LocalConfigEntity config){
        String mapToken = TokenRedisKeyUtil.getRedisMapKey(businessId,userId);
        if (avatar != null) {
            this.putHashCache(mapToken, "avatar", avatar, config.getTokenExpiredTime());
        }
        if (email != null) {
            this.putHashCache(mapToken, "email", email, config.getTokenExpiredTime());
        }
        if (nickName != null) {
            this.putHashCache(mapToken, "nickName", nickName, config.getTokenExpiredTime());
        }
        if (gender != -1) {
            this.putHashCache(mapToken, "gender", String.valueOf(gender), config.getTokenExpiredTime());
        }
    }

    public void removeOldInfo(Integer userId,String businessId,ClientTypeEnum clientTypeEnum){
        this.removeCache(TokenRedisKeyUtil.getUserId2TokenKey(clientTypeEnum,businessId,userId));
        this.removeCache(TokenRedisKeyUtil.getRedisMapKey(businessId,userId));
    }

    public void storeInfo(String token, String businessId, ClientTypeEnum clientTypeEnum, UserInfoEntity user, LocalConfigEntity config){
        this.pushValueCache(TokenRedisKeyUtil.getUserId2TokenKey(clientTypeEnum,businessId,user.getUserId()),token,config.getTokenExpiredTime());
        //UserInfo
        HashMap<Object,Object> userInfo = new HashMap<Object, Object>();
        userInfo.put("avatar", user.getAvatar());
        userInfo.put("email", user.getEmail());
        userInfo.put("mobile", user.getMobile());
        userInfo.put("nickName", user.getNickName());
        userInfo.put("gender", String.valueOf(user.getSex()));
        userInfo.put("status", String.valueOf(user.getStatus()));
        String mapToken = TokenRedisKeyUtil.getRedisMapKey(businessId,user.getUserId());
        this.putHashCache(mapToken,userInfo,config.getTokenExpiredTime());
    }

    public String getTokenByUserId(String businessId,ClientTypeEnum clientTypeEnum,Integer userId){
        String oldToken = this.getValueCache(TokenRedisKeyUtil.getUserId2TokenKey(clientTypeEnum,businessId,userId),String.class);
        return oldToken;
    }

    public String getMobileNo(Integer userId,String businessId){
        String mapToken = TokenRedisKeyUtil.getRedisMapKey(businessId,userId);
        return this.getHashCache(mapToken, "mobile", String.class);
    }

    public Integer getGender(Integer userId,String businessId){
        String mapToken = TokenRedisKeyUtil.getRedisMapKey(businessId,userId);
        String gender = this.getHashCache(mapToken, "gender", String.class);
        return gender == null ? 2:Integer.parseInt(gender);
    }

    public String getNickName(Integer userId,String businessId){
        String mapToken = TokenRedisKeyUtil.getRedisMapKey(businessId,userId);
        return this.getHashCache(mapToken, "nickName", String.class);
    }

    public String getEmail(Integer userId,String businessId){
        String mapToken = TokenRedisKeyUtil.getRedisMapKey(businessId,userId);
        return this.getHashCache(mapToken, "email", String.class);
    }

    public String getAvatar(Integer userId,String businessId){
        String mapToken = TokenRedisKeyUtil.getRedisMapKey(businessId,userId);
        return this.getHashCache(mapToken, "avatar", String.class);
    }
}

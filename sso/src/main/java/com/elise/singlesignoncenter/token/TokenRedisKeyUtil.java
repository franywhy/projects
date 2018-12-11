package com.elise.singlesignoncenter.token;


import com.hq.common.enumeration.ClientTypeEnum;

/**
 * Created by Glenn on 2017/4/26 0026.
 */


public class TokenRedisKeyUtil {

    private static final String TOKEN_PREFIX = "userInfo:";
    private static final String USER_ID_2_TOKEN = "userId2Token:";
    private static final String PC="pc:";
    private static final String MOBILE_CLIENT="mobile:";
    private static final String USER_INFO_NAME_SPACE = "sso:";

    public static String getUserId2TokenKey(ClientTypeEnum type, String businessId, Integer userId){
        StringBuilder sb = generateBusinessPrefix(type,businessId);
        sb.append(USER_ID_2_TOKEN);
        sb.append(userId);
        return sb.toString();
    }

    public static String getRedisMapKey(String businessId, Integer userId) {
        StringBuilder sb = new StringBuilder();
        sb.append(USER_INFO_NAME_SPACE);
        sb.append(businessId);
        sb.append(":");
        sb.append(TOKEN_PREFIX);
        sb.append(userId);
        return sb.toString();
    }

    private static StringBuilder generateBusinessPrefix(ClientTypeEnum type, String businessId){
        StringBuilder sb = new StringBuilder();
        sb.append(USER_INFO_NAME_SPACE);
        sb.append(businessId).append(":");
        if(type == ClientTypeEnum.WEB){
            sb.append(PC);
        }else{
            sb.append(MOBILE_CLIENT);
        }
        return sb;
    }
}

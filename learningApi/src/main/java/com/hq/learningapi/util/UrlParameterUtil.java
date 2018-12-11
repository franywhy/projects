package com.hq.learningapi.util;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by ShanYaofeng on 2017/6/1 0001.
 */
public class UrlParameterUtil {


    public static String spliceUrl(Map<String, Object> parameters,String url) {

        return url + spliceParameter(parameters);
   }

    /**
     * 公开课回放地址
     * @param vid 视频id
     * @param userid 用户id
     * @param token
     * @param url 回放地址
     * @return
     */
    public static String oliveReplayUrl(String vid, String userid, String token,String url) {
        Map<String,Object> parameters = new HashMap<String, Object>();
        parameters.put("vid", "a647f95e6e81bd9498d89dd8ab08017a_a");
        parameters.put("userid", userid);
        parameters.put("token", token);
        return url + spliceParameter(parameters);
    }

    /**
     * 直播回放地址
     * @param vid 视频id
     * @param userid 用户id
     * @param token
     * @param classPlanLiveId 排课子表id
     * @param url 回放地址
     * @return
     */
    public static String replayUrl(String vid, String userid, String token, String classPlanLiveId,String url){
        Map<String,Object> parameters = new HashMap<String, Object>();
        parameters.put("vid", "a647f95e6e81bd9498d89dd8ab08017a_a");
        parameters.put("userid", userid);
        parameters.put("token", token);
        parameters.put("classplanLiveId", classPlanLiveId);
        return url + spliceParameter(parameters);
    }

    /**
     * 展示互动直播回放地址
     * @param userid 用户id
     * @param nickname 用户昵称
     * @param k 验证值
     * @param url 回放地址
     * @return
     */
    public static String genseeReplayUrl(String userid, String nickname, String k,String url){
        Map<String,Object> parameters = new HashMap<String, Object>();
        //避免与gensee的系统用户冲突,将userid格式化为1000000000以后
        Long uid = 1000000000L + Long.parseLong(userid);
        parameters.put("uid", uid);
        parameters.put("nickName", nickname);
        parameters.put("k", k);
        return url + spliceParameter(parameters);
    }

    private static String spliceParameter(Map<String, Object> parameters){
        StringBuilder parameterStr = new StringBuilder();
        parameterStr.append("?");
        for (Map.Entry<String, Object> entry:parameters.entrySet()) {
            parameterStr.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        return  parameterStr.deleteCharAt(parameterStr.length()-1).toString();
    }
}

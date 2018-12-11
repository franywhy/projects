package com.hq.learningapi.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Value;

import com.hq.learningapi.util.http.HttpClientUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SendChannelIdUtil {


	public static void sendChannelId(String msgHost,Long userId , String channelId , Integer type){
		Map<String , String> map = new HashMap<>();
		
        JSONObject user = new JSONObject();
        user.put("channel", channelId);
        user.put("type", type);
        
        JSONArray userArray=new JSONArray();
        userArray.add(user);
        
        map.put("user_id", ""+userId);
    	map.put("groups", userArray.toString());
		
        System.out.println(msgHost+"/group/updateMsgGroup");
        String  result = HttpClientUtil.getInstance().sendHttpPost(msgHost+"/group/updateMsgGroup",map);
        System.out.println(result);
	}
	
	public static void sendGroupChannelId(String msgHost,List<Long> userIdList , String channelId , Integer type){
		Map<String , String> map = new HashMap<>();
		
        /*JSONObject user = new JSONObject();
        user.put("channel", channelId);
        user.put("type", type);
        
        JSONArray userArray=new JSONArray();
        userArray.add(user);
        
        map.put("user_id", ""+userIdList);
    	map.put("groups", userArray.toString());*/
		
		JSONObject user = new JSONObject();
		JSONArray userArray=new JSONArray();
		for(Long userId : userIdList){	
			user.put("user_id", ""+userId);
			user.put("type", type);
			userArray.add(user);
		}
        
        map.put("channel", channelId);
    	map.put("user_ids", userArray.toString());
        System.out.println(msgHost+"/group/updateMsgGroupByChannel");
        String  result = HttpClientUtil.getInstance().sendHttpPost(msgHost+"/group/updateMsgGroupByChannel",map);
        System.out.println(result);
	}
}

package com.elise.singlesignoncenter.util;

import java.util.HashMap;
import java.util.Map;

import com.elise.singlesignoncenter.util.http.HttpClientUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author Created by LiuHai 2018/02/05
 *
 */
public class SendChannelIdUtil {
	public static void sendChannelId(String msgHost,Integer userId , String channelId , Integer type){
		Map<String , String> map = new HashMap<>();
		
        JSONObject user = new JSONObject();
        user.put("channel", channelId);
        user.put("type", type);
        
        JSONArray userArray=new JSONArray();
        userArray.add(user);
        
        map.put("user_id", ""+userId);
    	map.put("groups", userArray.toString());
    	HttpClientUtil.getInstance().sendHttpPost(msgHost+"/group/updateMsgGroup",map);
	}
}

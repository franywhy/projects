package com.izhubo.userSystem.web;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.izhubo.rest.common.util.MsgDigestUtil;
import com.izhubo.userSystem.utils.JSONUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Controller
public class T3UserController extends MultiActionController{
	
	private static Logger logger = LoggerFactory
			.getLogger(T3UserController.class);

	
	public static final String RAN_TOKEN_SEED = "#@#qq"+ 
			DateFormatUtils.format(System.currentTimeMillis(), "yMMdd") +"%xi>YY";
	
	@Resource
	protected MongoTemplate t3Mongo;

	@Resource
	protected StringRedisTemplate mainRedis;
	
	public String getNamespace(String via, String t3pf){
		String ns = "un-";
		
		if(("aphone".equals(via) || "apad".equals(via)) && "mqq".equals(t3pf) ){
			ns = "q3-";
		}
		else if(("aphone".equals(via) || "apad".equals(via)) && "mwb".equals(t3pf)){
			ns = "w1-";
		}
		else if(("iphone".equals(via) || "ipad".equals(via)) && "mqq".equals(t3pf)){
			ns = "q4-";
		}
		else if(("iphone".equals(via) || "ipad".equals(via)) && "mwb".equals(t3pf)){
			ns = "w2-";
		}
		
		return ns;
	}
	
	public Map<String, Object> login(HttpServletRequest req,
			HttpServletResponse res) {
	
		Map<String, Object> map = new HashMap<String, Object>();
		
		String userid = req.getParameter("userid");
		String usertoken = req.getParameter("usertoken");
		String via = req.getParameter("via");
		String t3pf = req.getParameter("t3pf");
		
		String picurl = req.getParameter("picurl");
		String nickName = req.getParameter("nickname");
		
		t3pf = "null".equals(t3pf) ? null : t3pf;
		
		DBObject dbo = null;
		
		if( (dbo = t3Mongo.getCollection("t3user").findOne(new BasicDBObject("username", userid))) == null){
			
			
			//触发新增用户环节
			Map<String, Object> userMap = new HashMap<>();
			userMap.put("username", userid);
			userMap.put("usertoken", usertoken);
			userMap.put("via", via);
			userMap.put("t3pf", t3pf);
			userMap.put("picurl", picurl);
			userMap.put("nickName", nickName);
			userMap.put("tuid", UUID.randomUUID().toString());
			t3Mongo.getCollection("t3user").save(new BasicDBObject(userMap));
			
			
			String access_token = getNamespace(via, t3pf) + MsgDigestUtil.MD5.digest2HEX(RAN_TOKEN_SEED 
					+ userid + Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
			
			
			
			//如果之前有access_token，并且不相等，找到并删除掉，
			if(mainRedis.opsForHash().hasKey(LoginController.USERNAME_TO_ACCESS_TOKEN_KEY, userid)){
				String oldToken = (String)mainRedis.opsForHash().get(LoginController.USERNAME_TO_ACCESS_TOKEN_KEY, userid);
				if (!access_token.equals(oldToken)) {
					mainRedis.opsForHash().delete(LoginController.ACCESS_TOKEN_TO_USERNAME_KEY, oldToken);
				}
				
			}
			//写入username_to_access_token
			mainRedis.opsForHash().put(LoginController.USERNAME_TO_ACCESS_TOKEN_KEY, userid, access_token);
			//写入access_token_to_username
			mainRedis.opsForHash().put(LoginController.ACCESS_TOKEN_TO_USERNAME_KEY, access_token, userid);
			
			
			map.put("code", "1");
			map.put("msg", "OK");
			Map<String, Object> data = new HashMap<>();
			data.put("access_token", access_token);
			map.put("data", data);
		}
		else {
			
//			//检查密码是否正确
//			if(!usertoken.equals(dbo.get("usertoken"))){
//				map.put("code", "30302");
//			}
//			//用户存在，并且正确
//			else {
				
			//不检查密码直接更新
				//更新的属性参数  picurl nickName
				Map<String, Object> userMap = new HashMap<>();
				userMap.put("picurl", picurl);
				userMap.put("nickName", nickName);
				userMap.put("usertoken", usertoken);
				t3Mongo.getCollection("t3user").update(new BasicDBObject("username", userid), new BasicDBObject("$set", new BasicDBObject(userMap)));
				
				String access_token = getNamespace(via, t3pf) + MsgDigestUtil.MD5.digest2HEX(RAN_TOKEN_SEED 
						+ userid + Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
				
				
				//如果之前有access_token，并且不相等，找到并删除掉，
				if(mainRedis.opsForHash().hasKey(LoginController.USERNAME_TO_ACCESS_TOKEN_KEY, userid)){
					String oldToken = (String)mainRedis.opsForHash().get(LoginController.USERNAME_TO_ACCESS_TOKEN_KEY, userid);
					if (!access_token.equals(oldToken)) {
						mainRedis.opsForHash().delete(LoginController.ACCESS_TOKEN_TO_USERNAME_KEY, oldToken);
					}
					
				}
				//写入username_to_access_token
				mainRedis.opsForHash().put(LoginController.USERNAME_TO_ACCESS_TOKEN_KEY, userid, access_token);
				//写入access_token_to_username
				mainRedis.opsForHash().put(LoginController.ACCESS_TOKEN_TO_USERNAME_KEY, access_token, userid);
				
				map.put("code", "1");
				map.put("msg", "OK");
				Map<String, Object> data = new HashMap<>();
				data.put("access_token", access_token);
				map.put("data", data);
				
//			}
			
			
			
			
			//正确，返回access_token
		}            
		
		return map;
	}
	
	
	public Map<String, Object> info(HttpServletRequest req,
			HttpServletResponse res) {
	
		Map<String, Object> map = new HashMap<String, Object>();

//		Map<String, Object> map1 = new HashMap<String, Object>();
//		map.put("data", map1);

		// 获得access_token
		String access_token = req.getParameter("access_token");
		if (StringUtils.isBlank(access_token)) {
			logger.info("access_token is blank: {}", access_token);
			map.put("code", "30419");
			return map;
		}

		// 获得username (access_token如何保护？ 不能每次调用这个都返回正确的)
		String username = (String) mainRedis.opsForHash().get(
				LoginController.ACCESS_TOKEN_TO_USERNAME_KEY, access_token);
		if(StringUtils.isBlank(username)){
			logger.info("username not found in redis, clean token, is blank: username: {}, access_token: {}", 
					username, 
					access_token);
			
			map.put("code", "30419");
			//清空token缓存
			mainRedis.delete("token:" + access_token);
			return map;
		}
		DBObject dbo = t3Mongo.getCollection("t3user").findOne(new BasicDBObject("username", username));
		if(dbo == null){
			logger.info("username not found in mongo, clean token, username: {}", username);
			map.put("code", "30419");
			mainRedis.delete("token:" + access_token);
			return map;
		}

		//去掉关键数据
		dbo.removeField("usertoken");
		
		Map<String, Object> map1 = JSONUtil.beanToMap(dbo);
		//注意转换，确保有数据
		map1.put("pic", dbo.get("pic"));
		map1.put("nickName", dbo.get("nickName"));
		
		map.put("data", map1);
		
		return map;
	}
	
	public static void main(String[] args) {
		System.out.println("out: " + UUID.randomUUID().toString());
	}
}

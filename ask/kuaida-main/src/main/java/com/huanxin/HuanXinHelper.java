package com.huanxin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;

import com.izhubo.rest.common.util.JSONUtil;
import com.izhubo.rest.common.util.MsgDigestUtil;
import com.izhubo.rest.common.util.http.HttpClientUtil;
import com.izhubo.rest.common.util.http.HttpClientUtil4_3;


public class HuanXinHelper {
	
	private static final String HUANXIN_ACCESS_TOKEN_KEY = "HuanXin:TOKEN";


	private static final String APPNAME = "comkjcityanswer";
	
	
	private static final String HTTP_HOST ="http://a1.easemob.com/";


	private static final String ORG_NAME = "kjcity";
	
	private static final String SALT = "dea0e34ce8";
	
	
	
	protected StringRedisTemplate mainRedis;
	
	//获取token
	public static String GetToken(StringRedisTemplate mainRedis)
	{
		//尝试从redis中获取，如果不存在则重新请求
		
		if( mainRedis.opsForValue().get(HUANXIN_ACCESS_TOKEN_KEY)!=null)
		{
			String Token = (String)mainRedis.opsForValue().get(HUANXIN_ACCESS_TOKEN_KEY);
			
			return Token;
		}
		else
		{
	
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("grant_type", "client_credentials");
		params.put("client_id", "YXA6uaPHMBrWEeWhbSn2ECulxQ");
		params.put("client_secret", "YXA6wke6bRhsi_oLgGweY8ht-UCWors");
		
		String json = JSONUtil.hashMapToJson(params);

		
		
		
		String url = HTTP_HOST+ORG_NAME+"/"+APPNAME+"/token";
		Map<String, String> headers = new HashMap<String, String>();
		String result = "";
		String access_token = "";
		try {
	
			result = HttpClientUtil4_3.postStr(url, json , headers);
			
			 Map<String, Object> resultMap = JSONUtil.jsonToMap(result);
			  access_token = resultMap.get("access_token").toString();
			 int delaytime = (int) resultMap.get("expires_in");
			 mainRedis.opsForValue().set(HUANXIN_ACCESS_TOKEN_KEY, access_token, delaytime);
			 
			 
			
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	
		return access_token;
		
		}
	
	}
	
	//用户注册的时候调用(注意，传入的必须是MD5之后的密码)
	public static void AddUser(String userName,String MD5Password,StringRedisTemplate mainRedis)
	{
		String Password = ConventPasswordToMD5(MD5Password);
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", userName);
		params.put("password", Password);
	
		
		String url = HTTP_HOST+ORG_NAME+"/"+APPNAME+"/users";
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Bearer "+GetToken(mainRedis));
		String json = JSONUtil.hashMapToJson(params);
		
		
		try {
			HttpClientUtil4_3.postStr(url, json, headers);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//用户修改密码的时候调用
	public static void UpdatePassword(int userId,String NewMD5Password,StringRedisTemplate mainRedis)
	{
		String NewPassword = ConventPasswordToMD5(NewMD5Password);
		Map<String, String> params = new HashMap<String, String>();
		params.put("newpassword", NewPassword);
		String url = HTTP_HOST+ORG_NAME+"/"+APPNAME+"/users/"+String.valueOf(userId)+"/password";
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Bearer "+GetToken(mainRedis));
		try {
			HttpClientUtil4_3.postStr(url, JSONUtil.hashMapToJson(params), headers);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String ConventPasswordToMD5(String OlderPassword)
	{
		
		String md5 = SALT+OlderPassword;
		 
		return MsgDigestUtil.MD5.digest2HEX(md5);
	}

}

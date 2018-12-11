package com.hq.learningapi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hq.learningapi.config.SsoConfigEntity;
import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.util.http.HttpClientUtil4_3;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class SSOTokenUtils {
protected static final Logger logger = LoggerFactory.getLogger(SSOTokenUtils.class);
	
	private static final String SSOTOKEN = "SSOTOKEN";
	
	/**
     * redis 用户信息
     */
    protected static final String USER_INFO = "user_info:";

	@Autowired
	private StringRedisTemplate mainRedis;
	private static SSOTokenUtils ssoTokenUtils;   
	
	@Autowired
	private SsoConfigEntity ssoConfig;
      
    @PostConstruct  
    public void init() {  
    	ssoTokenUtils = this;  
    	ssoTokenUtils.mainRedis = this.mainRedis;  
    }
    
    /** 用户信息url "http://10.0.98.14:8084/inner/userInfo"*/
    private static String ssoUserinfoUrl = "";
	@Value("")
	private void setUserinfoUrl(){
		ssoUserinfoUrl = ssoConfig.getSsoUserinfoUrl();
	}
	
	/** user mobile url*/
	private static String ssoUsermobilenoUrl = "";
	@Value("")
	private void setUsermobilenoUrl(){
		ssoUsermobilenoUrl = ssoConfig.getSsoUsermobilenoUrl();
	}
	
	/** token detail url*/
	private static String ssoUsertokendetailUrl = "";
	@Value("")
	private void setUsertokendetailUrl(){
		ssoUsertokendetailUrl = ssoConfig.getSsoUsertokendetailUrl();
	}

	public static String getBussinessId(String token) {
		try {
			String result = HttpClientUtil4_3.get(ssoTokenUtils.ssoConfig.getSsoGetbussinessidUrl()+"?token="+token,null);
			Map<String, Object> map = JSONUtil.jsonToMap(result);
			if(null != map && 200 == (int)map.get("code")) {
				return (String)map.get("data");
			}
		} catch(Exception ex){
			logger.warn("getBussinessId faile, token={}, Cause: ", token, ex);
		}
		return null;
	}

    public static UserInfoPOJO getUserInfo(HttpServletRequest request, String token){
		String businessId = BusinessIdUtils.getBusinessId(request);
        /*UserInfoPOJO userInfo = (UserInfoPOJO)getFromCache(token);
        if(null == userInfo){*/
            UserInfoPOJO  userInfo = new UserInfoPOJO();
            /*Map<String,Object> result1 = getHttpResult(ssoUsertokendetailUrl,token, businessId);
            if(null != result1){
                userInfo.setUserId(((Integer)result1.get("userId")).longValue());
            }*/
            Map<String,Object> result2 = getHttpResult(ssoUsermobilenoUrl,token, businessId);
            if(null != result2){
                userInfo.setMobileNo((String)result2.get("mobileNo"));
            }
            Map<String,Object> result3 = getHttpResult(ssoUserinfoUrl,token, businessId);
            if(null != result3){
                userInfo.setAvatar((String)result3.get("avatar"));
                userInfo.setGender((Integer)result3.get("gender"));
                userInfo.setEmail((String)result3.get("email"));
                userInfo.setNickName((String)result3.get("nickName"));
                userInfo.setUserId(Long.valueOf((Integer)result3.get("uid")));
            }
         /*   setToCache(token, 7200, userInfo);
        }*/
        return userInfo;
	}
    
    /**
     * 根据key从缓存中获取用户信息。
     * @param key 缓存数据对应的key
     * @return 缓存数据，如果无法获取或数据已过期则返回null
     */
    protected static Object getFromCache(String key) {
    	try {
        	String json = ssoTokenUtils.mainRedis.opsForValue().get(USER_INFO+key);
        	if(StringUtils.isNotBlank(json)){
        		return JSONUtil.jsonToBean(json, UserInfoPOJO.class);
        	}
        } catch (Exception e) {
            logger.warn("从缓存中获取数据时发生异常, key={}, Cause: ", key, e);
            return null;
        }
        return null;
    }
    
    /**
     * 把用户数信息放在缓存中，同时设置缓存数据的过期时间。
     * @param key 此数据对应的key
     * @param exp 过期时间，单位：秒
     * @param obj 要存放在缓存中的数据
     */
    protected static void setToCache(String key, int exp, UserInfoPOJO obj) {
        if ((null != obj) && (obj instanceof java.io.Serializable)) {
            try {
            	ObjectMapper mapper = new ObjectMapper(); 
        		String jsonStr = "";
        		jsonStr = mapper.writeValueAsString(obj);
        		ssoTokenUtils.mainRedis.opsForValue().set(USER_INFO+key, jsonStr, exp, TimeUnit.SECONDS);
            } catch (Exception e) {
                logger.warn("把数据存放在缓存中时发生异常, key={}, Cause: ", key, e);
            }
        }
    }
    
    public static Map<String,Object> getHttpResult(String url, String token, String businessId){
    	String result = "";
    	try{
    		Map<String , String> headerMap = new HashMap<>();
        	headerMap.put(BusinessIdUtils.HTTP_HEADER_BUSINESS_ID(), businessId);
        	result = HttpClientUtil4_3.get(url + "?token=" + token, headerMap);
        }catch(Exception ex){
        	logger.warn("getUserInfo faile, token={}, Cause: ", token, ex);
        }
    	
    	if(!result.equals("")){
			Map<String,Object> resultMap = JSONUtil.jsonToMap(result);
			
			if(null != resultMap){
				Integer code = (Integer) resultMap.get("code");
				if(code == 200){
					return (Map<String,Object>)resultMap.get("data");
				}
			}
        }
    	
    	return null;
    }
}

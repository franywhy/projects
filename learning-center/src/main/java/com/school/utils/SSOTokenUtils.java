package com.school.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import com.school.pojo.UserInfoPOJO;
import com.school.utils.http.HttpClientUtil4_3;


/**
 * sso token 工具类
 * @class com.school.util.SSOTokenUtils.java
 * @Description:
 * @author shihongjie
 * @dete 2017年8月31日
 */
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
      
    @PostConstruct  
    public void init() {  
    	ssoTokenUtils = this;  
    	ssoTokenUtils.mainRedis = this.mainRedis;  
  
    }
	
	/** 用户信息url*/
    private static String userInfoUrl = "";
	@Value("#{application['pom.sso.userInfo.url']}")
	private void setUrl(String url){
		userInfoUrl = url;
	}
	
	/** token detail url*/
    private static String tokenDetailUrl = "";
	@Value("#{application['pom.sso.userTokenDetail.url']}")
	private void setDetailUrl(String url){
		tokenDetailUrl = url;
	}
	
	/** user mobile url*/
    private static String userMobileUrl = "";
	@Value("#{application['pom.sso.userMobileNo.url']}")
	private void setMobileUrl(String url){
		userMobileUrl = url;
	}
	
	/**
	 * 获取token
	 * @param request
	 * @return
	 */
	public static String getToken(HttpServletRequest request , HttpServletResponse response) {
		String token = null;
		
		if(StringUtils.isBlank(token)){
			try {
				token = ServletRequestUtils.getStringParameter(request, "token");
				if(StringUtils.isBlank(token)){
					token = ServletRequestUtils.getStringParameter(request, SSOTOKEN);
				}
				if(StringUtils.isNotBlank(token)){
					setCookie(token, response);
				}
			} catch (ServletRequestBindingException e) {
				e.printStackTrace();
			}
		}
		if(StringUtils.isBlank(token)){
			Cookie[] cookies = request.getCookies();
			if(null != cookies){
				for (Cookie cookie : cookies) {
					String name = cookie.getName();
					if (name.equals(SSOTOKEN)) {
						token = cookie.getValue();
						break;
					}
				}
			}
		}
		return token;
	}
//	public static String getToken(HttpServletRequest request , HttpServletResponse response) {
//		String token = null;
//		Cookie[] cookies = request.getCookies();
//		if(null != cookies){
//			for (Cookie cookie : cookies) {
//				String name = cookie.getName();
//				if (name.equals(SSOTOKEN)) {
//					token = cookie.getValue();
//					break;
//				}
//			}
//		}
//		if(StringUtils.isBlank(token)){
//			try {
//				token = ServletRequestUtils.getStringParameter(request, SSOTOKEN);
//				if(StringUtils.isNotBlank(token)){
//					setCookie(token, response);
//				}
//			} catch (ServletRequestBindingException e) {
//				e.printStackTrace();
//			}
//		}
//		return token;
//	}
	
	public static void setCookie(String token , HttpServletResponse response){
		Cookie cookie = new Cookie(SSOTOKEN, token);
		cookie.setPath("/");
		/*cookie.setMaxAge(1800);*/
		cookie.setMaxAge(864000);//10天
		response.addCookie(cookie);
	}
	
	public static UserInfoPOJO getUserInfo(HttpServletRequest request, String token){
		String businessId = BusinessIdUtils.getBusinessId(request);
        UserInfoPOJO userInfo = (UserInfoPOJO)getFromCache(token);
        if(null == userInfo){
            userInfo = new UserInfoPOJO();
            Map<String,Object> result1 = getHttpResult(tokenDetailUrl,token, businessId);
            if(null != result1){
                userInfo.setUserId(((Integer)result1.get("userId")).longValue());
            }
            Map<String,Object> result2 = getHttpResult(userMobileUrl,token, businessId);
            if(null != result2){
                userInfo.setMobileNo((String)result2.get("mobileNo"));
            }
            Map<String,Object> result3 = getHttpResult(userInfoUrl,token, businessId);
            if(null != result3){
                userInfo.setAvatar((String)result3.get("avatar"));
                userInfo.setGender((Integer)result3.get("gender"));
                userInfo.setEmail((String)result3.get("email"));
                userInfo.setNickName((String)result3.get("nickName"));
            }
            setToCache(token, 7200, userInfo);
        }
        return userInfo;
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
     * 根据key从缓存中删除。
     * @param key 缓存数据对应的key
     */
    protected static void deleteFromCache(String key) {
        try {
        	ssoTokenUtils.mainRedis.delete(USER_INFO+key);
        } catch (Exception e) {
            logger.warn("从缓存中删除数据时发生异常, key={}, Cause: ", key, e);
        }
    }
}

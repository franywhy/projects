package com.izhubo.web.interceptor;

import static com.izhubo.rest.common.doc.MongoKey.$setOnInsert;
import static com.izhubo.rest.common.doc.MongoKey._id;
import groovy.transform.CompileStatic;

import java.io.IOException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.HtmlUtils;

import com.izhubo.common.doc.Param;
import com.izhubo.common.util.KeyUtils;
import com.izhubo.model.UserType;
import com.izhubo.rest.common.doc.MongoKey;
import com.izhubo.rest.common.doc.ParamKey;
import com.izhubo.rest.persistent.KGS;
import com.izhubo.rest.web.data.SimpleJsonView;
import com.izhubo.web.api.Web;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;

/**
 * Oauth2 认证登录
 * <p/>
 * date: 12-8-24 下午1:52
 *
 * @author: wubinjie@ak.cc
 */
@CompileStatic
public class OAuth2SimpleInterceptor extends HandlerInterceptorAdapter {

    public void setMainMongo(MongoTemplate mainMongo) {
        this.mainMongo = mainMongo;
    }

    public void setMainRedis(StringRedisTemplate mainRedis) {
        this.mainRedis = mainRedis;
    }

    public void setChatRedis(StringRedisTemplate chatRedis) {
        this.chatRedis = chatRedis;
    }

    public void setUserKGS(KGS userKGS) {
        this.userKGS = userKGS;
    }

    @Resource
    MongoTemplate mainMongo;
    @Resource
    StringRedisTemplate mainRedis;
    @Resource
    StringRedisTemplate chatRedis;
//    @Resource
    KGS userKGS;

    @Resource
    WriteConcern writeConcern;

    static final Logger log = LoggerFactory.getLogger(OAuth2SimpleInterceptor.class);

    private static final ThreadLocal<Map<String, Object>> sessionHolder = new ThreadLocal<Map<String, Object>>();

    public static void setSession(Map<String, String> session)
    {
        sessionHolder.set((Map)session);
    }

    public static Map<String, Object> getSession()
    {
        Map<String, Object> map =  sessionHolder.get() ;

        return map ;
    }

    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        sessionHolder.remove();
    }

    static final Integer ROBOT_MAX = 1023956;
    private static final String GetAccessByUserId = "GetAccessByUserId";

    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ServletException, IOException {

    	log.info("prehandle before token");
        String tokenValue = parseToken(request);
        log.info("prehandle after token tokenValue: {}, url:{}", 
        		tokenValue, request.getRequestURI());


      //String query = "access_token=eb740172939567fe050c7b1b071b8cce&id1=&id2=&uid=865919012359316"
     /* Enumeration enumer = request.getHeaderNames();
        while(enumer.hasMoreElements())
        {
            String a = (String)enumer.nextElement();
            log.info("hearInfo----------->:"+a+":"+request.getHeader(a));
        }*/

      /*  if(StringUtils.isEmpty(tokenValue))
        {
            handleNotAuthorized(request, response, nullToken);
        }
*/
        if (tokenValue != null) {
            String key = KeyUtils.accessToken(tokenValue);
            String clientId = Web.getClientId(request);
            //log.debug(">>>>>>>>>>>>>>>clientId: {}", clientId);
            if (Web.isBanned(clientId)) { // 封掉客户端 uid
                handleNotAuthorized(request, response, banned);
                mainRedis.delete(key);
                log.info("mainRedis.delete(key);");
                return false;
            }
            if(clientId == null)
            { 	
                 throw new IllegalStateException(notAuthorized);   
            }
            Map obj = mainRedis.opsForHash().entries(key);
            log.info("obj: {}", obj);
            // 尝试从用户系统同步
            if (obj.isEmpty())
            {
                try
                {
                    obj = fetchFromUserSystem(tokenValue, clientId, request);
                    if(obj.isEmpty())
                    {
//                        log.error("fetchFromUserSystem:tokenValue------------------>:"+tokenValue);
                        handleNotAuthorized(request, response,notAuthorized_null);
//                        log.info("handleNotAuthorized(request, response,notAuthorized_null);");
                        return false;
                    }
                    String userId = (String) obj.get(MongoKey._id);
                    log.info("userId: {}", userId);
                    if (userId != null) {
                    	
                    	//写redis  userid - > access_token
                    	mainRedis.opsForHash().put(GetAccessByUserId, userId, tokenValue);
                        Integer cid = Integer.valueOf(userId);
                        String status = "true" ;
                        if(null != obj.get("status"))
                        {
                            status = obj.get("status").toString() ;
                        }
                        if (Boolean.TRUE != Boolean.parseBoolean(status))
                        {
                            throw new IllegalStateException(notAllowed);
                        }
//                        log.info("cid{} > ROBOT_MAX?  {}  ", cid, cid > ROBOT_MAX);
                        if (cid > ROBOT_MAX)
                        {
                            Web.day_login(request, cid);
                        }
                    }
                } catch (IllegalStateException e) {
                    handleNotAuthorized(request, response, e.getMessage());
                    log.error("handleNotAuthorized(request, response, e.getMessage());", e);
                    return false;
                } catch (Exception e) {
                    log.error("获取 TTUS session ERROR :", e);
                    e.printStackTrace();
                }
            }
            if (!obj.isEmpty()) {
                sessionHolder.set(obj);
                log.info("sessionHolder.set(obj);");
                
                
                this.updateUserAccessTime();
                
                return true;
            }
        }
        handleNotAuthorized(request, response, notAuthorized);
        log.info("handleNotAuthorized(request, response, notAuthorized);");
        return false;

    }
    
    
    private static final String UserAccessTimeKey = "UserAccessTimeKey";
    /**
     * 更新用户最后访问时间
     */
    public void updateUserAccessTime(){
		Integer usertoken = Web.getCurrentUserId();
//		log.info("usertoken:{}", usertoken);
		if(usertoken == 0){
			return;
		}
		String usertokenStr = usertoken.toString();
		// 获取最后用户的登录时间
		Long userlasttime = System.currentTimeMillis();
		
		// 查询redis是否存在该用户
		if(mainRedis.opsForHash().hasKey(UserAccessTimeKey, usertokenStr)){
			
			// 如果存在， 更新第二个时间userfirsttime_userlasttime
			String val = (String)mainRedis.opsForHash().get(UserAccessTimeKey, usertokenStr);
			String firstTime = val.split("_")[0];
			val = firstTime + "_" + userlasttime;
			mainRedis.opsForHash().put(UserAccessTimeKey, usertokenStr,
					val);
//			log.info("usertoken:{}, val: {}", usertokenStr, val);
		}
		else {
			// 如果不存在 userfirsttime_userlasttime
			Long userfirsttime = System.currentTimeMillis();
			String val = userfirsttime + "_" + userlasttime;
			mainRedis.opsForHash().put(UserAccessTimeKey, usertokenStr,
					val);
//			log.info("usertoken:{}, val: {}", usertokenStr, val);
		}
    }
    
    
   // final String nullToken = "{\"code\":30406,\"msg\":\"ACCESS_TOKEN为NULL\"}";
    final String notAuthorized = "{\"code\":30405,\"msg\":\"ACCESS_TOKEN无效\"}";
    final String notAllowed = "{\"code\":30418,\"msg\":\"账户已禁用\"}";
    final String regTooOften = "{\"code\":30423,\"msg\":\"注册太频繁\"}";
    final String banned = "{\"code\":30421,\"msg\":\"恶意访问，ip，设备被禁\"}";
    final String notAuthorized_null = "{\"code\":30460,\"msg\":\"ACCESS_TOKEN获取用户系统信息为Null\"}";



    protected void handleNotAuthorized(HttpServletRequest request, HttpServletResponse response, String json)
            throws ServletException, IOException {
        String callback = request.getParameter(ParamKey.In.callback);
        if (StringUtils.isNotBlank(callback)) {
            json = callback + '(' + json + ')';
        }
        SimpleJsonView.rennderJson(json, response);
    }

    public static String parseToken(HttpServletRequest request) {

        // bearer type allows a request parameter as well
        String token = request.getParameter(ACCESS_TOKEN);
        if (token == null) {
            token = parseHeaderToken(request);
        }
        String queryString = request.getQueryString();
        if(null == token && StringUtils.isNotBlank(queryString))
        {
            try
            {
                String queryStringParse = null ;
                String [] tmp  = queryString.split("&");
                String access_token =  tmp[0] ;
                String [] tmp2 = access_token.split("=") ;
                queryStringParse = tmp2[1];
                token =  queryStringParse ;
               // log.info("parseToken Parse token----------->:{}",queryStringParse);
            }
            catch(Exception ex)
            {
                log.error("parseToken parse is Exception");
                ex.printStackTrace();
            }
        }
        return token;
    }

    static String ACCESS_TOKEN = "access_token";

    static final String BEARER_TYPE = "bearer";

    static String EXPIRES_IN = "expires_in";

    /**
     * Parse the OAuth header parameters. The parameters will be oauth-decoded.
     *
     * @param request The request.
     * @return The parsed parameters, or null if no OAuth authorization header was supplied.
     */
    static String parseHeaderToken(HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Enumeration<String> headers = request.getHeaders("Authorization");
        while (headers.hasMoreElements()) { // typically there is only one (most servers enforce that)
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(BEARER_TYPE))) {
                String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            } else {
                // support additional authorization schemes for different token types, e.g. "MAC" specified by
                // http://tools.ietf.org/html/draft-hammer-oauth-v2-mac-token
            }
        }

        return null;
    }


    //http://192.168.1.181/redmine/projects/xinyuan/wiki/%E6%98%9F%E6%84%BFAPI%E6%96%87%E6%A1%A3#完成任务
    static final DBObject complete_mission = new BasicDBObject();
    static final DBObject MONEY = new BasicDBObject();
    static final Map<String,String>  QD_USER = new HashMap<String,String>() ;

    static final DBObject REG_LIMIT = new BasicDBObject();
    public static final Long THREE_DAY_SECONDS = 3 * 24 * 3600L;

    //http://192.168.1.181/redmine/projects/izhubo-ttus/wiki/%E5%AE%A2%E6%88%B7%E7%AB%AF40%E7%94%A8%E6%88%B7%E7%B3%BB%E7%BB%9F%E6%8E%A5%E5%8F%A3%E6%96%87%E6%A1%A3#根据access_token获取用户基本信息
    Map fetchFromUserSystem(final String access_token, final String clientId, HttpServletRequest req) throws IOException {

    	
        String sQd = req.getParameter("qd");
        String namespace = access_token.substring(0, 3);
        log.info("namespace: {}", namespace);
        for( String key : QD_USER.keySet()){
        	log.info("key: {}, val: {}", key, QD_USER.get(key));
        }
        log.info("StringUtils.isEmpty(sQd: {}): {}, access_token.charAt(2): {}", 
        		sQd, StringUtils.isEmpty(sQd), access_token.charAt(2));
        if(StringUtils.isEmpty(sQd))
        {
            if (access_token.charAt(2) == UserFrom.FLAG)
            {
              sQd = QD_USER.get(namespace);
            }
            else
            {
              sQd = "ttxy" ;
            }
        }
        log.info("sQd: {}", sQd);
        final String qd = sQd ;
        final String child_qd = req.getParameter("child_qd");
        _ThirdUser data = null;
        try
        {
            if (access_token.charAt(2) == UserFrom.FLAG)
            { // has namespace ?
            
                    try
                    {//第三方合作的
                    	log.info("第三方合作的");
                        ThirdUserBuilder builder = UserFrom.from(access_token.substring(0, 3));
                        log.info("builder: {}", builder);
                        data = builder.build(access_token);   // ignore nullpoint No enum constant
                        log.info("data: {}", data);
                    }
                    catch(Exception ex)
                    {
                        log.error("access_token:union data------------------>:"+access_token);
                        ex.printStackTrace();
                    }
                
            }
            else
            {
                String ledou_openid = req.getParameter(LeDou.ledou_openid);
                //TODO ledou access_token = 41 , so no need reHash to avoid conflict with own ttus..
                if (ledou_openid != null && ledou_openid.length() == LeDou.OPEN_ID_LENGTH)
                {
                	log.info("LeDou.userInfo");
                    data = LeDou.userInfo(access_token, ledou_openid);
                }
                //缺省使用新的q2
                else
                {

                	log.info("UserFrom.izhubo_qq.build(access_token), access_token： {}", access_token);
                	data = UserFrom.izhubo_qq.build(access_token);
                }
            }
        }
        catch(Exception ex)
        {
            log.error("access_token: data------------------>:"+access_token, ex);
        }

        DBObject user = buildShowUser(qd, clientId, data,child_qd);
        if (user == REG_LIMIT) {
            throw new IllegalStateException(regTooOften);
        }
        String status = "true" ;
        if(null != user.get("status"))
            status = user.get("status").toString() ;


        Integer userId = (Integer) user.get("_id");
        if (Boolean.TRUE != Boolean.parseBoolean(status))
        {
            throw new IllegalStateException(notAllowed);
        }

        Integer priv = Integer.parseInt(user.get("priv").toString());
        Map<String, String> hashResult = new HashMap<String, String>();
        hashResult.put("_id", userId.toString());
        hashResult.put("nick_name", (String) user.get("nick_name"));
        hashResult.put("priv", priv.toString());

//        Map finance = (Map) user.get(Finance.finance);
//        if (null != finance) {
//            Number coin_spend = (Number) finance.get(Finance.coin_spend_total);
//            if (null != coin_spend) {
//                hashResult.put("spend", coin_spend.toString());
//            }
//        }
        if (priv == UserType.主播.ordinal()) {
            Map star = (Map) user.get("star");
            if (star != null) {
                Integer roomId = Integer.parseInt(star.get("room_id").toString()) ;
                if (null != roomId) {
                    hashResult.put("room_id", roomId.toString());
                }
            }
        }
        String token_key = KeyUtils.accessToken(access_token);
        mainRedis.opsForHash().putAll(token_key, hashResult);
        mainRedis.expire(token_key, THREE_DAY_SECONDS, TimeUnit.SECONDS);
        mainRedis.opsForValue().set(KeyUtils.USER.token(userId), access_token, THREE_DAY_SECONDS, TimeUnit.SECONDS);

        //调试机器人
        String nick_name = (String) user.get("nick_name");
        log.info("mainRedis set key: {}, value: {}, nick_name: {}", 
        		KeyUtils.USER.token(userId), access_token, nick_name);
        //Web.setVIP(user);
        Web.setNewVIP(user);

        return hashResult;
    }

    static final String[] needFields = {"user_name", "via", "sex", "pic"};

    static final Long REG_LIMIT_SECONDS = 600L;
    static final String TOTAL_REG_PER_IP = "10";

    DBObject buildShowUser(final String qd, final String clientId, final _ThirdUser basicInfoWithTuid,final String child_qd) {
        final Object tuid =  basicInfoWithTuid != null ? basicInfoWithTuid.tuid : null ;

        if (null == tuid) {
            throw new IllegalStateException(notAuthorized);
        }
        DBCollection users = mainMongo.getCollection("users");
        log.info("tuid:{}",tuid);
        BasicDBObject query_tuid = new BasicDBObject("tuid", tuid);
        DBObject user = users.findOne(query_tuid);
        if (user != null) {
            return user;
        }

        //取消注册过于频繁的判断
//        if (clientIsLimited(clientId)) {
//            return REG_LIMIT;
//        }
        user = new BasicDBObject(_id, userKGS.nextId());
        
        

        String nick_name = (String) basicInfoWithTuid.nick_name;
        if (null == nick_name) {
            nick_name = "izhubo_" + user.get(_id);
        }
        for (String field : needFields) {
            Object value = basicInfoWithTuid.get(field);			//注意QQ需要返回QQ头像到pic执行初始化
            if (null != value) {
                user.put(field, value);
            }
        }
//                        user.putAll(basicInfoWithNickName);
        user.put("nick_name", HtmlUtils.htmlEscape(nick_name));    //注意QQ需要返回QQ昵称，执行初始化
        user.put("tuid", tuid);
        user.put("priv", UserType.普通用户.ordinal());
        user.put("status", Boolean.TRUE);
        user.put("mission", complete_mission);

        user.put(Param.timestamp, System.currentTimeMillis());

        user.put("finance", MONEY);

        if (StringUtils.isNotBlank(qd)) {
            user.put("qd", qd);
            if (StringUtils.isNotBlank(child_qd))
            {
                String child_id = qd+"_"+child_qd ;
                user.put("qd", child_id);
                user.put("parent_qd", qd);
                Web.saveChannel(qd,child_id);
            }
        }
        try {
            //TODO  wait Mod on _id not allowed
            // https://jira.mongodb.org/browse/SERVER-9958
            //log.debug(">>>>>>>>>>>>>>>buildShowUser qd: {}", user.get("qd"));
            return users.findAndModify(query_tuid.append(_id, user.removeField(_id)), null,
                    null, false,
                    new BasicDBObject($setOnInsert, user), true, true); //upsert
   
        } catch (MongoException e) {
            log.error("TUID Error..upsert...", e);
            query_tuid.remove(_id);
            return users.findOne(query_tuid);
        }
    }


    private boolean clientIsLimited(String clientId) {
        //
        //10分钟注册一个
        String regLimit = KeyUtils.USER.regLimit(clientId);
        if (!mainRedis.opsForValue().setIfAbsent(regLimit, "")) {
            if (mainRedis.getExpire(regLimit) < 0) { //make sure crash between the first SETNX and the EXPIRE will cause a deadlock.
                mainRedis.expire(regLimit, REG_LIMIT_SECONDS, TimeUnit.SECONDS);
            }

            return true;
        } else {
            mainRedis.expire(regLimit, REG_LIMIT_SECONDS, TimeUnit.SECONDS);
        }

        String totalIpLimit = KeyUtils.USER.regLimitTotalPerIp(clientId);
        mainRedis.opsForValue().setIfAbsent(totalIpLimit, TOTAL_REG_PER_IP);
        if (mainRedis.getExpire(totalIpLimit) < 0) {
            Calendar self = Calendar.getInstance();
            self.set(Calendar.HOUR_OF_DAY, 24);
            self.clear(Calendar.MINUTE);
            self.clear(Calendar.SECOND);
            mainRedis.expireAt(totalIpLimit, self.getTime());
        }
        return mainRedis.opsForValue().increment(totalIpLimit, -1L).intValue() < 0;

    }
}

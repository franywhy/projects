package com.izhubo.web;

import static com.izhubo.rest.common.doc.MongoKey._id;
import static com.izhubo.rest.common.doc.MongoKey.timestamp;
import groovy.transform.CompileStatic;
//import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.izhubo.common.util.ChatExecutor;
import com.izhubo.common.util.KeyUtils;
import com.izhubo.model.Code;
import com.izhubo.rest.AppProperties;
import com.izhubo.rest.common.util.JSONUtil;
import com.izhubo.rest.web.support.ControllerSupport7;
import com.izhubo.userSystem.mongo.qquser.QQUserRepositery;
import com.izhubo.web.api.Web;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteConcern;

/**
 * action<br>
 * <p/>
 * <p/>
 * date: 12-8-20 下午2:58
 * 
 * @author: wubinjie@ak.cc
 */
@CompileStatic
// @Slf4j
public abstract class BaseController extends ControllerSupport7 {

	@Resource
	public MongoTemplate mainMongo;
	@Resource
	public MongoTemplate adminMongo;
	@Resource
	public MongoTemplate topicMongo;
	@Resource
	public MongoTemplate logMongo;
	@Resource
	public MongoTemplate unionMongo;
	@Resource
	public MongoTemplate activeMongo;
	@Resource
	public MongoTemplate singMongo;
	@Resource
	public MongoTemplate rankMongo;

	@Resource
	public MongoTemplate qquserMongo;

	public static final String SHOW_URL = AppProperties.get("site.domain");
	public static final StringRedisTemplate mainRedis = Web.mainRedis;
	public static final StringRedisTemplate chatRedis = Web.chatRedis;
//	public static final StringRedisTemplate picRedis = Web.picRedis;
	@Resource
	public StringRedisTemplate liveRedis;

	static final Logger logs = LoggerFactory.getLogger(BaseController.class);
	@Resource
	public WriteConcern writeConcern;
	
	@Autowired
	protected QQUserRepositery qqUserRepositery;

	protected String appid;
	protected String appkey;
	protected String appServerDomain;
	protected String appServerTestDomain;
	protected String appServerProductDomain;
	protected String hostName;
	protected ServerTypeEnum serverType = ServerTypeEnum.product;
	protected String url;
	protected static String softwareSerialNo;
	protected static String key;

	protected boolean openApiTest = false;

	private static Logger logger = LoggerFactory
			.getLogger(BaseController.class);

	public BaseController() {

		openApiTest = "1".equals(AppProperties.get("openapi.test"));
		appServerProductDomain = AppProperties.get("openapi.domain");
		appServerTestDomain = AppProperties.get("openapi.test.domain");

		appServerDomain = openApiTest ? appServerTestDomain
				: appServerProductDomain;

		appid = AppProperties.get("appid");
		appkey = AppProperties.get("appkey");
//		logger.info(
//				"openApiTest: {}, appServerProductDomain: {}, appServerTestDomain: {}, appServerDomain: {}, "
//						+ "appid: {}, appkey: {}", openApiTest,
//				appServerProductDomain, appServerTestDomain, appServerDomain,
//				appid, appkey);

		hostName = "app" + appid + ".twsapp.com";
		
		String stype = AppProperties.get("serverType");
//		logger.info("stype: {}", stype);
		serverType = ServerTypeEnum.valueOf(AppProperties.get("serverType", "product"));
		url = AppProperties.get("url");
		softwareSerialNo = AppProperties.get("softwareSerialNo");
		key = AppProperties.get("key");
		
	}

	public DBCollection users() {
		return mainMongo.getCollection("users");
	}
	

	public DBCollection rooms() {
		return mainMongo.getCollection("rooms");
	}

	public DBCollection songs() {
		return mainMongo.getCollection("songs");
	}

	public DBCollection photos() {
		return mainMongo.getCollection("photos");
	}

	public void logRoomEdit(String type, Integer roomId, Object data) {
		Map obj = new HashMap();
		obj.put("type", type);
		obj.put("room", roomId);
		obj.put("data", data);
		obj.put("session", Web.getSession());
		obj.put("timestamp", System.currentTimeMillis());
		logMongo.getCollection("room_edit").save(new BasicDBObject(obj));
	}

	public static void publish(final String channel, final String json) {
		ChatExecutor.execute(new Runnable() {
			public void run() {
				final byte[] data = KeyUtils.serializer(json);
				chatRedis.execute(new RedisCallback() {
					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						Object obj = connection.publish(
								KeyUtils.serializer(channel), data);
						return obj;
					}
				});
			}
		});
	}

	public static void publish(final String channel,
			Map<String, Serializable> json) {
		publish(channel, JSONUtil.beanToJson(json));
	}

	public void incLiveSpeed(String userId, Number coin, Object roomId) {
		logger.info("incLiveSpeed userId: {}, coin: {}, roomid: {}", userId, coin, roomId);
		liveRedis.opsForZSet().incrementScore(
				KeyUtils.LIVE.userCostZset(roomId), userId, coin.doubleValue());
	}



	public static final String finance_log = "finance_log";
	public static final String finance_log_id = finance_log + "." + _id;
	public static final String room_cost = "room_cost";
	public static final String cost_logs = "cost_logs";
	public static final int DEFAULT_RATE = 100;
	public static final int VPAY_DEFAULT_RATE = 50;
	public static long DAY_MILLON = 24 * 3600 * 1000L;


	private static final String LOG_COLL_NAME = "ops";
	
	public void opLog(Enum type,Object data){
		opLog(type.name(), data);
	}
	public void opLog(String type,Object data){
        BasicDBObject obj = new BasicDBObject();
        Long tmp = System.currentTimeMillis();
        obj.put(_id,tmp);
        obj.put("type",type);
        obj.put("session",Web.getSession());
        obj.put("data",data);
        obj.put(timestamp,tmp);
        adminMongo.getCollection(LOG_COLL_NAME).save(obj);
	}
	
	public Map getResultOK(){
		Map map = new HashMap();
		map.put("code", Code.OK);
		map.put("msg", Code.OK_S);
		return map;
	}
	public Map getResultOKS(){
		Map map = new HashMap();
		map.put("code", Code.OKS);
		map.put("msg", Code.OK_S);
		return map;
	}
	public Map getResultOK(Object data){
		Map map = new HashMap();
		map.put("code", Code.OK);
		map.put("msg", Code.OK_S);
		map.put("data", data);
		return map;
	}
	public Map getResultOKS(Object data){
		Map map = new HashMap();
		map.put("code", Code.OKS);
		map.put("msg", Code.OK_S);
		map.put("data", data);
		return map;
	}
	public Map getResultParamsError(){
		Map map = new HashMap();
		map.put("code", Code.参数无效S);
		map.put("msg", Code.参数无效_S);
		return map;
	}
	public Map getResult(Integer code , String msg , Object data){
		Map map = new HashMap();
		map.put("code", code);
		map.put("msg", msg);
		map.put("data", data);
		return map;
		
	}
	
	public Map getResult(String code , String msg , Object data){
		Map map = new HashMap();
		map.put("code", code);
		map.put("msg", msg);
		map.put("data", data);
		return map;
	}
	

}

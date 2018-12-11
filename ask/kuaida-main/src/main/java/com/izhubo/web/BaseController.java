package com.izhubo.web;

import static com.izhubo.model.Finance.finance$coin_count;
import static com.izhubo.model.Finance.finance$coin_spend_total;
import static com.izhubo.rest.common.doc.MongoKey.$gte;
import static com.izhubo.rest.common.doc.MongoKey.$inc;
import static com.izhubo.rest.common.doc.MongoKey.$pull;
import static com.izhubo.rest.common.doc.MongoKey.$push;
import static com.izhubo.rest.common.doc.MongoKey.$set;
import static com.izhubo.rest.common.doc.MongoKey._id;
import static com.izhubo.rest.common.doc.MongoKey.timestamp;

import com.izhubo.rest.common.util.http.HttpClientUtil4_3;

import groovy.transform.CompileStatic;
//import lombok.extern.slf4j.Slf4j;





import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.hqonline.model.Privs;
import com.izhubo.common.doc.Param;
import com.izhubo.common.util.ChatExecutor;
import com.izhubo.common.util.KeyUtils;
import com.izhubo.model.Code;
import com.izhubo.model.DR;
import com.izhubo.model.EnumType;
import com.izhubo.model.UserType;
import com.izhubo.rest.AppProperties;
import com.izhubo.rest.common.util.JSONUtil;
import com.izhubo.rest.common.util.MsgDigestUtil;
import com.izhubo.rest.pushmsg.IPushService;
import com.izhubo.rest.pushmsg.XinggePushService;
import com.izhubo.rest.web.support.ControllerSupport7;
import com.izhubo.userSystem.mongo.qquser.QQUser;
import com.izhubo.userSystem.mongo.qquser.QQUserRepositery;
import com.izhubo.web.api.DoCoin;
import com.izhubo.web.api.Web;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.qq.open.OpenApiV3;
import com.qq.open.SnsSigCheck;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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

	public static final String REF = "";

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
	public static final StringRedisTemplate picRedis = Web.picRedis;
	@Resource
	public StringRedisTemplate liveRedis;

	static final Logger logs = LoggerFactory.getLogger(BaseController.class);
	@Resource
	public WriteConcern writeConcern;

	@Autowired
	protected QQUserRepositery qqUserRepositery;

	@Autowired
	private JedisPool jedisPool;

	private static final String LOCK_SUCCESS = "OK";
	private static final String SET_IF_NOT_EXIST = "NX";
	private static final String SET_WITH_EXPIRE_TIME = "PX";

	private static final Long RELEASE_SUCCESS = 1L;

	private static volatile Jedis jedis;

	public Jedis getJedis(){
		if (jedis == null){
			synchronized (Jedis.class){
				if (jedis == null){
					jedis = jedisPool.getResource();
				}
			}
		}
		return jedis;
	}

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

	public IPushService GetPushService() {
		XinggePushService xingePush = new XinggePushService();

		return xingePush;

	}

	public BaseController() {

		openApiTest = "1".equals(AppProperties.get("openapi.test"));
		appServerProductDomain = AppProperties.get("openapi.domain");
		appServerTestDomain = AppProperties.get("openapi.test.domain");

		appServerDomain = openApiTest ? appServerTestDomain
				: appServerProductDomain;
		appid = AppProperties.get("appid");
		appkey = AppProperties.get("appkey");
		// logger.info(
		// "openApiTest: {}, appServerProductDomain: {}, appServerTestDomain: {}, appServerDomain: {}, "
		// + "appid: {}, appkey: {}", openApiTest,
		// appServerProductDomain, appServerTestDomain, appServerDomain,
		// appid, appkey);

		hostName = "app" + appid + ".twsapp.com";

		String stype = AppProperties.get("serverType");
		// logger.info("stype: {}", stype);
		serverType = ServerTypeEnum.valueOf(AppProperties.get("serverType",
				"product"));
		url = AppProperties.get("url");
		softwareSerialNo = AppProperties.get("softwareSerialNo");
		key = AppProperties.get("key");

	}

	/** 报名表 */
	public DBCollection signs() {
		return mainMongo.getCollection("signs");
	}

	public DBCollection users() {
		return mainMongo.getCollection("users");
	}
	
	public DBCollection constants() {
		return mainMongo.getCollection("constants");
	}

	public DBCollection enums() {
		return mainMongo.getCollection("enums");
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

	/** 业务单元-校区省市大区总部 */
	public DBCollection area() {
		return mainMongo.getCollection("area");
	}
	
	
	public DBCollection qQUser() {
		return qquserMongo.getCollection("qQUser");
	}

	protected DBObject enumItem(EnumType et) {
		List<DBObject> list = enums()
				.find(new BasicDBObject("type", et.ordinal())).limit(1)
				.toArray();
		if (list != null) {
			return list.get(0);
		}
		return null;
	}

	protected java.util.List getCurrentUserPrivs(Integer user_id) {

		DBObject user = users().findOne(new BasicDBObject("_id", user_id));
		java.util.List privs = (java.util.List) user.get("privs");

		return privs;

	}

	/**
	 * 获取用户某个权限值
	 * 
	 * @param user_id
	 *            用户id
	 * @param priv
	 *            权限枚举对象
	 * @return 0.无权限 1.有权限
	 */
	protected Integer getPrivs(Integer user_id, Privs priv) {
		DBObject user = users().findOne(new BasicDBObject("_id", user_id));
		return (Integer) user.get(priv.getKey());
	}

	/**
	 * 判断用户某个权限
	 * 
	 * @param user_id
	 *            用户id
	 * @param priv
	 *            权限枚举对象
	 * @return true:有权限 false:无权限
	 */
	protected Boolean isPrivs(Integer user_id, Privs priv) {
		Integer p = getPrivs(user_id, priv);
		if (null != p && p == 1) {
			return true;
		}
		return false;
	}

	protected int getUserIdByToken(String token) {
		int user_id = 0;
		user_id = Integer.valueOf((String) (mainRedis.opsForHash().get(
				"token:" + token, "_id") == null ? "0" : mainRedis.opsForHash()
				.get("token:" + token, "_id")));

		return user_id;
	}

	protected Double kd_prop() {
		return 1d;
		/*
		 * Double p = 0d; DBObject item = enumItem(EnumType.会豆人民币转换率); if(item
		 * != null){ p = (Double) item.get("val0"); } return p;
		 */
	}

	protected Double fee_prop() {
		DBObject constantsTab = constants().findOne(new BasicDBObject("dr", 0));
		if (null != constantsTab
				&& null != constantsTab.get("topic_tip_fee_prop")) {
			return (Double) constantsTab.get("topic_tip_fee_prop");
		}
		return 0.8d;
		/*
		 * Double p = 0d; DBObject item = enumItem(EnumType.会豆人民币转换率); if(item
		 * != null){ p = (Double) item.get("val0"); } return p;
		 */
	}

	/**
	 * 每月提问免费数量
	 * 
	 * @return 免费数量
	 */
	protected Integer topic_free_num(Integer user_id, boolean isStudent) {
		DBObject constantsTab = constants().findOne(new BasicDBObject("dr", 0));
		// if (isPrivs(user_id, Privs.学员)) {
		if (isStudent) {
			if (getIsZJStudent(user_id)) {
				if (null != constantsTab && null != constantsTab.get("topic_free_num_student_ZJ")) {
					return (Integer) constantsTab.get("topic_free_num_student_ZJ");
				}
				return 30;
			} else {
				if (null != constantsTab && null != constantsTab.get("topic_free_num_student")) {
					return (Integer) constantsTab.get("topic_free_num_student");
				}
				return 10;
			}
		} else {
			if (null != constantsTab && null != constantsTab.get("topic_free_num")) {
				return (Integer) constantsTab.get("topic_free_num");
			}
			return 3;
		}
	}

	/**
	 * 剩余提问文案
	 * 
	 * @return 剩余提问文案
	 */
	protected String topic_free_num_text(boolean is_free, boolean isStudent) {
		DBObject constantsTab = constants().findOne(new BasicDBObject("dr", 0));
		if (null != constantsTab && null != constantsTab.get("topic_free_num_text") && null != constantsTab.get("topic_free_num_text")) {
			if (isStudent) {
				if (is_free) {
					return (String) constantsTab.get("topic_free_num_text_1_1");
				} else {
					return (String) constantsTab.get("topic_no_free_num_text_1_1");
				}
			} else {
				if (is_free) {
					return (String) constantsTab.get("topic_free_num_text_no_s_1_1");
				} else {
					return (String) constantsTab.get("topic_no_free_num_text_no_s_1_1");
				}
			}
		}
		//return "请详细描述您的问题（不少于十个个字符），每次提问不能包含多个问题。（本月可免费提问{FREE_NUM}个问题）";
		return "请输入不少于10个字符的问题描述，单次提问不能包含多个问题。";
	}

	/**
	 * 剩余提问文案-自考
	 *
	 * @return 剩余提问文案-自考
	 */
	protected String topic_free_num_text_zikao(boolean is_free) {
		DBObject constantsTab = constants().findOne(new BasicDBObject("dr", 0));
		if (null != constantsTab && null != constantsTab.get("topic_free_num_text_zikao")) {
			if (is_free) {
				return (String) constantsTab.get("topic_free_num_text_no_s_1_1_zikao");
			} else {
				return (String) constantsTab.get("topic_no_free_num_text_no_s_1_1_zikao");
			}
		}
		return "请输入不少于10个字符的问题描述，单次提问不能包含多个问题。";
	}

	/**
	 * 获取用户是否为学员
	 * 
	 * @param user_id
	 *            用户id
	 * @param priv
	 *            权限枚举对象
	 * @return 0.无权限 1.有权限
	 */
	protected Boolean getIsStudent(Integer user_id) {
		DBObject user = users().findOne(new BasicDBObject("_id", user_id));
		if (user != null) {
			String tuid = (String) user.get("tuid");
			if (StringUtils.isNotBlank(tuid)) {
				List<QQUser> list = this.qqUserRepositery.findByTuid(tuid);
				QQUser qqUser = list.get(0);
				String phone = qqUser.getUsername();
				DBObject signs = signs().findOne(
						new BasicDBObject("phone", phone).append("dr", 0));
				if (signs != null) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 获取用户是否为中级的学员
	 * 
	 * @param user_id
	 *            用户id
	 * @param priv
	 *            权限枚举对象
	 * @return 0.无权限 1.有权限
	 */
	protected Boolean getIsZJStudent(Integer user_id) {
		DBObject user = users().findOne(new BasicDBObject("_id", user_id));

		if (user != null) {
			String tuid = (String) user.get("tuid");
			if (StringUtils.isNotBlank(tuid)) {
				List<QQUser> list = this.qqUserRepositery.findByTuid(tuid);
				QQUser qqUser = list.get(0);
				String phone = qqUser.getUsername();
				Object nc_commodity_idList = constants().findOne().get(
						"middle_commodity_id");
				if (nc_commodity_idList == null) {
					return false;
				}
				BasicDBObject q = new BasicDBObject();
				q.append("phone", phone);
				q.append("dr", 0);
				q.append("nc_commodity_id", new BasicDBObject("$in",
						nc_commodity_idList));
				DBObject signs = signs().findOne(q);
				if (signs != null) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	/**
	 * 商品
	 */
	public DBCollection commoditys() {
		return mainMongo.getCollection("commoditys");
	}

	public static final int MAX_LIMIT = 1000;

	public static final String IS_SHELVES = "is_shelves";

	public static final String USERNAME_TO_ACCESS_TOKEN_KEY = "username_to_access_token";
	public static final String ACCESS_TOKEN_TO_USERNAME_KEY = "access_token_to_username";

	protected String getToken(Integer user_id) {
		String token = null;
		DBObject user = users().findOne(new BasicDBObject("_id", user_id),
				new BasicDBObject("tuid", 1));
		if (user != null) {
			String tuid = (String) user.get("tuid");
			if (StringUtils.isNotBlank(tuid)) {
				List<QQUser> list = this.qqUserRepositery.findByTuid(tuid);
				QQUser qqUser = list.get(0);
				String username = qqUser.getUsername();
				token = (String) mainRedis.opsForHash().get(
						USERNAME_TO_ACCESS_TOKEN_KEY, username);
			}
		}
		return token;
	}

	/**
	 * 获取登录用户手机号码
	 * 
	 * @return
	 */
	public String getUserPhoneByUserId() {
		return getUserPhoneByUserId(Web.getCurrentUserId());
	}

	/**
	 * 获取登录用户手机号码
	 * 
	 * @return
	 */
	public String getUserPhoneByUserId(Integer user_id) {
		String phone = null;
		DBObject user = users().findOne(new BasicDBObject("_id", user_id),
				new BasicDBObject("tuid", 1));
		if (user != null) {
			String tuid = (String) user.get("tuid");
			if (StringUtils.isNotBlank(tuid)) {
				List<QQUser> list = this.qqUserRepositery.findByTuid(tuid);
				QQUser qqUser = list.get(0);
				phone = qqUser.getUsername();
			}
		}
		return phone;
	}

	/**
	 * 查询条件加入未删除
	 * 
	 * @param dbo
	 * @return
	 */
	public BasicDBObject appendDR(BasicDBObject dbo) {
		if (dbo == null) {
			dbo = new BasicDBObject();
		}
		dbo.append("dr", DR.正常.ordinal());
		return dbo;
	}

	public BasicDBObject commoditysQuery() {
		return new BasicDBObject(IS_SHELVES, 1);
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
		logger.info("incLiveSpeed userId: {}, coin: {}, roomid: {}", userId,
				coin, roomId);
		liveRedis.opsForZSet().incrementScore(
				KeyUtils.LIVE.userCostZset(roomId), userId, coin.doubleValue());
	}

	/**
	 * Concurrent requests for database connection have exceeded limit of 80 ab
	 * -c 500 -n 10000
	 * "http://api.show.izhubo.com/room/send_gift/8e51475598a15777dced50e56c2d5a81/1024418/1?count=1&user_id=1024418"
	 * connections-per-host="80"
	 * threads-allowed-to-block-for-connection-multiplier="6"
	 * <p/>
	 * costLog.get("live") 记录本场消费 所有房间内消费
	 */
	public boolean costCoin(Integer userId, Integer coin, DoCoin doCoin) {
		BasicDBObject costLog;
		if (coin <= 0 || (costLog = doCoin.costLog()) == null) {
			return false;
		}
		logger.info("doCoin.costLog()", doCoin.costLog());
		String costLogId = userId + "_" + System.nanoTime(); // use nano To
																// query NO need
																// log._id :[$ne
																// : logId ]
		costLog.put(_id, costLogId);
		DBCollection users = users();

		BasicDBObject update = new BasicDBObject($inc, new BasicDBObject(
				finance$coin_count, -coin).append(finance$coin_spend_total,
				coin)).append(
				$push,
				new BasicDBObject(room_cost, costLog.append(timestamp,
						System.currentTimeMillis())));

		if (0 == users.update(
				new BasicDBObject(_id, userId).append(finance$coin_count,
						new BasicDBObject($gte, coin)), update, false, false,
				writeConcern).getN()) {
			return false;
		}

		boolean hasError;
		try {
			hasError = !doCoin.costSuccess();
		} catch (Exception e) {
			logs.error("cost coin Error", e);
			hasError = true;
		}

		if (hasError) { // 花钱失败了
			users.update(
					new BasicDBObject(_id, userId),
					new BasicDBObject($inc, new BasicDBObject(
							finance$coin_count, coin).append(
							finance$coin_spend_total, -coin)).append($pull,
							new BasicDBObject(room_cost, new BasicDBObject(_id,
									costLogId))), false, false, writeConcern);
			return false;
		}
		// 默认认为扣费成功。。 timestamp 用来做检查
		logMongo.getCollection(room_cost).save(costLog, writeConcern);
		users.update(new BasicDBObject(_id, userId),
				new BasicDBObject($pull, new BasicDBObject(room_cost,
						new BasicDBObject(_id, costLogId))));
		String liveId = (String) costLog.get("live");
		if (StringUtils.isNotBlank(liveId)) {// 记录本场消费
			incLiveSpeed(((Map) costLog.get("session")).get(_id).toString(),
					(Number) costLog.get("cost"), costLog.get("room"));
		}
		return true;
	}

	/**
	 * 招生权限
	 * 
	 * @param user_id
	 * @return true 有招生权限 false 无招生权限
	 */
	public boolean isZS(Integer user_id) {
		BasicDBObject q = new BasicDBObject(_id, user_id);
		q.append("priv", UserType.教学老师.ordinal());
		q.append("priv2", 1);
		DBObject user = mainMongo.getCollection("users").findOne(q);
		return user != null;
	}

	/**
	 * 教学权限
	 * 
	 * @param user_id
	 * @return true 有教学权限 false 无教学权限
	 */
	public boolean isJX(Integer user_id) {
		BasicDBObject q = new BasicDBObject(_id, user_id);
		q.append("priv", UserType.教学老师.ordinal());
		q.append("priv1", 1);
		DBObject user = mainMongo.getCollection("users").findOne(q);
		return user != null;
	}

    private  final  String CCREDISKEY = "cc:";
	private  final  String CCUSERKEY = "FE7A65E6BE2EA539";
	private  final  String CCUSERAPIKEY = "aangMoDUUacDTApvw8eCs8IobwMb9Gec";
	private  final  String CCAPIURL = "http://spark.bokecc.com/api/video";

    /**
     * 获取cc视频id，视频封面会做缓存redis，缓存1天自动过期
     * @param vid
     * @return 视频的封面url
     */
	public String getCCVideoPic(String vid)
	{
		String ccvideopic = mainRedis.opsForValue().get(CCREDISKEY+vid);
		if(ccvideopic == null)
		{
		    try {
		    	long unixtimeString = System.currentTimeMillis();
		    	String urlString = String.format("format=json&userid=%s&videoid=%s&time=%s", CCUSERKEY,vid,unixtimeString);
		    	String saltString =CCUSERAPIKEY;
		    	String hashString =  MsgDigestUtil.MD5.digest2HEX(urlString+String.format("&salt=%s", saltString));
		    	urlString+=String.format("&hash=%s", hashString);
                String result = HttpClientUtil4_3.get(CCAPIURL+"?"+urlString, null);
                JSONObject jsonObject = new JSONObject(result);
                ccvideopic = jsonObject.getJSONObject("video").getString("image");
                //存入redis，过期时间1天
                mainRedis.opsForValue().set(CCREDISKEY+vid,ccvideopic,24, TimeUnit.DAYS);

            }
            catch (Exception ex)
            {

            }

		}
        return ccvideopic;

	}


	public boolean activityCostCoin(Integer userId, Integer coin, DoCoin doCoin) {
		BasicDBObject costLog;
		if (coin <= 0 || (costLog = doCoin.costLog()) == null) {
			return false;
		}
		String costLogId = userId + "_" + System.nanoTime(); // use nano To
																// query NO need
																// log._id :[$ne
																// : logId ]
		costLog.put(_id, costLogId);
		DBCollection users = users();

		BasicDBObject update = new BasicDBObject($inc, new BasicDBObject(
				finance$coin_count, -coin).append(finance$coin_spend_total,
				coin)).append(
				$push,
				new BasicDBObject(cost_logs, costLog.append(timestamp,
						System.currentTimeMillis())));

		if (0 == users.update(
				new BasicDBObject(_id, userId).append(finance$coin_count,
						new BasicDBObject($gte, coin)), update, false, false,
				writeConcern).getN()) {
			return false;
		}

		boolean hasError;
		try {
			hasError = !doCoin.costSuccess();
		} catch (Exception e) {
			logs.error("cost coin Error", e);
			hasError = true;
		}

		if (hasError) { // 花钱失败了
			users.update(
					new BasicDBObject(_id, userId),
					new BasicDBObject($inc, new BasicDBObject(
							finance$coin_count, coin).append(
							finance$coin_spend_total, -coin)).append($pull,
							new BasicDBObject(cost_logs, new BasicDBObject(_id,
									costLogId))), false, false, writeConcern);
			return false;
		}
		// 默认认为扣费成功。。 timestamp 用来做检查
		activeMongo.getCollection(cost_logs).save(costLog, writeConcern);
		users.update(new BasicDBObject(_id, userId),
				new BasicDBObject($pull, new BasicDBObject(cost_logs,
						new BasicDBObject(_id, costLogId))));
		return true;
	}

	public static final String finance_log = "finance_log";
	public static final String finance_log_id = finance_log + "." + _id;
	public static final String room_cost = "room_cost";
	public static final String cost_logs = "cost_logs";
	public static final int DEFAULT_RATE = 100;
	public static final int VPAY_DEFAULT_RATE = 50;
	public static long DAY_MILLON = 24 * 3600 * 1000L;

	// 最后用户充值加币
	public boolean addCoin(Integer userId, Long coin, BasicDBObject logWithId) {
		try {

			DBCollection users = users();
			DBObject my_user = users.findOne(new BasicDBObject(_id, userId));
			DBObject obj = (DBObject) my_user.get("finance");
			logger.info("into addCoin userId:{},my_user:{}", userId, my_user);
			// 查询用户充值之前的余额数"coin_count"
			float beforebalance = Float.parseFloat(obj.get("coin_count")
					.toString());
			String log_id = (String) logWithId.get(_id);
			if (coin <= 0 || log_id == null) {
				return false;
			}
			if (my_user != null) {

				if (null != my_user.get("qd")) {
					String qd = my_user.get("qd").toString();
					logWithId.append("qd", qd);
				}
				DBCollection logColl = adminMongo.getCollection(finance_log);
				if (users.update(
						new BasicDBObject(_id, userId),
						new BasicDBObject($inc, new BasicDBObject(
								finance$coin_count, coin))).getN() == 1) {
					logger.info("into update.getN==1 userId:{}", userId);

					users.update(
							new BasicDBObject(_id, userId),
							new BasicDBObject($push, new BasicDBObject(
									finance_log, logWithId.append(timestamp,
											System.currentTimeMillis()))),
							false, false, writeConcern);

					// logColl.save(logWithId, writeConcern);
					logger.info("token：{}", logWithId.get("token"));
					if (logWithId.get("token") != null
							&& logWithId.get("token") != "") {
						logColl.update(
								new BasicDBObject("token", logWithId
										.get("token")),
								new BasicDBObject($set, new BasicDBObject(
										"Confirmation", 1).append("coin", coin)));
					} else {
						logger.info("token is null!");
						logWithId.put("Confirmation", 1);
						logColl.save(logWithId, writeConcern);
					}

					// int i = logColl.update(new
					// BasicDBObject("token",logWithId.get("token")),
					// new BasicDBObject($set,new
					// BasicDBObject("ext",logWithId.get("ext")))).getN();

					users.update(new BasicDBObject(_id, userId),
							new BasicDBObject($pull,
									new BasicDBObject(finance_log,
											new BasicDBObject(_id, log_id))),
							false, false, writeConcern);
					// 获得充值之后的金额
					DBCollection userss = users();
					DBObject my_users = userss.findOne(new BasicDBObject(_id,
							userId));
					DBObject objs = (DBObject) my_users.get("finance");
					float afterbalance = Float.parseFloat(objs
							.get("coin_count").toString());

					logger.info(
							"userid:{},logWithId.ext :{},logWithId.token :{},beforebalance:{},afterbalance:{}",
							userId, logWithId.get("ext"),
							logWithId.get("token"), beforebalance, afterbalance);

					return true;
				} else {
					logger.info("Recharge fail");
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		return false;

	}

	public boolean isRoomManagement(Integer room_id) {
		int userType = Web.currentUserType();
		int userId = Web.getCurrentUserId();

		// 要判断是否是这个房间的主播

		DBObject room = rooms().findOne(room_id);
		return userType == UserType.运营人员.ordinal()
				|| (userType == UserType.教学老师.ordinal() && room
						.get("xy_star_id").toString()
						.equals(String.valueOf(userId)));
	}

	/**
	 * 检查验证码是否输错
	 * 
	 * @param req
	 * @return
	 */
	public boolean codeVerifError(HttpServletRequest req) {
		String auth_code = req.getParameter("auth_code");
		String auth_key = KeyUtils.USER.authCode(Web.currentUserId());
		String mauth_code = mainRedis.opsForValue().get(auth_key);
		logger.info("auth_code: {}, auth_key: {}, mauth_code: {}", auth_code,
				auth_key, mauth_code);
		if (null == auth_code || !auth_code.equalsIgnoreCase(mauth_code)) {
			return true;// [code: 30419]
		}
		mainRedis.delete(auth_key);
		return false;
	}

	public Map<String, Object> getYellowVip(int userid) {
		logger.info("hello getYellowVip");
		if (userid == 0) {
			userid = Web.getCurrentUserId();
		}
		String username = null;
		String openkey;
		String pf;
		String pfkey;
		String openid;
		Map<String, Object> map = new HashMap<String, Object>();
		HashMap<String, String> params = new HashMap<String, String>();
		// 查询用户name
		BasicDBObject query_tuid = new BasicDBObject("_id", userid);
		DBObject UserObj = mainMongo.getCollection("users").findOne(query_tuid);
		String tuid = (String) UserObj.get("tuid");
		logger.info("tuid:{}", tuid);
		try {
			List<QQUser> list = this.qqUserRepositery.findByTuid(tuid);
			QQUser qqUser = list.get(0);
			username = qqUser.getUsername();
			logger.info("username:{}", username);
		} catch (Exception e) {
			logger.info("e:{}", e);
		}
		// 查询openkey,openid
		String openIdAndKey = (String) mainRedis.opsForHash().get(
				Param.redis_username_to_openIdAndOpenKey, username);
		logger.info("openIdAndKey:{}", openIdAndKey);
		String[] openIdAndKeyArray = openIdAndKey.split(",,,,");
		openid = openIdAndKeyArray[0];
		openkey = openIdAndKeyArray[1];
		pf = openIdAndKeyArray[2];
		String secret = appkey + "&";
		params.put("openid", openid);
		params.put("openkey", openkey);
		params.put("appid", appid);
		params.put("pf", pf);
		String sig;
		try {
			sig = SnsSigCheck.makeSig("get", "/v3/user/is_vip", params, secret);
			logger.info("sig:{}", sig);
			params.put("sig", sig);

			OpenApiV3 sdk = new OpenApiV3(appid, appkey);
			sdk.setServerName(appServerDomain);
			logger.info("params:{}", params);
			String jsonStr = sdk.api("/v3/user/is_vip", params, "http");
			logger.info("jsonStr:{}", jsonStr);
			map = JSONUtil.jsonToMap(jsonStr);
			logger.info("jsonStr:{}", map);
		} catch (Exception e) {
			logger.error("", e);
		}
		return map;
	}

	private static final String LOG_COLL_NAME = "ops";

	public void opLog(Enum type, Object data) {
		opLog(type.name(), data);
	}

	public void opLog(String type, Object data) {
		BasicDBObject obj = new BasicDBObject();
		Long tmp = System.currentTimeMillis();
		obj.put(_id, tmp);
		obj.put("type", type);
		obj.put("session", Web.getSession());
		obj.put("data", data);
		obj.put(timestamp, tmp);
		adminMongo.getCollection(LOG_COLL_NAME).save(obj);
	}

	public Map getResultOK() {
		Map map = new HashMap();
		map.put("code", Code.OK);
		map.put("msg", Code.OK_S);
		return map;
	}

	public Map getResultOKS() {
		Map map = new HashMap();
		map.put("code", Code.OKS);
		map.put("msg", Code.OK_S);
		return map;
	}

	public Map getResultOK(Object data) {
		Map map = new HashMap();
		map.put("code", Code.OK);
		map.put("msg", Code.OK_S);
		map.put("data", data);
		return map;
	}

	public Map getResultOKS(Object data) {
		Map map = new HashMap();
		map.put("code", Code.OKS);
		map.put("msg", Code.OK_S);
		map.put("data", data);
		return map;
	}

	public Map getResultParamsError() {
		Map map = new HashMap();
		map.put("code", Code.参数无效S);
		map.put("msg", Code.参数无效_S);
		map.put("data", Code.参数无效_S);

		return map;
	}

	public Map getResultToken() {
		Map map = new HashMap();
		map.put("code", 30405);
		map.put("msg", "ACCESS_TOKEN无效");
		//map.put("data", "ACCESS_TOKEN无效");

		return map;
	}

	public Map getResult(Integer code, String msg, Object data) {
		Map map = new HashMap();
		map.put("code", code);
		map.put("msg", msg);
		map.put("data", data);
		return map;

	}

	public Map getResult(Integer code, String msg) {
		Map map = new HashMap();
		map.put("code", code);
		map.put("msg", msg);
		map.put("data", msg);

		return map;

	}

	public Map getResult(String code, String msg) {
		Map map = new HashMap();
		map.put("code", code);
		map.put("msg", msg);
		map.put("data", msg);

		return map;

	}

	public Map getResult(Integer code, Object msg, Object data) {
		Map map = new HashMap();
		map.put("code", code);
		map.put("msg", msg);
		map.put("data", data);
		return map;

	}

	public Map getResult(String code, String msg, Object data) {
		Map map = new HashMap();
		map.put("code", code);
		map.put("msg", msg);
		map.put("data", data);
		return map;
	}

	public Map getResultOK(Object data, Integer all_page, Long count,
			Integer page, Integer size) {
		Map map = new HashMap();
		map.put("code", Code.OK);
		map.put("msg", Code.OK_S);
		map.put("data", data);
		map.put("all_page", all_page);
		map.put("count", count);
		map.put("page", page);
		map.put("size", size);
		return map;
	}

	public static Boolean isLogin(Integer user_id) {
		Boolean b = false;
		if (user_id != null && user_id > 0) {
			b = true;
		}
		return b;
	}

	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 保存支付日志
	 * 
	 * @param type
	 *            类型
	 * @param arrs
	 */
	protected void savePayLogs(String type, Object... arrs) {
		BasicDBObject bd = new BasicDBObject("_id", UUID.randomUUID()
				.toString());
		bd.append("timestamp", System.currentTimeMillis());
		bd.append("type", type);
		for (int i = 0; i < arrs.length; i++) {
			bd.append("P" + i, arrs[i]);
		}
		logMongo.getCollection("pay_logs").save(bd);
	}

	/**
	 * 保存支付日志
	 * 
	 * @param type
	 *            类型
	 * @param arrs
	 */
	protected void savePayLogs(String type, List arrs) {
		savePayLogs(type, arrs.toArray());
	}

	// 模糊查询
	protected Pattern getLikeStrPattern(String findStr) {
		Pattern pattern = Pattern.compile("^.*" + findStr + ".*$",
				Pattern.MULTILINE);
		return pattern;
	}

	// 模糊查询
	protected BasicDBObject getLikeStr(String findStr) {
		Pattern pattern = Pattern.compile("^.*" + findStr + ".*$",
				Pattern.MULTILINE);
		return new BasicDBObject("$regex", pattern);
	}

	// endWith文件扩展名
	protected BasicDBObject endWithStr(String findStr) {
		Pattern pattern = Pattern.compile(findStr + "$", Pattern.MULTILINE);
		return new BasicDBObject("$regex", pattern);
	}

	// startWith文件扩展名
	protected static BasicDBObject startWithStr(String findStr) {
		Pattern pattern = Pattern.compile("^" + findStr, Pattern.MULTILINE);
		return new BasicDBObject("$regex", pattern);
	}

	protected static String getProvinceCode(final String scode) {
		String code = null;
		if (StringUtils.isNotBlank(scode) && scode.length() > 5) {
			code = scode.substring(0, 5);
		}
		return code;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T clone(T obj) {
		T clonedObj = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			oos.close();
			ByteArrayInputStream bais = new ByteArrayInputStream(
					baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			clonedObj = (T) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clonedObj;
	}
	
	
	/**
	 * 根据手机号码获取用户ID
	 * @param phone
	 * @return userId 
	 */
	public Integer getUserIdByPhone(String phone) {
		DBObject user = qQUser().findOne(new BasicDBObject("username", phone));
		if(null==user || user.equals(null)){
			return null;
		}
		String tuid = (String) user.get("tuid");
		Integer userId = (Integer) users().findOne(new BasicDBObject("tuid", tuid)).get("_id");
		return userId;
	}
	
	
	/**
	 * 每日提问免费数量
	 * 
	 * @return 免费数量
	 */
	protected Integer topic_free_num_by_day(Integer user_id, boolean isStudent) {
		DBObject constantsTab = constants().findOne(new BasicDBObject("dr", 0));
		// if (isPrivs(user_id, Privs.学员)) {
		if (isStudent) {
			if (getIsZJStudent(user_id)) {
				if (null != constantsTab && null != constantsTab.get("topic_free_num_student_ZJ")) {
					return (Integer) constantsTab.get("topic_free_num_student_ZJ");
				}
				return 30;
			} else {//每日
				if (null != constantsTab && null != constantsTab.get("topic_free_num_student_by_day")) {
					return (Integer) constantsTab.get("topic_free_num_student_by_day");
				}
				return 10;
			}
		} else {
			if (null != constantsTab && null != constantsTab.get("topic_free_num_by_day")) {
				return (Integer) constantsTab.get("topic_free_num_by_day");
			}
			return 1;
		}
	}

	/**
	 * 每日提问免费数量-自考
	 *
	 * @return 免费数量
	 */
	protected Integer topic_free_num_by_day_zikao() {
		DBObject constantsTab = constants().findOne(new BasicDBObject("dr", 0));
		if (null != constantsTab && null != constantsTab.get("topic_free_num_by_day_zikao")) {
			return (Integer) constantsTab.get("topic_free_num_by_day_zikao");
		}
		return 1;
	}

	/**
	 * 剩余弹窗提示文案
	 * 
	 * @return 剩余弹窗提示文案
	 */
	protected String topic_is_show_text(boolean is_free, boolean isStudent) {
		DBObject constantsTab = constants().findOne(new BasicDBObject("dr", 0));
		if (null != constantsTab && null != constantsTab.get("topic_no_free_s_is_show_text")) {
			if (isStudent) {
				if (is_free) {
					return "";
				} else {
					return (String) constantsTab.get("topic_no_free_s_is_show_text");
				}
			} else {
				if (is_free) {
					return "";
				} else {
					return (String) constantsTab.get("topic_no_free_is_show_text");
				}
			}
		}
		return "";
	}

	/**
	 * 剩余弹窗提示文案-自考
	 *
	 * @return 剩余弹窗提示文案-自考
	 */
	protected String topic_is_show_text_zikao(boolean is_free) {
		DBObject constantsTab = constants().findOne(new BasicDBObject("dr", 0));
		if (null != constantsTab && null != constantsTab.get("topic_no_free_is_show_text_zikao")) {
			if (is_free) {
				return "";
			} else {
				return (String) constantsTab.get("topic_no_free_is_show_text_zikao");
			}
		}
		return "";
	}

	/**
	 * 剩余弹窗提示文案(tip)
	 * 
	 * @return 剩余弹窗提示文案(tip)
	 */
	protected String topic_is_show_text_tip(boolean is_free, boolean isStudent) {
		DBObject constantsTab = constants().findOne(new BasicDBObject("dr", 0));
		if (null != constantsTab && null != constantsTab.get("topic_no_free_is_show_text_tip")) {
			if (isStudent) {
				if (is_free) {
					return "";
				}
			} else {
				if (is_free) {
					return "";
				} else {
					return (String) constantsTab.get("topic_no_free_is_show_text_tip");
				}
			}
		}
		return "";
	}

	/**
	 * 剩余弹窗提示文案(tip)-自考
	 *
	 * @return 剩余弹窗提示文案(tip)-自考
	 */
	protected String topic_is_show_text_tip_zikao(boolean is_free) {
		DBObject constantsTab = constants().findOne(new BasicDBObject("dr", 0));
		if (null != constantsTab && null != constantsTab.get("topic_no_free_is_show_text_tip_zikao")) {
			if (is_free) {
				return "";
			} else {
				return (String) constantsTab.get("topic_no_free_is_show_text_tip_zikao");
			}
		}
		return "";
	}

	/**
	 * 尝试获取分布式锁
	 * @param jedis Redis客户端
	 * @param lockKey 锁
	 * @param requestId 请求标识
	 * @param expireTime 超期时间
	 * @return 是否获取成功
	 */
	public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {

		String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);

		if (LOCK_SUCCESS.equals(result)) {
			return true;
		}
		return false;
	}

	/**
	 * 释放分布式锁
	 * @param jedis Redis客户端
	 * @param lockKey 锁
	 * @param requestId 请求标识
	 * @return 是否释放成功
	 */
	public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {

		String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
		Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

		if (RELEASE_SUCCESS.equals(result)) {
			return true;
		}
		return false;
	}
}

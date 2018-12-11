package com.izhubo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.qq.open.OpenApiV3;
import com.izhubo.rest.AppProperties;
import com.izhubo.rest.common.util.JSONUtil;
import com.izhubo.model.UserType;

//@Service
public class QQRemindService {

	protected boolean openApiTest = false;
	protected String appServerProductDomain;
	protected String appServerTestDomain;
	protected String appServerDomain;
	protected String appid;
	protected String appkey;
	protected String apiDomain;
	private static Logger logger = LoggerFactory
			.getLogger(QQRemindService.class);

	@Autowired
	private MongoTemplate mainMongo;
	String user_name;
	String avatar_url;
	String message;

	static String secretKey = "771dc976aeb5ca07b8ab566687f44ad0";
	static String servername = "guang.qq.com";
	
	static int conTimeout = 60000;
	static int soTimeout = 60000;

	/**
	 * @param sdk
	 * @param openid
	 * @param openkey
	 * @param userip
	 * @param appid
	 * @param sig
	 * @param pf
	 * @return resp 错误返回 null
	 */
	public String remind(OpenApiV3 sdk, String pf, String openid,
			String openkey, String userip) {

		openApiTest = "1".equals(AppProperties.get("openapi.test"));
		appServerProductDomain = AppProperties.get("openapi.domain");
		appServerTestDomain = AppProperties.get("openapi.test.domain");

		appServerDomain = openApiTest ? appServerTestDomain
				: appServerProductDomain;

		appid = AppProperties.get("appid");
		appkey = AppProperties.get("appkey");

		OpenApiV3 sdk1 = new OpenApiV3(appid, appkey);
		sdk1.setServerName(appServerDomain);

		HashMap<String, String> remindStr = new HashMap<>();
		remindStr.put("openid", openid);
		remindStr.put("openkey", openkey);
		remindStr.put("pf", pf);
		remindStr.put("userip", userip);

		String resp = null;

		try {
			resp = sdk1.api("/v3/spread/is_reminder_set", remindStr, "https");
			JSONObject jo = new JSONObject(resp);
			if ((int) jo.get("ret") != 0) {
				logger.warn(" ret: {}, message:{},remindStr",
						(int) jo.get("ret"), jo.get("msg"), remindStr);
			}
		} catch (Exception e1) {
			logger.error("", e1);
		}
		return resp;
	}

	// 上线和下线通知接口
	public String anchorOnLineOrOffLineServive(Integer roomId, boolean on) {

		String resp = "";

		try {
			appid = AppProperties.get("appid");
			appkey = AppProperties.get("appkey");
			String appuid = String.valueOf(roomId);

			OpenApiV3 sdk2 = new OpenApiV3(appid, appkey);
			sdk2.setServerName(servername);

			HashMap<String, String> remindStr1 = new HashMap<>();
			
			Long a = System.currentTimeMillis()/1000;
		    int b =  (int) Math.floor(a);
		    String onlinetime = Integer.toString(b);
			//String onlinetime1 = Long.toString(System.currentTimeMillis()/1000);
			

			remindStr1.put("cmd", on ? "1": "2");
			remindStr1.put("appid", appid);
			remindStr1.put("appuid", appuid);
			remindStr1.put("onlinetime", onlinetime);

			logger.info("remindStr1: {}", remindStr1);

			try {
				resp = sdk2.api("/cgi-bin/widget/notice_import", remindStr1,
						"http", secretKey, false, conTimeout, soTimeout);
				logger.info("resp: {}", resp);
				JSONObject jo = new JSONObject(resp);
				if ((int) jo.get("ret") != 0) {
					logger.warn(" ret: {}, message:{},remindStr",
							(int) jo.get("ret"), jo.get("msg"), remindStr1);
				}
			} catch (Exception e1) {
				logger.error("", e1);
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		return resp;
	}

	
	public String synanchorAnchorInforAll() {
		List<DBObject> stars =  mainMongo.getCollection("users").find(new BasicDBObject("priv", UserType.教学老师.ordinal()), 
				new BasicDBObject("_id", 1)).toArray();
		int total = stars.size();
		int fail = 0;
		logger.info("start commit stars, total: {}", total);
		for(DBObject star : stars){
			Integer sid = (Integer) star.get("_id");
			boolean result = this.synanchorAnchorInfor(sid);
			if(!result){
				fail++;
			}
			logger.info("commit start id {}, result: {}", sid, result);
		}
		logger.info("finish commit stars, total: {}, fail: {}", total, fail);
		
		
		return "";
	}
	// 同步主播信息
	public boolean synanchorAnchorInfor(Integer roomId) {

		String resp = "";
		boolean result = false;
		try {
			int userid = roomId;
			BasicDBObject query_tuid = new BasicDBObject("_id", userid);
			DBObject userObj = mainMongo.getCollection("users").findOne(
					query_tuid);
			String username = (String) userObj.get("nick_name");
			if (username.length() > 7) {
				username = username.substring(0, 6);
			}
			String avatar_url = (String) userObj.get("pic");
			if (avatar_url != null && avatar_url.indexOf("qlogo.cn/") > -1
					&& avatar_url.indexOf("/50?") > -1) {
				avatar_url = avatar_url.replace("/50?", "/100?");
			}

			appid = AppProperties.get("appid");
			appkey = AppProperties.get("appkey");
			String appuid = String.valueOf(roomId);

			OpenApiV3 sdk3 = new OpenApiV3(appid, appkey);
			sdk3.setServerName(servername);

			HashMap<String, String> remindStr1 = new HashMap<>();

			remindStr1.put("cmd", "3");
			remindStr1.put("appid", appid);
			remindStr1.put("appuid", appuid);
			remindStr1.put("user_name", username);
			remindStr1.put("avatar_url", avatar_url);
//		    remindStr1.put("message", "");
			logger.info("remindStr1: {}", remindStr1);

			try {
				resp = sdk3.api("/cgi-bin/widget/notice_import", remindStr1,
						"http", secretKey, false, conTimeout, soTimeout);
				logger.info("resp: {}", resp);
				JSONObject jo = new JSONObject(resp);
				if ((int) jo.get("ret") != 0) {
					logger.warn(" ret: {}, message:{},remindStr",
							(int) jo.get("ret"), jo.get("msg"), remindStr1);
				}
				else {
					result = true;
				}
			} catch (Exception e1) {
				logger.error("", e1);
			}

		} catch (Exception e) {
			logger.error("", e);
		}

		return result;
	}
	
	public String anchorAuditInformationAll() {
		List<DBObject> stars =  mainMongo.getCollection("users").find(new BasicDBObject("priv", UserType.教学老师.ordinal()), 
				new BasicDBObject("_id", 1)).toArray();
		int total = stars.size();
		int passes = 0;
		logger.info("start getInfo stars, total: {}", total);
		for(DBObject star : stars){
			Integer sid = (Integer)star.get("_id");
			boolean pass = this.anchorAuditInformation(sid);
			if(pass){
				passes++;
			}
			logger.info("commit start id {}, pass: {}", sid, pass);
		}
		logger.info("finish commit stars, total: {}, passes: {}", total, passes);
		return "";
	}

	// 获取主播审核信息接口
	public boolean anchorAuditInformation(Integer roomId) {

		String resp = null;
		boolean pass = false;

		try {
			appid = AppProperties.get("appid");
			appkey = AppProperties.get("appkey");
			String appuid = String.valueOf(roomId);

			OpenApiV3 sdk4 = new OpenApiV3(appid, appkey);
			sdk4.setServerName(servername);

			HashMap<String, String> remindStr1 = new HashMap<>();

			remindStr1.put("cmd", "4");
			remindStr1.put("appid", appid);
			remindStr1.put("appuid", appuid);

			logger.info("remindStr1: {}", remindStr1);

			try {
				resp = sdk4.api("/cgi-bin/widget/notice_import", remindStr1,
						"http", secretKey, false, conTimeout, soTimeout);
				logger.info("resp: {}", resp);
//				JSONObject jo = new JSONObject(resp);
				Map<String, Object> map = JSONUtil.jsonToMap(resp);
				if ((int) map.get("ret") != 0) {
					logger.warn(" ret: {}, message:{},remindStr",
							(int) map.get("ret"), map.get("msg"), remindStr1);
				}
				else {
					pass = ((int)((Map)map.get("data")).get("status")) == 2;
					mainMongo.getCollection("users").update(new BasicDBObject("_id", roomId), 
							new BasicDBObject("$set", new BasicDBObject("passTxFollow", pass?1:0)));
					
				}
			} catch (Exception e1) {
				logger.error("", e1);
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		return pass;
	}

	/**
	 * @param sdk
	 * @param openid
	 * @param openkey
	 * @param appid
	 * @param sig
	 * @param pf
	 * @return resp 错误返回 null
	 */
	public String setremind(OpenApiV3 sdk, String pf, String openid,
			String openkey, String userip) {

		openApiTest = "1".equals(AppProperties.get("openapi.test"));
		appServerProductDomain = AppProperties.get("openapi.domain");
		appServerTestDomain = AppProperties.get("openapi.test.domain");

		appServerDomain = openApiTest ? appServerTestDomain
				: appServerProductDomain;

		appid = AppProperties.get("appid");
		appkey = AppProperties.get("appkey");
		OpenApiV3 sdk3 = new OpenApiV3(appid, appkey);
		sdk3.setServerName(appServerDomain);

		HashMap<String, String> remindStr2 = new HashMap<>();
		remindStr2.put("openid", openid);
		remindStr2.put("openkey", openkey);
		remindStr2.put("pf", pf);
		remindStr2.put("userip", userip);

		String resp = null;
		try {
			resp = sdk3.api("/v3/spread/set_reminder", remindStr2, "https");
			JSONObject jo = new JSONObject(resp);
			if ((int) jo.get("ret") != 0) {
				logger.warn(" ret: {}, message:{},remindStr",
						(int) jo.get("ret"), jo.get("msg"), remindStr2);
			}
		} catch (Exception e1) {
			logger.error("", e1);
		}
		return resp;
	}

	public MongoTemplate getMainMongo() {
		return mainMongo;
	}

	public void setMainMongo(MongoTemplate mainMongo) {
		this.mainMongo = mainMongo;
	}

}

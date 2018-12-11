package com.izhubo.userSystem.web;

import static com.izhubo.rest.common.doc.MongoKey.$setOnInsert;
import static com.izhubo.rest.common.doc.MongoKey._id;
import static com.izhubo.rest.common.doc.MongoKey.timestamp;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hqonline.model.HK;
import com.izhubo.rest.common.util.MsgDigestUtil;
import com.izhubo.userSystem.mongo.qquser.QQUser;
import com.izhubo.userSystem.mongo.qquser.QQUserRepositery;
import com.izhubo.userSystem.utils.AccScoreGainType;
import com.izhubo.userSystem.utils.JSONUtil;
import com.izhubo.userSystem.utils.ParamKey.Req;
import com.izhubo.userSystem.vo.SSOResult;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@Controller
public class LoginController {

	private static Logger logger = LoggerFactory
			.getLogger(LoginController.class);

	public static final String namespace = "q2-"; // 2字符必须是-
	public static final String RAN_TOKEN_SEED = "#@#qq"
			+ DateFormatUtils.format(System.currentTimeMillis(), "yMMdd")
			+ "%xi>YY";

	public static final String ACCESS_TOKEN_TO_USERNAME_KEY = "access_token_to_username";
	public static final String USERNAME_TO_ACCESS_TOKEN_KEY = "username_to_access_token";

	private static final String PRIVKEY = "nWLFRmgUd2CypottTm0d";

	@Resource
	protected MongoTemplate mainMongo;

	@Resource
	protected MongoTemplate logMongo;

	@Autowired
	protected QQUserRepositery qqUserRepositery;

	@Resource
	protected StringRedisTemplate mainRedis;
	
	@Resource
	protected UserService userService;

	@RequestMapping("/check")
	public Map<String, Object> check(HttpServletRequest req,
			HttpServletResponse res) {
		Map<String, Object> map = new HashMap<String, Object>();

		int user_id = Integer.valueOf(req.getParameter("user_id"));
		String password = req.getParameter("password");
		String md5 = req.getParameter("req_key").toLowerCase();

		String md5result = MsgDigestUtil.MD5.digest2HEX(
				user_id + password + PRIVKEY).toLowerCase();
		if (md5.equals(md5result)) {

			DBObject user = mainMongo.getCollection("users").findOne(
					new BasicDBObject("_id", user_id));
			if (user == null) {
				map.put("code", "30302");
				map.put("msg", "用户名或密码不正确!");
				return map;
			}
			String tuid = (String) user.get("tuid");

			List<QQUser> list;
			QQUser qqUser = null;
			if ((list = this.qqUserRepositery.findByTuid(tuid)).size() > 0) {
				qqUser = list.get(0);
			} else {
				map.put("code", "30302");
				map.put("msg", "用户名或密码不正确!");
				return map;
			}

			password = StringUtils.isNotBlank(password) ? MsgDigestUtil.MD5
					.digest2HEX(password) : password;
			if (!password.equals(qqUser.getPassword().toString())) {
				map.put("code", "-1");
				map.put("msg", "psw wrong");
			} else {
				map.put("code", "1");
				map.put("msg", "success");

			}
		} else {
			map.put("code", "-1");
			map.put("msg", "fail");
		}

		return map;
	}

	/**
	 * 学员登录接口
	 */
	@RequestMapping("/login")
	public Map<String, Object> login(HttpServletRequest req, HttpServletResponse res) {
		Map<String, Object> map = new HashMap<String, Object>();

		// TODO login
		String username = req.getParameter("user_name");
		String password = req.getParameter("password");
		Integer priv = 0;
		if (req.getParameter("priv") != null) {
			priv = Integer.valueOf(req.getParameter("priv"));
		}

		// latitude , longitude
		String latitudeString = req.getParameter("latitude");
		String longitudeString = req.getParameter("longitude");
		Double latitude = 0.0;
		Double longitude = 0.0;
		if (StringUtils.isNotBlank(latitudeString)) {
			latitude = Double.valueOf(latitudeString);
		}
		if (StringUtils.isNotBlank(longitudeString)) {
			longitude = Double.valueOf(longitudeString);
		}

		List<QQUser> list;
		QQUser qqUser = null;
		DBObject user = null;
		
		/*****************单点登录改造	BY lihaifei	20170825*******************/
		long startTime = System.currentTimeMillis();
		//password = StringUtils.isNotBlank(password) ? MsgDigestUtil.MD5.digest2HEX(password) : password;
		SSOResult loginResult = null;
		try {
			loginResult = userService.login(username, password);
			long afterSSOLogin = System.currentTimeMillis();
			System.out.println("SSO login cost : "+ (afterSSOLogin - startTime) + "ms");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String code = "";
		String msg = "";
		String token = "";
		if(loginResult != null && StringUtils.isNotBlank(loginResult.getCode())){
			code = loginResult.getCode();
			msg = loginResult.getMessage();
			if("200".equals(code)){
				//Map<String, Object> jsonToMap = JSONUtil.jsonToMap((String)loginResult.getData());
				JSONObject dataObj = JSONObject.fromObject(loginResult.getData());
				token = dataObj.getString("token");
			}else if ("402".equals(code)) {
				map.put("code", "30301");
				map.put("msg", "该用户尚未注册");
				return map;
			}else if ("403".equals(code)) {
				map.put("code", "30302");
				map.put("msg", "用户名或密码错误");
				return map;
			}else if ("411".equals(code)) {
				map.put("code", "10001");
				map.put("msg", "您的账号因违规已被封禁");
				return map;
			}else{
				logger.error("login error:username={},password={};SSO result code={},message={}",username,password,code,msg);
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
		}else{
			logger.error("login error:SSO login result is error.username={},password={}",username,password);
			map.put("code", "99999");
			map.put("msg", "登录出错");
			return map;
		}
		long beforeFind = System.currentTimeMillis();
		list = this.qqUserRepositery.findByUsername(username);
		long afterFind = System.currentTimeMillis();
		System.out.println("qqUserRepositery.findByUsername() cost : "+ (afterFind - beforeFind) + "ms");
		/************************测试***********************/
		/*if(list.size()>0){
			qqUser = list.get(0);
			DBCollection users = mainMongo.getCollection("users");
			BasicDBObject query_tuid = new BasicDBObject("tuid", qqUser.getTuid());
			this.qqUserRepositery.delete(qqUser);
			DBObject user_temp = users.findOne(query_tuid);
			users.remove(user_temp);
			list = this.qqUserRepositery.findByUsername(username);
		}*/
		/************************测试	END***********************/
		
		if (list.size() == 0) {
			long beforeSync = System.currentTimeMillis();
			System.out.println("start synUserToMongo...");
			//对于SSO中存在，但Mongodb中不存在的用户，需要把SSO的用户信息同步到Mongodb
			userService.synUserToMongo(token,username, password);
			long afterSycn = System.currentTimeMillis();
			System.out.println("synUserToMongo cost : "+ (afterSycn - beforeSync) + "ms");
			list = this.qqUserRepositery.findByUsername(username);
		}
		//判断用户
		//this.qqUserRepositery.findByUsernameAndPassword(username, password);
		if (list.size() > 0) {
			long beforeFindUserByTuid = System.currentTimeMillis();
			qqUser = list.get(0);
			user = mainMongo.getCollection("users").findOne(new BasicDBObject("tuid", qqUser.getTuid()));

			long afterFindUserByTuid = System.currentTimeMillis();
			System.out.println("findUserByTuid cost : "+ (afterFindUserByTuid - beforeFindUserByTuid) + "ms");
			
			//对于MongoDB里SSOUserId为空的用户，需要把SSO的userId同步到MongoDB
			if(user != null && user.get("sso_userid") == null){
				System.out.println("start synSSOUserId...");
				long beforesynSSOUserId = System.currentTimeMillis();
				userService.synSSOUserIdToMongo(token, qqUser.getTuid());
				long aftersynSSOUserId = System.currentTimeMillis();
				System.out.println("synSSOUserId cost : "+ (aftersynSSOUserId - beforesynSSOUserId) + "ms");
			}
			
			// 如果是具有学员权限的账号，允许登录
			if (Integer.valueOf(user.get("priv").toString()) == 3 ||
					// 有学员权限的教师
					(((Integer) user.get("priv0")) == 1 && Integer.valueOf(user.get("priv").toString()) == 2)
					|| Integer.valueOf(user.get("priv").toString()) == priv) {
				map.put("code", "1");
			} else {
				qqUser = null;
				user = null;
				map.put("code", "30300");
				map.put("msg", "未开通学员端");
			}

		} else {
			map.put("code", "30302");
			map.put("msg", "登陆出错！账号信息未同步到APP");
			return map;
		}

		if (qqUser != null && user != null) {
			if (user.get("status").toString().startsWith("true")) {
				
				long beforeRedisData = System.currentTimeMillis();
				
				//String access_token = this.getNamespace(qqUser.getQd()) + MsgDigestUtil.MD5.digest2HEX(RAN_TOKEN_SEED + username + Calendar.getInstance().get(Calendar.MILLISECOND)); // 改成token即时过期
				String access_token = token;
				Map<String, Object> map1 = JSONUtil.beanToMap(qqUser);

				map1.put("access_token", access_token);
				map1.put("user_id", user.get("_id").toString());
				map1.put("nick_name", user.get("nick_name").toString());
				
				 if (user.get("is_show_update") == null) {
					 map1.put("is_pic_update", 0);
				 } else {
					 map1.put("is_pic_update", user.get("is_show_update"));
				 }
				 
				// 暂时修复BUG，让isshowupdate为0，让客户端不显示
				map1.put("is_show_update", 0);
				// map1.put("expires_at", 18*60*60); //超时时间，单位秒，设定为 18*60*60
				map.put("data", map1);

				logger.info("login success username: {}, access_token: {}",username, access_token);

				// 如果之前有access_token，并且不相等，找到并删除掉，
				if (mainRedis.opsForHash().hasKey(USERNAME_TO_ACCESS_TOKEN_KEY,username)) {
					String oldToken = (String) mainRedis.opsForHash().get(USERNAME_TO_ACCESS_TOKEN_KEY, username);
					if (!access_token.equals(oldToken)) {
						mainRedis.opsForHash().delete(ACCESS_TOKEN_TO_USERNAME_KEY, oldToken);
						mainRedis.opsForHash().delete("token:" + oldToken,
								"priv", "_id", "nick_name", "status", "tag",
								"pic", "vlevel", "nc_id", "school_code",
								"privs", HK.LOGIN.PRIV0, HK.LOGIN.PRIV1,
								HK.LOGIN.PRIV2);
					}
				}
				// 写入username_to_access_token
				mainRedis.opsForHash().put(USERNAME_TO_ACCESS_TOKEN_KEY, username, access_token);
				// 写入access_token_to_username
				mainRedis.opsForHash().put(ACCESS_TOKEN_TO_USERNAME_KEY, access_token, username);
				// 记录登录日志
				day_login(req, Integer.valueOf(user.get("_id").toString()));
				// 更新坐标
				if (latitude != 0 && longitude != 0) {
					BasicDBObject updateDB = new BasicDBObject("latitude", latitude);
					updateDB.append("longitude", longitude);
					mainMongo.getCollection("users").update(new BasicDBObject("tuid", qqUser.getTuid()), new BasicDBObject("$set", updateDB));
				}
				String hash = HK.LOGIN.hash(access_token);
				// 类型
				mainRedis.opsForHash().put(hash, HK.LOGIN.PRIV, user.get("priv").toString());
				// ID
				mainRedis.opsForHash().put(hash, HK.LOGIN.ID, user.get("_id").toString());
				// 昵称
				mainRedis.opsForHash().put(hash, HK.LOGIN.NICK_NAME, user.get("nick_name"));
				mainRedis.opsForHash().put(hash, HK.LOGIN.STATUS, "1");
				mainRedis.opsForHash().put(hash, HK.LOGIN.TAG, "");
				// 头像
				mainRedis.opsForHash().put(hash, HK.LOGIN.PIC, user.get("pic"));
				// 权限
				mainRedis.opsForHash().put(hash, HK.LOGIN.PRIV0, user.get("priv0").toString());
				mainRedis.opsForHash().put(hash, HK.LOGIN.PRIV1, user.get("priv1").toString());
				mainRedis.opsForHash().put(hash, HK.LOGIN.PRIV2, user.get("priv2").toString());
				// nc_id
				if (user.get("nc_id") != null) {
					mainRedis.opsForHash().put(hash, HK.LOGIN.NC_ID, user.get("nc_id"));
				}
				if (user.get("school_code") != null) {
					mainRedis.opsForHash().put(hash, HK.LOGIN.SCHOOL_CODE, user.get("school_code"));
				}
				// 用户头像
				map1.put("pic", user.get("pic"));
				
				long afterRedisData = System.currentTimeMillis();
				System.out.println("RedisData cost : "+ (afterRedisData - beforeRedisData) + "ms");
			} else {
				map.put("code", "10001");
				map.put("msg", "您的账号因违规已被封禁");
			}
		}
		return map;
	}

	@RequestMapping("/tlogin")
	public Map<String, Object> tlogin(HttpServletRequest req, HttpServletResponse res) {
		Map<String, Object> map = new HashMap<String, Object>();

		// TODO login
		String username = req.getParameter("user_name");
		String password = req.getParameter("password");
		// Integer priv = Integer.valueOf(req.getParameter("priv"));
		// latitude , longitude
		String latitudeString = req.getParameter("latitude");
		String longitudeString = req.getParameter("longitude");
		Double latitude = 0.0;
		Double longitude = 0.0;
		if (StringUtils.isNotBlank(latitudeString)) {
			latitude = Double.valueOf(latitudeString);
		}
		if (StringUtils.isNotBlank(longitudeString)) {
			longitude = Double.valueOf(longitudeString);
		}

		List<QQUser> list;
		QQUser qqUser = null;
		
		/*****************单点登录改造	BY lihaifei	20170825*******************/
		SSOResult loginResult = null;
		try {
			loginResult = userService.login(username, password);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String code = "";
		String msg = "";
		String token = "";
		if(loginResult != null && StringUtils.isNotBlank(loginResult.getCode())){
			code = loginResult.getCode();
			msg = loginResult.getMessage();
			if("200".equals(code)){
				//Map<String, Object> jsonToMap = JSONUtil.jsonToMap((String)loginResult.getData());
				JSONObject dataObj = JSONObject.fromObject(loginResult.getData());
				token = dataObj.getString("token");
			}else if ("402".equals(code)) {
				map.put("code", "30301");
				map.put("msg", "该用户尚未注册");
				return map;
			}else if ("403".equals(code)) {
				map.put("code", "30302");
				map.put("msg", "用户名或密码错误");
				return map;
			}else if ("411".equals(code)) {
				map.put("code", "10001");
				map.put("msg", "您的账号因违规已被封禁");
				return map;
			}else{
				logger.error("login error:username={},password={};SSO result code={},message={}",username,password,code,msg);
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
		}else{
			logger.error("login error:SSO login result is error.username={},password={}",username,password);
			map.put("code", "99999");
			map.put("msg", "登录出错");
			return map;
		}
		
		//password = StringUtils.isNotBlank(password) ? MsgDigestUtil.MD5.digest2HEX(password) : password;
		// TODO //密码正确的前提下，必须是内部员工才能登录
		//list = this.qqUserRepositery.findByUsernameAndPassword(username,password);
		list = this.qqUserRepositery.findByUsername(username);
		
		if (list.size() == 0) {
			//对于SSO中存在，但Mongodb中不存在的用户，需要把SSO的用户信息同步到Mongodb,但是同步后priv=3,不能登陆教师端
			userService.synUserToMongo(token,username, password);
		}
		String privTemp = mainMongo.getCollection("users").findOne(new BasicDBObject("tuid", list.get(0).getTuid())).get("priv").toString();
		if (list.size() > 0 && Integer.valueOf(privTemp) == 2) {
			qqUser = list.get(0);
			map.put("code", "1");
		} else {
			map.put("code", "30302");
			map.put("msg", "您没有权限登陆恒企员工APP");
		}
		if (qqUser != null) {
			DBObject user = mainMongo.getCollection("users").findOne(new BasicDBObject("tuid", qqUser.getTuid()));
			
			//对于MongoDB里SSOUserId为空的用户，需要把SSO的userId同步到MongoDB
			if(user.get("sso_userid") == null){
				userService.synSSOUserIdToMongo(token, username);
				//对于MongoDB里SSOUserId为空的用户，需要把SSO的userId同步到MongoDB
				System.out.println("start synSSOUserId...");
				long beforesynSSOUserId = System.currentTimeMillis();
				userService.synSSOUserIdToMongo(token, qqUser.getTuid());
				long aftersynSSOUserId = System.currentTimeMillis();
				System.out.println("synSSOUserId cost : "+ (aftersynSSOUserId - beforesynSSOUserId) + "ms");
			}
			
			if (user.get("status").toString().startsWith("true")) {
				//String access_token = this.getNamespace(qqUser.getQd())+ MsgDigestUtil.MD5.digest2HEX(RAN_TOKEN_SEED + username + Calendar.getInstance().get(Calendar.MILLISECOND)); // 改成token即时过期
				String access_token = token;
				Map<String, Object> map1 = JSONUtil.beanToMap(qqUser);

				map1.put("access_token", access_token);
				map1.put("user_id", user.get("_id").toString());

				if (user.get("is_show_update") == null) {
					map1.put("is_show_update", 1);
				} else {
					map1.put("is_show_update", user.get("is_show_update"));
				}
				// map1.put("expires_at", 18*60*60); //超时时间，单位秒，设定为 18*60*60
				map.put("data", map1);

				logger.info("login success username: {}, access_token: {}", username, access_token);

				// 如果之前有access_token，并且不相等，找到并删除掉，
				if (mainRedis.opsForHash().hasKey(USERNAME_TO_ACCESS_TOKEN_KEY,username)) {
					String oldToken = (String) mainRedis.opsForHash().get(USERNAME_TO_ACCESS_TOKEN_KEY, username);
					if (!access_token.equals(oldToken)) {
						mainRedis.opsForHash().delete(ACCESS_TOKEN_TO_USERNAME_KEY, oldToken);
						mainRedis.opsForHash().delete("token:" + oldToken,
								"priv", "_id", "nick_name", "status", "tag",
								"pic", "vlevel", "nc_id", "school_code",
								"privs", HK.LOGIN.PRIV0, HK.LOGIN.PRIV1,
								HK.LOGIN.PRIV2);
					}
				}
				// 写入username_to_access_token
				mainRedis.opsForHash().put(USERNAME_TO_ACCESS_TOKEN_KEY, username, access_token);
				// 写入access_token_to_username
				mainRedis.opsForHash().put(ACCESS_TOKEN_TO_USERNAME_KEY, access_token, username);
				// 记录登录日志
				day_login(req, Integer.valueOf(user.get("_id").toString()));
				// 更新坐标
				if (latitude != 0 && longitude != 0) {
					BasicDBObject updateDB = new BasicDBObject("latitude", latitude);
					updateDB.append("longitude", longitude);
					mainMongo.getCollection("users").update(new BasicDBObject("tuid", qqUser.getTuid()), new BasicDBObject("$set", updateDB));
				}
				String hash = HK.LOGIN.hash(access_token);
				mainRedis.opsForHash().put(hash, HK.LOGIN.PRIV, user.get("priv").toString());
				mainRedis.opsForHash().put(hash, HK.LOGIN.ID, user.get("_id").toString());
				mainRedis.opsForHash().put(hash, HK.LOGIN.NICK_NAME, user.get("nick_name"));
				mainRedis.opsForHash().put(hash, HK.LOGIN.STATUS, "1");
				mainRedis.opsForHash().put(hash, HK.LOGIN.TAG, "");
				mainRedis.opsForHash().put(hash, HK.LOGIN.PIC, user.get("pic"));
				// 权限
				mainRedis.opsForHash().put(hash, HK.LOGIN.PRIV0, user.get("priv0").toString());
				mainRedis.opsForHash().put(hash, HK.LOGIN.PRIV1, user.get("priv1").toString());
				mainRedis.opsForHash().put(hash, HK.LOGIN.PRIV2, user.get("priv2").toString());
				// nc_id
				if (user.get("nc_id") != null) {
					mainRedis.opsForHash().put(hash, HK.LOGIN.NC_ID, user.get("nc_id"));
				}
				if (user.get("school_code") != null) {
					mainRedis.opsForHash().put(hash, HK.LOGIN.SCHOOL_CODE, user.get("school_code"));
				}
				// 用户头像
				map1.put("pic", user.get("pic"));
			} else {
				map.put("code", "10001");
				map.put("msg", "您的账号因违规已被封禁");
			}
		}
		return map;
	}

	public void day_login(HttpServletRequest req, Integer uid) {
		Date time = new Date();
		Long tmp = time.getTime();
		String id = new SimpleDateFormat("yyyyMMdd_").format(time) + uid;
		Map<String, Object> setOnInsert = new HashMap<>();
		setOnInsert.put("user_id", uid);
		setOnInsert.put(timestamp, tmp);
		String mobileId = req.getParameter(Req.uid);
		if (StringUtils.isNotBlank(mobileId)) {
			setOnInsert.put("uid", mobileId);
		}
		String ip = req.getHeader(Req.XFF);
		if (StringUtils.isBlank(ip)) {
			ip = req.getRemoteAddr();
		}
		setOnInsert.put("ip", ip);
		// 添加qd
		DBObject qdUser = mainMongo.getCollection("users").findOne(
				new BasicDBObject(_id, uid), new BasicDBObject("qd", 1));
		String qd = null;
		Object oqd = qdUser != null ? qdUser.get("qd") : null;
		if (null != oqd)
			qd = oqd.toString();
		if (StringUtils.isNotEmpty(qd))
			setOnInsert.put("qd", qd);

		logMongo.getCollection("day_login").findAndModify(
				new BasicDBObject(_id, id), null, null, false,
				new BasicDBObject($setOnInsert, setOnInsert), true, true); // upsert

		BasicDBObject update = new BasicDBObject("$set", new BasicDBObject(
				"islogin", 1));
		mainMongo.getCollection("users").findAndModify(
				new BasicDBObject("_id", uid), update);

		logMongo.getCollection("day_login").findAndModify(
				new BasicDBObject(_id, id), null, null, false,
				new BasicDBObject($setOnInsert, setOnInsert), true, true); // upsert

	}

	public String getNamespace(String qd) {
		if ("izhubo".equals(qd)) {
			return "i1-";
		} else {
			return namespace;
		}

	}

	// 积分预先就规定好的，直接调用即可
	public void PushScoreMsg(Integer userid, String nickname) {
		JSONObject jo = new JSONObject();
		jo.put("score_gain_type", AccScoreGainType.每日签到登录.ordinal());
		jo.put("user_id", userid);
		jo.put("score", 0);
		jo.put("nickname", nickname);
		jo.put("create_time", dateToStringNormal(new Date()));

		// messageProductorService.pushToMessageQueue("rabbit_queue_hqonline_score",
		// jo.toString());
	}

	public static String dateToStringNormal(Date time) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = formatter.format(time);

		return ctime;
	}

	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.DAY_OF_MONTH, 3);

		String result = MsgDigestUtil.MD5.digest2HEX("10180039" + "781028"
				+ PRIVKEY);
		System.out.println("cal " + result);
	}
}

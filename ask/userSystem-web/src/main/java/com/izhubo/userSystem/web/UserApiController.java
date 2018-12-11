package com.izhubo.userSystem.web;

import static com.izhubo.rest.common.doc.MongoKey.$setOnInsert;
import static com.izhubo.rest.common.doc.MongoKey._id;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.holders.StringHolder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

import com.izhubo.rest.AppProperties;
import com.izhubo.rest.common.util.JSONUtil;
import com.izhubo.rest.common.util.MsgDigestUtil;
import com.izhubo.rest.common.util.http.HttpClientUtil4_3;
import com.izhubo.rest.persistent.KGS;
import com.izhubo.userSystem.mongo.qquser.QQUser;
import com.izhubo.userSystem.mongo.qquser.QQUserRepositery;
import com.izhubo.userSystem.utils.AES;
import com.izhubo.userSystem.utils.NCUserState;
import com.izhubo.userSystem.utils.PhoneNumberUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.usercenter.webservice.SynchroMemberSoapProxy;

@Controller
public class UserApiController {

	// db.users.update( { } , { $set : { "area_name" : "","nc_id" :
	// "","nc_user_state" : 0,"school_code" : "","school_name" : ""}
	// },false,true );
	@Autowired
	protected QQUserRepositery qqUserRepositery;

	@Resource
	protected RegisterController registerController;

	@Resource
	protected MongoTemplate qquserMongo;

	@Resource
	protected MongoTemplate mainMongo;

	@Resource
	protected MongoTemplate logMongo;

	@Resource
	protected StringRedisTemplate mainRedis;

	@Resource
	protected AddressController addressController;

	public DBCollection logs() {
		return logMongo.getCollection("day_login");
	}

	
	
	// @Resource
	// private MessageProductor messageProductorService;

	@Resource
	KGS userKGS;

	private static final String AESKEY = "%^$AF>.12*******";

	private static final String ACAESKEY = "71bcbe39400d8328";

	private static final String HQZX_WEBSITEKEY = "hqzx";
	private String WebSiteKey = "answer";

	private int registertype = 10;

	private static Logger logger = LoggerFactory
			.getLogger(UserApiController.class);

	@RequestMapping("/api_register")
	public Map<String, Object> api_register(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		// HuanxinHelper hx = new HuanxinHelper();

		String MD5Result = req.getParameter("req_key");

		String area_name = req.getParameter("area_name");// 大区
		String province = req.getParameter("province");// 省份
		String city = req.getParameter("city");// 城市

		String school_code = req.getParameter("school_code");// 校区
		String school_name = req.getParameter("school_name");// 校区名称
		String nc_user_state = req.getParameter("nc_user_state"); // 1潜在,2成交,3暂停;
		String nc_id = req.getParameter("nc_id");

		// 先进行MD5验签 user_name+password+key 进行MD5 将MD5的结果作为req_key一起传过来。

		logger.info("注册原始字符 " + req.getParameter("website")
				+ req.getParameter("user_name") + req.getParameter("password")
				+ req.getParameter("area_name") + req.getParameter("province")
				+ req.getParameter("city") + req.getParameter("school_code")
				+ req.getParameter("school_name")
				+ req.getParameter("nc_user_state") + nc_id + AESKEY);

		String psw = req.getParameter("password");

		psw = psw.replace(" ", "+");

		logger.info("website: " + req.getParameter("website") + " user_name:"
				+ req.getParameter("user_name") + " area_name:" + area_name
				+ " province:" + province + " city:" + city + " school_code:"
				+ school_code + " school_name:" + school_name
				+ " nc_user_state:" + nc_user_state + " nc_id:" + nc_id);

		String md5str = req.getParameter("website")
				+ req.getParameter("user_name") + psw + area_name + province
				+ city + school_code + school_name + nc_user_state + nc_id
				+ AESKEY;
		// String MYMD5Result = MsgDigestUtil.MD5.MD5ForDotNet(md5str);
		String MYMD5Result = MsgDigestUtil.MD5.digest2HEX(md5str);

		logger.info("md5result:" + MYMD5Result);

		if (!MD5Result.toLowerCase().equals(MYMD5Result.toLowerCase())) {
			map.put("code", "-1");
			map.put("msg", "验签失败");
			return map;
		} else {

			String username = req.getParameter("user_name");
			String nickname = req.getParameter("nick_name");
			if (nickname == "" || nickname == null) {
				nickname = "kuida_" + username.substring(0, 2);
			}
			String password = "";
			try {
				password = AES.aesDecrypt(psw, AESKEY);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				map.put("code", UsError.UsernameHasExisted.getCode());
				map.put("msg", "注册失败，请联系对接开发者");
				return map;
			}
			String qd = req.getParameter("website");
			qd = "null".equals(qd) ? "kuaida" : qd;

			if (this.qqUserRepositery.findByUsername(username).size() > 0) {

				String tuid = this.qqUserRepositery.findByUsername(username)
						.get(0).getTuid();
				// 用户如果存在的话，这边也会去修改用户的校区和状态
				BasicDBObject update = new BasicDBObject("$set",
						new BasicDBObject("school_code", school_code)
								.append("school_name", school_name)
								.append("nc_user_state",
										Integer.valueOf(nc_user_state))
								.append("city", city)
								.append("province", province)
								.append("area_name", area_name)
								.append("nc_id", nc_id).append("qd", qd));

				mainMongo.getCollection("users").findAndModify(
						new BasicDBObject("tuid", tuid), update);

				map.put("code", UsError.UsernameHasExisted.getCode());
				map.put("msg", UsError.UsernameHasExisted.getMessage());

			} else {
				String temppsw = password;
				password = MsgDigestUtil.MD5.digest2HEX(password);
				String tuid = com.izhubo.rest.common.util.RandomUtil.getTuid();
				QQUser qqUser = new QQUser();
				qqUser.setNickName(nickname);
				qqUser.setPassword(password);
				qqUser.setQd(qd);
				qqUser.setTuid(tuid);
				qqUser.setUsername(username);
				this.qqUserRepositery.save(qqUser);

				HuanxinController hx = new HuanxinController();

				DBObject user = addUser(qqUser.getTuid(), qqUser.getNickName(),
						temppsw, username, area_name, province, city,
						school_code, school_name,
						Integer.valueOf(nc_user_state), nc_id, "", qd,null,null);

				hx.AddUser(user.get("_id").toString(), password, mainRedis);
				// 环信新增用户
				// hx.AddUser((String) user.get("_id"), password);

				map.put("code", "1");
				map.put("msg", "OK");

				// 正确，返回access_token
			}

			logger.info("结果code:" + map.get("code") + " 结果:" + map.get("msg"));

			return map;
		}
	}

	// N2kqRRvIZGWObQb9mPlPtA==
	@RequestMapping("/update_userncstate")
	public Map<String, Object> api_ac(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		// HuanxinHelper hx = new HuanxinHelper();
		String MD5Result = req.getParameter("req_key");

		String nc_id = req.getParameter("nc_id");
		String school_code = req.getParameter("school_code");// 校区
		String school_name = req.getParameter("school_name");// 校区名称
		String nc_user_state = req.getParameter("nc_user_state"); // 1潜在,2成交,3暂停;

		String area_name = "";// 大区
		String province = "";// 省份
		String city = "";// 城市

		logger.info("req_key: " + MD5Result + "nc_id:"
				+ req.getParameter("nc_id") + "school_code:" + school_code
				+ "school_name:" + school_name + "nc_user_state:"
				+ nc_user_state);

		// 先进行MD5验签 user_name+password+key 进行MD5 将MD5的结果作为req_key一起传过来。

		logger.info("编辑原始字符串 " + req.getParameter("nc_id")
				+ req.getParameter("school_code")
				+ req.getParameter("school_name")
				+ req.getParameter("nc_user_state") + AESKEY);

		String myMD5Result = MsgDigestUtil.MD5.digest2HEX(nc_id + school_code
				+ school_name + nc_user_state + AESKEY);

		if (!MD5Result.toLowerCase().equals(myMD5Result.toLowerCase())) {
			map.put("code", "-1");
			map.put("msg", "验签失败");
		} else {

			if (mainMongo.getCollection("users").count(
					new BasicDBObject("nc_id", nc_id)) > 0) {

				BasicDBObject update = new BasicDBObject("$set",
						new BasicDBObject("school_code", school_code)
								.append("school_name", school_name)
								.append("nc_user_state",
										Integer.valueOf(nc_user_state))
								.append("city", city)
								.append("province", province)
								.append("area_name", area_name)
								.append("nc_id", nc_id).append("qd", "nc"));

				mainMongo.getCollection("users").findAndModify(
						new BasicDBObject("nc_id", nc_id), update);

				map.put("code", "1");
				map.put("msg", "修改成功");

			} else {
				map.put("code", "2");
				map.put("msg", "用户不存在修改失败");
			}
		}
		logger.info("结果code:" + map.get("code") + " 结果:" + map.get("msg"));
		return map;

	}

	@RequestMapping("/updateusercity")
	public Map<String, Object> updateusercity(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		// HuanxinHelper hx = new HuanxinHelper();

		List<DBObject> users = qquserMongo.getCollection("qQUser").find()
				.toArray();

		for (int i = 0; i < users.size(); i++) {
			DBObject user = users.get(i);
			String tuid = (String) user.get("tuid");
			String userphone = (String) user.get("username");

			// 先判断用户名是否是电话号码
			if (PhoneNumberUtil.veriyMobile(userphone)) {
				Map<String, String> usermap = addressController
						.addressByMobile(userphone);

				BasicDBObject update = new BasicDBObject("$set",
						new BasicDBObject("province", usermap.get("province"))
								.append("city", usermap.get("city")));

				mainMongo.getCollection("users").findAndModify(
						new BasicDBObject("tuid", tuid), update);

			}

			System.out.println("updatecount: " + i);
		}

		return map;

	}

	@RequestMapping("/updateuserqd")
	public Map<String, Object> updateuserqd(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		// HuanxinHelper hx = new HuanxinHelper();

		List<DBObject> users = qquserMongo.getCollection("qQUser").find()
				.toArray();

		for (int i = 0; i < users.size(); i++) {
			DBObject user = users.get(i);
			String tuid = (String) user.get("tuid");
			String qd = (String) user.get("qd");

			if (StringUtils.isNotEmpty(qd) || StringUtils.isNotBlank(qd)) {

			} else {
				qd = "kuaida";
			}

			BasicDBObject update = new BasicDBObject("$set", new BasicDBObject(
					"qd", qd));

			mainMongo.getCollection("users").findAndModify(
					new BasicDBObject("tuid", tuid), update);

			System.out.println("updatecount: " + i);
		}

		return map;

	}

	// @RequestMapping("/updatencfromcopy")
	// public Map<String, Object> updatencfromcopy(HttpServletRequest req,
	// HttpServletResponse res) throws Exception {
	// Map<String, Object> map = new HashMap<String, Object>();
	//
	// // HuanxinHelper hx = new HuanxinHelper();
	//
	// List<DBObject> users = mainMongo.getCollection("copy_user").find(new
	// BasicDBObject("nc_user_state", "已成交"))
	// .toArray();
	//
	// for (int i = 0; i < users.size(); i++) {
	// DBObject user = users.get(i);
	// String nc_id = (String) user.get("nc_id");
	// String phone = user.get("phone").toString();
	//
	// JSONObject jo = new JSONObject();
	// jo.put("phone", phone);
	// jo.put("nc_id", nc_id);
	//
	//
	// messageProductorService.pushToMessageQueue("rabbit_queue_hqonline_nc_userpk_pro",
	// jo.toString());
	// }
	// return map;
	//
	//
	// }
	//

	@RequestMapping("/api_register_fast")
	public Map<String, Object> api_register_fast(HttpServletRequest req,
			HttpServletResponse res) throws Exception {

		// HuanxinHelper hx = new HuanxinHelper();

		String json = req.getParameter("json");
		String priv = req.getParameter("priv");
		JSONArray jo = new JSONArray();

		jo = jo.fromObject(json);

		JSONArray huanxinJSON = new JSONArray();

		Map<String, Object> resultMap = new HashMap<String, Object>();

		String result = "";
		for (int i = 0; i < jo.size(); i++) {
			String map = "";
			JSONObject item = jo.getJSONObject(i);
			String username = (String) item.get("username");
			String nickname = (String) item.get("username");// 昵称和用户名一样啦
			String password = (String) item.get("userpass");
			String qd = "kjcity";

			qd = "null".equals(qd) ? null : qd;

			if (this.qqUserRepositery.findByUsername(username).size() > 0) {

				// HuanxinController hx = new HuanxinController();
				//
				// QQUser qqUser =
				// this.qqUserRepositery.findByUsername(username).get(0);
				// DBObject user = mainMongo.getCollection("users").findOne(new
				// BasicDBObject("tuid" ,qqUser.getTuid()));
				// hx.AddUser(user.get("_id").toString(),hx.ConventPasswordToMD5(qqUser.getPassword()),
				// mainRedis);

				map = UsError.UsernameHasExisted.getCode();
			} else {

				try {
					password = AES.aesDecrypt(password, AESKEY);
				} catch (Exception e) {
					// TODO Auto-generated catch block

					map = UsError.UsernameHasExisted.getCode();
				}
				String temppsw = password;
				password = MsgDigestUtil.MD5.digest2HEX(password);

				QQUser qqUser = new QQUser();
				qqUser.setNickName(nickname);
				qqUser.setPassword(password);
				qqUser.setQd(qd);
				qqUser.setTuid(UUID.randomUUID().toString());
				qqUser.setUsername(username);
				this.qqUserRepositery.save(qqUser);

				DBObject user;
				if (priv.equals("2")) {
					// user = addTeacher(qqUser.getTuid(), qqUser.getNickName(),
					// temppsw);
				} else {
					user = addUser(qqUser.getTuid(), qqUser.getNickName(),
							temppsw, username, "", "", "", "", "",
							NCUserState.默认.ordinal(), qqUser.getTuid(), "",
							"kuaida",null,null);
				}

				// HuanxinController hx = new HuanxinController();
				// JSONObject huanxinAdd = new JSONObject();
				// huanxinAdd.put("username", user.get("_id").toString());
				// huanxinAdd.put("password",
				// hx.ConventPasswordToMD5(password));
				// huanxinJSON.add(huanxinAdd);
				map = "1";

				// 正确，返回access_token
			}

			// 暂时注释掉，上线的时候再导入

			result = result + "\r\n";
			result = result + username + ":" + map;
			// resultMap.put(username, map);

		}

		// HuanxinController hx = new HuanxinController();
		// hx.AddUser(huanxinJSON.toString(), mainRedis);
		resultMap.put("data", result);

		return resultMap;

	}

	@RequestMapping("/api_register_addteacher")
	public Map<String, Object> api_register_addteacher(HttpServletRequest req,
			HttpServletResponse res) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		String json = URLDecoder.decode(req.getParameter("json").toString(),
				"utf-8");
		
		logger.info("api_register_addteacher: "+json);

		String map = "";
		JSONObject item = new JSONObject();
		item = item.fromObject(json);
		String username = (String) item.get("teacher_mobile");
		String nc_id = "";
		if( !item.get("teacher_nc_id").equals(net.sf.json.JSONNull.getInstance()))
		{
			nc_id = (String) item.get("teacher_nc_id");
		}
		String sm_user_id = (String) item.get("sm_user_id");
		String school_code = (String) item.get("school_nc_code");
		String teacher_name = (String) item.get("teacher_name");
		String post_name = (String) item.get("post_name");
		String idpassString = (String) item.get("cardend");
		// 人事信息是否启用。1为未启用，2为启用，3为停用
		int enablestate = Integer.valueOf(item.get("enablestate").toString());

	

		String pic = "http://answerimg.kjcity.com/default_teacher.png";

		String qd = "nc";

		qd = "null".equals(qd) ? null : qd;

		//只接受有效的员工，停用和离职的不处理
		if (enablestate == 2) {

			if (this.qqUserRepositery.findByUsername(username).size() > 0) {

				// HuanxinController hx = new HuanxinController();
				//
				QQUser qqUser = this.qqUserRepositery.findByUsername(username)
						.get(0);
				updateTeacher(qqUser.getTuid(), teacher_name, nc_id,
						sm_user_id, school_code, post_name);
				map = UsError.UsernameHasExisted.getCode();

			} else {

				String temppsw = idpassString;
				String password = MsgDigestUtil.MD5.digest2HEX(temppsw);

				QQUser qqUser = new QQUser();
				qqUser.setNickName(teacher_name);
				qqUser.setPassword(password);
				qqUser.setQd(qd);
				qqUser.setTuid(UUID.randomUUID().toString());
				qqUser.setUsername(username);
				this.qqUserRepositery.save(qqUser);

				DBObject user;

	

				user = addTeacher(qqUser.getTuid(), qqUser.getNickName(),
						temppsw, pic, nc_id, sm_user_id, school_code, post_name);

				HuanxinController hx = new HuanxinController();
				hx.AddUser(user.get("_id").toString(), password, mainRedis);

				map = "1";

				// 正确，返回access_token
			}
		}

		// 暂时注释掉，上线的时候再导入

		resultMap.put("data", username + ":" + map);

		return resultMap;

	}

	@RequestMapping("/api_register_fast_teacher")
	public Map<String, Object> api_register_fast_teacher(
			HttpServletRequest req, HttpServletResponse res) throws Exception {

		// HuanxinHelper hx = new HuanxinHelper();

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String json = req.getParameter("json");

		String map = "";
		JSONObject item = new JSONObject();
		item = item.fromObject(json);
		String username = (String) item.get("username");
		String nickname = (String) item.get("nickname");// 昵称和用户名一样啦
		String password = (String) item.get("password");
		String pic = "http://kuaida.kjcity.com/teacher/"
				+ (String) item.get("pic");
		JSONArray ljson = (JSONArray) item.get("user_industry");

		String qd = "kjcity";

		qd = "null".equals(qd) ? null : qd;

		if (this.qqUserRepositery.findByUsername(username).size() > 0) {

			// HuanxinController hx = new HuanxinController();
			//
			QQUser qqUser = this.qqUserRepositery.findByUsername(username).get(
					0);
			// DBObject user = mainMongo.getCollection("users").findOne(new
			// BasicDBObject("tuid" ,qqUser.getTuid()));
			// hx.AddUser(user.get("_id").toString(),hx.ConventPasswordToMD5(qqUser.getPassword()),
			// mainRedis);

			// 更新用户基本信息
			updateTeacher(qqUser.getTuid(), nickname, ljson.toString());

			map = UsError.UsernameHasExisted.getCode();
		} else {

			String temppsw = password;
			password = MsgDigestUtil.MD5.digest2HEX(password);

			QQUser qqUser = new QQUser();
			qqUser.setNickName(nickname);
			qqUser.setPassword(password);
			qqUser.setQd(qd);
			qqUser.setTuid(UUID.randomUUID().toString());
			qqUser.setUsername(username);
			this.qqUserRepositery.save(qqUser);

			DBObject user;

			user = addTeacher(qqUser.getTuid(), qqUser.getNickName(), temppsw,
					pic, ljson.toString());

			HuanxinController hx = new HuanxinController();
			hx.AddUser(user.get("_id").toString(), password, mainRedis);

			map = "1";

			// 正确，返回access_token
		}

		// 暂时注释掉，上线的时候再导入

		resultMap.put("data", username + ":" + map);

		return resultMap;

	}

	@RequestMapping("/api_register_fast_teacher_forone")
	public Map<String, Object> api_register_fast_teacher_forone(
			HttpServletRequest req, HttpServletResponse res) throws Exception {

		// HuanxinHelper hx = new HuanxinHelper();

		Map<String, Object> resultMap = new HashMap<String, Object>();

		DBObject tlistDbObject = mainMongo.getCollection("teachertemp").find()
				.toArray().get(0);
		String json = tlistDbObject.get("list").toString();

		JSONArray ja = new JSONArray();

		ja = JSONArray.fromObject(json);
		for (int i = 0; i < ja.size(); i++) {

			String map = "";
			JSONObject item = ja.getJSONObject(i);

			String username = (String) item.get("phone");

			String nickname = (String) item.get("nick_name");// 昵称和用户名一样啦
			String password = "123456";
			String province = (String) item.get("province");
			String area = (String) item.get("area");
			String school = (String) item.get("school");
			String person_num = (String) item.get("person_num");

			if (this.qqUserRepositery.findByUsername(username).size() > 0) {

				// HuanxinController hx = new HuanxinController();
				//
				QQUser qqUser = this.qqUserRepositery.findByUsername(username)
						.get(0);
				// DBObject user = mainMongo.getCollection("users").findOne(new
				// BasicDBObject("tuid" ,qqUser.getTuid()));
				// hx.AddUser(user.get("_id").toString(),hx.ConventPasswordToMD5(qqUser.getPassword()),
				// mainRedis);
				// String tuid,
				// String nick_name,
				// String person_num,
				// String province,
				// String area,
				// String school_name,
				// String insJson
				// 更新用户基本信息
				// updateTeacher(qqUser.getTuid(), nickname, ljson.toString());
				updateTeacherfornc(qqUser.getTuid(), nickname, person_num,
						province, area, school, "");
				map = UsError.UsernameHasExisted.getCode();
			} else {

				String temppsw = password;
				password = MsgDigestUtil.MD5.digest2HEX(password);

				String qd = "nc";

				QQUser qqUser = new QQUser();
				qqUser.setNickName(nickname);
				qqUser.setPassword(password);
				qqUser.setQd(qd);
				qqUser.setTuid(com.izhubo.rest.common.util.RandomUtil.getTuid());
				qqUser.setUsername(username);
				this.qqUserRepositery.save(qqUser);

				DBObject user;

				user = addTeacherfromnc(qqUser.getTuid(), qqUser.getNickName(),
						temppsw, "", person_num, province, area, school, "");

				HuanxinController hx = new HuanxinController();
				hx.AddUser(user.get("_id").toString(), password, mainRedis);

				map = "1";

				// 正确，返回access_token
			}

			// 暂时注释掉，上线的时候再导入

			resultMap.put("data", username + ":" + map);

		}

		return resultMap;

	}

	@RequestMapping("/api_register_huanxin")
	public Map<String, Object> api_register_huanxin(HttpServletRequest req,
			HttpServletResponse res) throws Exception {

		// HuanxinHelper hx = new HuanxinHelper();

		String json = req.getParameter("json");
		String priv = req.getParameter("priv");
		JSONArray jo = new JSONArray();

		jo = jo.fromObject(json);

		JSONArray huanxinJSON = new JSONArray();

		Map<String, Object> resultMap = new HashMap<String, Object>();

		String result = "";
		for (int i = 0; i < jo.size(); i++) {
			String map = "";
			JSONObject item = jo.getJSONObject(i);
			String username = (String) item.get("username");
			String nickname = (String) item.get("username");// 昵称和用户名一样啦
			String password = (String) item.get("userpass");
			String qd = "kjcity";

			qd = "null".equals(qd) ? null : qd;

			if (this.qqUserRepositery.findByUsername(username).size() > 0) {

				HuanxinController hx = new HuanxinController();

				QQUser qqUser = this.qqUserRepositery.findByUsername(username)
						.get(0);
				DBObject user = mainMongo.getCollection("users").findOne(
						new BasicDBObject("tuid", qqUser.getTuid()));
				try {
					hx.AddUserEX(user.get("_id").toString(),
							hx.ConventPasswordToMD5(qqUser.getPassword()),
							mainRedis);
					map = "1";
				} catch (Exception e) {

					map = "400";
				}
			} else {

				// 正确，返回access_token
			}

			// 暂时注释掉，上线的时候再导入

			result = result + "\r\n";
			result = result + username + ":" + map;
			// resultMap.put(username, map);

		}

		// HuanxinController hx = new HuanxinController();
		// hx.AddUser(huanxinJSON.toString(), mainRedis);
		resultMap.put("data", result);

		return resultMap;

	}

	@RequestMapping("/api_update_userpwd")
	public Map<String, Object> api_update_userpwd(HttpServletRequest req,
			HttpServletResponse res) {
		Map<String, Object> map = new HashMap<String, Object>();

		// HuanxinHelper hx = new HuanxinHelper();

		String MD5Result = req.getParameter("req_key");

		// 先进行MD5验签 user_name+password+key 进行MD5 将MD5的结果作为req_key一起传过来。

		System.out.println("修改密码原始字符 " + req.getParameter("user_name")
				+ req.getParameter("old_password")
				+ req.getParameter("new_password") + AESKEY);

		System.out.println(req.getParameter("user_name") + "|"
				+ req.getParameter("old_password") + "|"
				+ req.getParameter("new_password") + "|"
				+ req.getParameter("req_key"));

		String oldpassword = req.getParameter("old_password");
		String newpassword = req.getParameter("new_password");

		oldpassword = oldpassword.replace(" ", "+");
		newpassword = newpassword.replace(" ", "+");

		String MYMD5Result = MsgDigestUtil.MD5
				.MD5ForDotNet(req.getParameter("user_name") + oldpassword
						+ newpassword + AESKEY);

		System.out.println("MD5结果" + MYMD5Result);

		if (!MD5Result.toLowerCase().equals(MYMD5Result.toLowerCase())) {
			map.put("code", "-1");
			map.put("msg", "验签失败");
			return map;
		} else {

			String username = req.getParameter("user_name");

			try {
				oldpassword = AES.aesDecrypt(oldpassword, AESKEY);
				newpassword = AES.aesDecrypt(newpassword, AESKEY);

				List<QQUser> list = qqUserRepositery.findByUsername(username);
				// 去掉修改密码的验证，因为内部站点可以信任
				// if (list.size() == 0) {
				//
				// map.put("code", "99");
				// map.put("msg", "密码错误，更改失败");
				// return map;
				// }
				QQUser qqUser = list.get(0);

				String password_md5 = MsgDigestUtil.MD5.digest2HEX(newpassword);
				BasicDBObject update = new BasicDBObject("$set",
						new BasicDBObject("password", password_md5));
				qquserMongo.getCollection("qQUser").findAndModify(
						new BasicDBObject("tuid", qqUser.getTuid()), update);

				// 同步修改环信密码
				DBObject user = mainMongo.getCollection("users").findOne(
						new BasicDBObject("tuid", qqUser.getTuid()));
				HuanxinController hx = new HuanxinController();
				hx.UpdatePassword(user.get("_id").toString(), password_md5,
						mainRedis);

				// 同步修改用户的AES加密密码。
				SetAcKey(Integer.valueOf(user.get("_id").toString()),
						newpassword);

				map.put("code", "1");
				map.put("msg", "修改密码成功");
				return map;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Exception: " + e.toString());
				map.put("code", UsError.UsernameHasExisted.getCode());
				map.put("msg", "更新密码失败，请联系对接开发者");
				return map;
			}

		}
	}

	// public boolean UserNameContainByKjCity(String userName) {
	//
	// try {
	//
	// SynchroMemberSoapProxy ss = new SynchroMemberSoapProxy();
	// String MYMD5Result = MsgDigestUtil.MD5.MD5ForDotNet(userName
	// + AESKEY);
	// MemberModel model = ss.getLoginName(userName, MYMD5Result);
	//
	// if (model.getUsername() != null) {
	// return true;
	// } else {
	// return false;
	// }
	// } catch (Exception e) {
	// return false;
	// }
	//
	// }

	public static final String ACCESS_TOKEN_TO_USERNAME_KEY = "access_token_to_username";
	public static final String USERNAME_TO_ACCESS_TOKEN_KEY = "username_to_access_token";

	public boolean AddUserToKjcity(String userName, String pwd, String tuid) {
		try {
			SynchroMemberSoapProxy ss = new SynchroMemberSoapProxy();
			pwd = AES.aesEncrypt(pwd, AESKEY);
			String MYMD5Result = MsgDigestUtil.MD5.MD5ForDotNet(WebSiteKey + userName + pwd + AESKEY).toUpperCase();
			StringHolder holder = new StringHolder("");
			boolean addsuccess = ss.addUser(WebSiteKey, userName, pwd, tuid, registertype, MYMD5Result, holder);

			return addsuccess;
		} catch (Exception e) {
			return false;
		}

	}

	public Boolean addUserLogin(String username, String tuid) {
		Boolean result = null;
		try {

			com.usercenter.webservice.SynchroMemberSoapProxy ss = new com.usercenter.webservice.SynchroMemberSoapProxy();
			String MYMD5Result = MsgDigestUtil.MD5.MD5ForDotNet(
					HQZX_WEBSITEKEY + username + tuid + AESKEY).toUpperCase();
			result = ss.addUserLogin(HQZX_WEBSITEKEY, username, tuid,
					MYMD5Result);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("/redirect_to_other")
	public Map<String, Object> redirect_to_other(HttpServletRequest req,
			HttpServletResponse res) {

		// String access_token = req.getParameter("access_token");
		// String rd_url = req.getParameter("rd_url");
		// String username = (String) mainRedis.opsForHash().get(
		// LoginController.ACCESS_TOKEN_TO_USERNAME_KEY, access_token);

		String access_token = req.getParameter("access_token");
		String rd_url = req.getParameter("rd_url");
		String username = (String) mainRedis.opsForHash().get(
				LoginController.ACCESS_TOKEN_TO_USERNAME_KEY, access_token);

		List<QQUser> list = qqUserRepositery.findByUsername(username);
		if (list.size() > 0) {
			QQUser qqUser = list.get(0);
			addUserLogin(username, qqUser.getTuid());
			Cookie cookie = new Cookie("kjcity_SessionID", qqUser.getTuid());
			cookie.setPath("/");
			String host = (String) req.getHeader("Host");
			host = host.substring(host.indexOf("."),host.length());
			cookie.setDomain(host);
			
			
			Cookie cookietoken = new Cookie("access_token", access_token);
			cookietoken.setPath("/");
			cookietoken.setDomain(host);

			try {
				res.addCookie(cookie);
				res.addCookie(cookietoken);
				res.sendRedirect(rd_url);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;

	}
	
	
	/**
	 * 用于会计新PC官网学习中心跳转到旧的会计PC官网(临时使用)
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("/redirect_to_other_4old_pc")
	public Map<String, Object> redirect_to_other_4old_pc(HttpServletRequest req, HttpServletResponse res) {

		String access_token = req.getParameter("access_token");
		String username = req.getParameter("username");//13824429749,5Qvo+IU80gWoglcatq7uww==,NVF2bytJVTgwZ1dvZ2xjYXRxN3V3dz09
		String rd_url = req.getParameter("rd_url");
		if(StringUtils.isBlank(username)){
			return null;
		}
		if(StringUtils.isBlank(rd_url)){
			rd_url = "http://kuaijiold.hqjy.com/UserManage/studyCenter.html";//旧的会计PC学习中心
		}
		try {
			username = URLDecoder.decode(username, "UTF-8");
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		
		//获取手机号
		try {
			username = AES.aesDecrypt_hex(username, "06k9bCEpUAKeL9I3");//AES解密，秘钥为06k9bCEpUAKeL9I3
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		//username = "13824429749";
		
		List<QQUser> list = qqUserRepositery.findByUsername(username);
		if (list.size() > 0) {
			QQUser qqUser = list.get(0);
			addUserLogin(username, qqUser.getTuid());
			Cookie cookie = new Cookie("kjcity_SessionID", qqUser.getTuid());
			cookie.setPath("/");
			String host = (String) req.getHeader("Host");
			//host = host.substring(host.indexOf("."),host.length());
			host = "hqjy.com";
			cookie.setDomain(host);
			
			
			/*Cookie cookietoken = new Cookie("access_token", access_token);
			cookietoken.setPath("/");
			cookietoken.setDomain(host);*/

			try {
				res.addCookie(cookie);
				//res.addCookie(cookietoken);
				res.sendRedirect(rd_url);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;

	}
	
	
	@SuppressWarnings("deprecation")
	@RequestMapping("/getmemberid")
	public Map<String, Object> getmemberid(HttpServletRequest req,
			HttpServletResponse res) {

		// String access_token = req.getParameter("access_token");
		// String rd_url = req.getParameter("rd_url");
		// String username = (String) mainRedis.opsForHash().get(
		// LoginController.ACCESS_TOKEN_TO_USERNAME_KEY, access_token);
		String domain = AppProperties.get("kjcityapi.domain").toString();

		String access_token = req.getParameter("access_token");
		String username = (String) mainRedis.opsForHash().get(
				LoginController.ACCESS_TOKEN_TO_USERNAME_KEY, access_token);
		//http://177.77.77.118:12001/api/account/getmemberid?username=123456@gmail.com
		List<QQUser> list = qqUserRepositery.findByUsername(username);
		if (list.size() > 0) {
			QQUser qqUser = list.get(0);
			String resultString ="";
			try {
				resultString = HttpClientUtil4_3.get(domain+"/api/account/getmemberid?username="+username, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Map<String, Object> map = new HashMap<String, Object>();
			DBObject user = mainMongo.getCollection("users").findOne(new BasicDBObject("tuid", qqUser.getTuid()), new BasicDBObject("_id", 1));
			map.put("uid", user.get("_id"));
			resultString = URLEncoder.encode(resultString);
			map.put("memberid", resultString);
			map.put("code", "1");
			return map;
		}

		return null;

	}
	

	@RequestMapping("/getuserid")
	public Map<String, Object> getuserid(HttpServletRequest req,
			HttpServletResponse res) {

		// String access_token = req.getParameter("access_token");
		// String rd_url = req.getParameter("rd_url");
		// String username = (String) mainRedis.opsForHash().get(
		// LoginController.ACCESS_TOKEN_TO_USERNAME_KEY, access_token);


		String username =URLDecoder.decode(req.getParameter("p"));
		username =username.replace(" ", "+");
		try {
			username = AES.aesDecrypt(username, AESKEY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//http://177.77.77.118:12001/api/account/getmemberid?username=123456@gmail.com
		List<QQUser> list = qqUserRepositery.findByUsername(username);
		if (list.size() > 0) {
			QQUser qqUser = list.get(0);
			
			BasicDBObject query_tuid = new BasicDBObject("tuid", qqUser.getTuid());
			DBObject user = mainMongo.getCollection("users").findOne(query_tuid);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user_id", user.get("_id"));
			map.put("code", "1");
			return map;
		}

		return null;

	}

	@RequestMapping("/closeteacher")
	public Map<String, Object> closeteacher(HttpServletRequest req,
			HttpServletResponse res) {
		String username = req.getParameter("username");
		String nc_id = req.getParameter("nc_id");
		String MD5Result = req.getParameter("key");

		Map<String, Object> map = new HashMap<String, Object>();
		List<QQUser> list;
		QQUser qqUser = null;

		String MYMD5Result = MsgDigestUtil.MD5.MD5ForDotNet(username + nc_id
				+ AESKEY);

		System.out.println("MD5结果" + MYMD5Result);

		if (!MD5Result.toLowerCase().equals(MYMD5Result.toLowerCase())) {
			map.put("code", "-1");
			map.put("msg", "验签失败");
			return map;
		} else {
			if ((list = this.qqUserRepositery.findByUsername(username)).size() > 0) {
				qqUser = list.get(0);
				DBCollection users = mainMongo.getCollection("users");
				BasicDBObject query_id = new BasicDBObject("tuid",
						qqUser.getTuid());
				BasicDBObject update = new BasicDBObject("$set",
						new BasicDBObject("priv", 3).append(
								"close_reason", "leave"));
				users.findAndModify(query_id, update);

				if (mainRedis.opsForHash().hasKey(USERNAME_TO_ACCESS_TOKEN_KEY,
						username)) {
					String oldToken = (String) mainRedis.opsForHash().get(
							USERNAME_TO_ACCESS_TOKEN_KEY, username);
					mainRedis.opsForHash().delete(ACCESS_TOKEN_TO_USERNAME_KEY,
							oldToken);
					mainRedis.opsForHash().delete("token:" + oldToken, "priv",
							"_id", "nick_name", "status", "tag", "pic",
							"vlevel", "nc_id", "school_code");
				}
				map.put("code", "1");
				map.put("msg", "ok");
			} else {
				map.put("code", "30302");
				map.put("msg", "用户名或密码错误");
			}

			return map;
		}

	}

	/**
	 * 会计城密码修改
	 * 
	 * @date 2015年8月25日 上午10:54:23
	 * @param @param username 手机号码
	 * @param @param oldpassword 旧密码MD5
	 * @param @param newpassword 新密码非MD5
	 * @param @return
	 * @throws
	 */
	public boolean UpdateKjcityPsw(String username, String oldpassword,
			String newpassword) {

		try {
			SynchroMemberSoapProxy ss = new SynchroMemberSoapProxy();

			newpassword = AES.aesEncrypt(newpassword, AESKEY);
			oldpassword = AES.aesEncrypt(oldpassword, AESKEY);

			String MYMD5Result = MsgDigestUtil.MD5.MD5ForDotNet(
					WebSiteKey + username + oldpassword + newpassword + AESKEY)
					.toUpperCase();

			System.out.println("update password md5:" + WebSiteKey + username
					+ oldpassword + newpassword + AESKEY);

			boolean addsuccess = ss.updateUserPwd(WebSiteKey, username,
					oldpassword, newpassword, MYMD5Result);

			return addsuccess;
		} catch (Exception e) {
			return false;
		}

	}

	// 修改密码时调用下，psw是原始密码
	public void SetAcKey(int userId, String psw) throws Exception {
		DBCollection users = mainMongo.getCollection("users");
		BasicDBObject query_id = new BasicDBObject("_id", userId);
		DBObject user = users.findOne(query_id);
		psw = AES.aesEncrypt(psw, ACAESKEY);
		if (user != null) {
			BasicDBObject update = new BasicDBObject("$set", new BasicDBObject(
					"ackey", psw));

			users.findAndModify(query_id, update);
		}
	}

	public String GetAcKey(String AcKeyDes) {
		try {
			return AES.aesDecrypt(AcKeyDes, ACAESKEY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public void updateTeacher(String tuid, String nick_name, String insJson)
			throws Exception {
		List js = null;
		if (insJson != "") {
			js = JSONUtil.jsonToBean(insJson, List.class);

		}

		DBCollection users = mainMongo.getCollection("users");
		BasicDBObject query_id = new BasicDBObject("tuid", tuid);
		BasicDBObject update = new BasicDBObject("$set", new BasicDBObject(
				"priv", 2).append("nick_name", HtmlUtils.htmlEscape(nick_name))
				.append("user_industry", js));

		users.findAndModify(query_id, update);

		BasicDBObject up = new BasicDBObject("nickName", nick_name);

		qquserMongo.getCollection("qQUser").update(
				new BasicDBObject("tuid", tuid), new BasicDBObject("$set", up));

	}

	public void updateTeacher(String tuid, String nick_name, String nc_id,
			String sm_user_id, String school_code, String post_name)
			throws Exception {

		BasicDBObject updateapppendBasicDBObject = new BasicDBObject("nc_id",
				nc_id).append("school_code", school_code).append("priv", 2)
				.append("sm_user_id", sm_user_id)
				.append("post_name", post_name).append("nick_name", nick_name);

		BasicDBObject update = new BasicDBObject("$set",
				updateapppendBasicDBObject);
		DBCollection users = mainMongo.getCollection("users");
		BasicDBObject query_id = new BasicDBObject("tuid", tuid);

		DBCollection fail = mainMongo.getCollection("fail");
		if (users.findOne(new BasicDBObject("tuid", tuid)) == null) {
			fail.save(query_id);
		}

		users.findAndModify(query_id, update);

	}

	public void updateTeacherfornc(String tuid, String nick_name,
			String person_num, String province, String area,
			String school_name, String insJson) throws Exception {
		List js = null;
		if (insJson != "") {
			js = JSONUtil.jsonToBean(insJson, List.class);

		}

		DBCollection users = mainMongo.getCollection("users");
		BasicDBObject query_id = new BasicDBObject("tuid", tuid);
		BasicDBObject update = new BasicDBObject("$set", new BasicDBObject(
				"priv", 2).append("nick_name", HtmlUtils.htmlEscape(nick_name))
				.append("person_num", person_num).append("province", province)
				.append("area_name", area).append("school_name", school_name));

		users.findAndModify(query_id, update);

		BasicDBObject up = new BasicDBObject("nickName", nick_name);

		qquserMongo.getCollection("qQUser").update(
				new BasicDBObject("tuid", tuid), new BasicDBObject("$set", up));

	}

	public void closeTeacherfornc(String tphone) throws Exception {

	}

	public DBObject addTeacherfromnc(String tuid, String nick_name, String psw,
			String pic, String person_num, String province, String area,
			String school_name, String insJson) throws Exception {

		DBCollection users = mainMongo.getCollection("users");

		BasicDBObject query_tuid = new BasicDBObject("tuid", tuid);
		DBObject user = users.findOne(query_tuid);
		if (user != null) {
			return user;
		} else {

			user = new BasicDBObject(_id, userKGS.nextId());
			if (null == nick_name) {
				nick_name = "izhubo_" + user.get(_id);
			}

			// user.putAll(basicInfoWithNickName);
			user.put("nick_name", HtmlUtils.htmlEscape(nick_name)); // 注意QQ需要返回QQ昵称，执行初始化
			user.put("tuid", tuid);
			user.put("priv", 2);
			user.put("status", Boolean.TRUE);
			user.put("timestamp", System.currentTimeMillis());
			user.put("pic", pic);
			user.put("topic_count", 0);
			user.put("vlevel", 0);
			user.put("topic_evaluation_count", 0);
			user.put("ackey", AES.aesEncrypt(psw, ACAESKEY));

			user.put("person_num", person_num);
			user.put("province", province);
			user.put("area_name", area);
			user.put("school_name", school_name);

			if (insJson != "") {
				List js = JSONUtil.jsonToBean(insJson, List.class);
				user.put("user_industry", js);
			}

			try {
				// TODO wait Mod on _id not allowed
				// https://jira.mongodb.org/browse/SERVER-9958
				// log.debug(">>>>>>>>>>>>>>>buildShowUser qd: {}",
				// user.get("qd"));
				return users.findAndModify(
						query_tuid.append(_id, user.removeField(_id)), null,
						null, false, new BasicDBObject($setOnInsert, user),
						true, true); // upsert
			} catch (MongoException e) {

				query_tuid.remove(_id);
				return users.findOne(query_tuid);
			}
		}
	}

	public DBObject addTeacher(String tuid, String nick_name, String psw,
			String pic, String nc_id, String sm_user_id, String school_code,
			String postname) throws Exception {

		DBCollection users = mainMongo.getCollection("users");

		BasicDBObject query_tuid = new BasicDBObject("tuid", tuid);
		DBObject user = users.findOne(query_tuid);
		if (user != null) {
			return user;
		} else {

			user = new BasicDBObject(_id, userKGS.nextId());
			if (null == nick_name) {
				nick_name = "izhubo_" + user.get(_id);
			}

			// user.putAll(basicInfoWithNickName);
			user.put("nick_name", HtmlUtils.htmlEscape(nick_name)); // 注意QQ需要返回QQ昵称，执行初始化
			user.put("tuid", tuid);
			user.put("priv", 2);
			user.put("status", Boolean.TRUE);
			user.put("timestamp", System.currentTimeMillis());
			user.put("pic", pic);
			user.put("topic_count", 0);
			user.put("vlevel", 0);
			user.put("topic_evaluation_count", 0);
			user.put("nc_id", nc_id);
			user.put("sm_user_id", sm_user_id);
			user.put("post_name", postname);
			user.put("school_code", school_code);
			user.put("ackey", AES.aesEncrypt(psw, ACAESKEY));

			user.put("priv0", 1);// 学生权限
			user.put("priv1", 0);// 抢答权限
			user.put("priv2", 0);// 招生权限
			user.put("priv3", 0);
			user.put("priv4", 0);
			user.put("priv5", 0);
			user.put("priv6", 0);
			user.put("priv7", 0);
			user.put("priv8", 0);
			user.put("priv9", 0);

			try {
				// TODO wait Mod on _id not allowed
				// https://jira.mongodb.org/browse/SERVER-9958
				// log.debug(">>>>>>>>>>>>>>>buildShowUser qd: {}",
				// user.get("qd"));
				return users.findAndModify(
						query_tuid.append(_id, user.removeField(_id)), null,
						null, false, new BasicDBObject($setOnInsert, user),
						true, true); // upsert
			} catch (MongoException e) {

				query_tuid.remove(_id);
				return users.findOne(query_tuid);
			}
		}
	}

	public DBObject addTeacher(String tuid, String nick_name, String psw,
			String pic, String insJson) throws Exception {

		DBCollection users = mainMongo.getCollection("users");

		BasicDBObject query_tuid = new BasicDBObject("tuid", tuid);
		DBObject user = users.findOne(query_tuid);
		if (user != null) {
			return user;
		} else {

			user = new BasicDBObject(_id, userKGS.nextId());
			if (null == nick_name) {
				nick_name = "izhubo_" + user.get(_id);
			}

			// user.putAll(basicInfoWithNickName);
			user.put("nick_name", HtmlUtils.htmlEscape(nick_name)); // 注意QQ需要返回QQ昵称，执行初始化
			user.put("tuid", tuid);
			user.put("priv", 2);
			user.put("status", Boolean.TRUE);
			user.put("timestamp", System.currentTimeMillis());
			user.put("pic", pic);
			user.put("topic_count", 0);
			user.put("vlevel", 0);
			user.put("topic_evaluation_count", 0);
			user.put("ackey", AES.aesEncrypt(psw, ACAESKEY));

			if (insJson != "") {
				List js = JSONUtil.jsonToBean(insJson, List.class);
				user.put("user_industry", js);
			}

			try {
				// TODO wait Mod on _id not allowed
				// https://jira.mongodb.org/browse/SERVER-9958
				// log.debug(">>>>>>>>>>>>>>>buildShowUser qd: {}",
				// user.get("qd"));
				return users.findAndModify(
						query_tuid.append(_id, user.removeField(_id)), null,
						null, false, new BasicDBObject($setOnInsert, user),
						true, true); // upsert
			} catch (MongoException e) {

				query_tuid.remove(_id);
				return users.findOne(query_tuid);
			}
		}
	}

	public DBObject addUser(String tuid, String nick_name, String psw,
			String mobile, String area_name, String province, String city,
			String school_code, String school_name, Integer nc_user_state,
			String nc_id, String channel, String qd, String ssoUserId, String pic) throws Exception {

		DBCollection users = mainMongo.getCollection("users");

		BasicDBObject query_tuid = new BasicDBObject("tuid", tuid);
		DBObject user = users.findOne(query_tuid);
		if (user != null) {
			return user;
		} else {

			user = new BasicDBObject(_id, userKGS.nextId());
			if (null == nick_name) {
				nick_name = "kuaida_" + user.get(_id);
			}
			if(org.apache.commons.lang3.StringUtils.isBlank(pic)){
				pic = "http://answerimg.kjcity.com/default_student.png";
			}

			// user.putAll(basicInfoWithNickName);
			user.put("nick_name", HtmlUtils.htmlEscape(nick_name)); // 注意QQ需要返回QQ昵称，执行初始化
			user.put("tuid", tuid);
			user.put("sso_userid", ssoUserId);//增加保存sso的userid
			user.put("priv", 3);
			user.put("status", Boolean.TRUE);
			user.put("timestamp", System.currentTimeMillis());
			user.put("pic", pic);
			// user.put("pic", "http://answerimg.kjcity.com/logo.png");
			user.put("topic_count", 0);
			user.put("topic_evaluation_count", 0);
			user.put("ackey", AES.aesEncrypt(psw, ACAESKEY));
			user.put("vlevel", 0);
			user.put("is_show_update", 0);
			user.put("mobile", mobile);
			user.put("channel", channel);
			user.put("qd", qd);

			user.put("priv0", 1);// 学生权限
			user.put("priv1", 0);// 抢答权限
			user.put("priv2", 0);// 招生权限
			user.put("priv3", 0);
			user.put("priv4", 0);
			user.put("priv5", 0);
			user.put("priv6", 0);
			user.put("priv7", 0);
			user.put("priv8", 0);
			user.put("priv9", 0);

			// 省份也有可能是同步过来的，如果是同步过来的，则不需要
			if (province.equals("")) {
				// add by shihongjie 2015-10-19
				// 用户的省市
				Map<String, String> map = addressController
						.addressByMobile(mobile);
				user.put("province", map.get("province"));
				user.put("city", map.get("city"));
			} else {
				user.put("province", province);
				user.put("city", city);
			}
			// 同步NC的校区编码和校区名称还有学员状态，方便后期统计
			user.put("area_name", area_name);
			user.put("school_code", school_code);
			user.put("school_name", school_name);
			user.put("nc_user_state", nc_user_state);

			// 这里加一个NC_ID的判断，如果 原先日志中有，则直接获取nc_id并删除原先记录

			String new_ncid = GetNC_ID(mobile);
			if (new_ncid != "") {
				nc_id = new_ncid;
			}
			user.put("nc_id", nc_id);
			try {
				// TODO wait Mod on _id not allowed
				// https://jira.mongodb.org/browse/SERVER-9958
				// log.debug(">>>>>>>>>>>>>>>buildShowUser qd: {}",
				// user.get("qd"));
				return users.findAndModify(
						query_tuid.append(_id, user.removeField(_id)), null,
						null, false, new BasicDBObject($setOnInsert, user),
						true, true); // upsert
			} catch (MongoException e) {
				e.printStackTrace();
				query_tuid.remove(_id);
				return users.findOne(query_tuid);
			}
		}
	}

	public DBObject addTUser(String tuid, String nick_name, String psw,
			String mobile, String area_name, String province, String city,
			String school_code, String school_name, Integer nc_user_state,
			String nc_id, String channel, String qd, int priv, String ssoUserId, String pic) throws Exception {

		DBCollection users = mainMongo.getCollection("users");

		BasicDBObject query_tuid = new BasicDBObject("tuid", tuid);
		DBObject user = users.findOne(query_tuid);
		if (user != null) {
			return user;
		} else {

			user = new BasicDBObject(_id, userKGS.nextId());
			if (null == nick_name) {
				nick_name = "kuaida_" + user.get(_id);
			}

			// user.putAll(basicInfoWithNickName);
			user.put("nick_name", HtmlUtils.htmlEscape(nick_name)); // 注意QQ需要返回QQ昵称，执行初始化
			user.put("tuid", tuid);
			user.put("priv", 3);
			user.put("status", Boolean.TRUE);
			user.put("timestamp", System.currentTimeMillis());
			user.put("pic", "http://answerimg.kjcity.com/logo.png");
			user.put("topic_count", 0);
			user.put("topic_evaluation_count", 0);
			user.put("ackey", AES.aesEncrypt(psw, ACAESKEY));
			user.put("vlevel", 0);
			user.put("is_show_update", 0);
			user.put("mobile", mobile);
			user.put("channel", channel);
			user.put("qd", qd);

			user.put("priv0", 0);// 学生权限
			user.put("priv1", 0);// 抢答权限
			user.put("priv2", 0);// 招生权限
			user.put("priv3", 0);
			user.put("priv4", 0);
			user.put("priv5", 0);
			user.put("priv6", 0);
			user.put("priv7", 0);
			user.put("priv8", 0);
			user.put("priv9", 0);

			// 省份也有可能是同步过来的，如果是同步过来的，则不需要
			if (province.equals("")) {
				// add by shihongjie 2015-10-19
				// 用户的省市
				Map<String, String> map = addressController
						.addressByMobile(mobile);
				user.put("province", map.get("province"));
				user.put("city", map.get("city"));
			} else {
				user.put("province", province);
				user.put("city", city);
			}
			// 同步NC的校区编码和校区名称还有学员状态，方便后期统计
			user.put("area_name", area_name);
			user.put("school_code", school_code);
			user.put("school_name", school_name);
			user.put("nc_user_state", nc_user_state);

			// 这里加一个NC_ID的判断，如果 原先日志中有，则直接获取nc_id并删除原先记录

			String new_ncid = GetNC_ID(mobile);
			if (new_ncid != "") {
				nc_id = new_ncid;
			}
			user.put("nc_id", nc_id);

			try {
				// TODO wait Mod on _id not allowed
				// https://jira.mongodb.org/browse/SERVER-9958
				// log.debug(">>>>>>>>>>>>>>>buildShowUser qd: {}",
				// user.get("qd"));
				return users.findAndModify(
						query_tuid.append(_id, user.removeField(_id)), null,
						null, false, new BasicDBObject($setOnInsert, user),
						true, true); // upsert
			} catch (MongoException e) {

				query_tuid.remove(_id);
				return users.findOne(query_tuid);
			}
		}
	}

	public String GetNC_ID(String mobile) {
		DBCollection users_ncpk = mainMongo.getCollection("users_ncpk");

		DBObject userdata = users_ncpk
				.findOne(new BasicDBObject("_id", mobile));

		if (userdata == null) {
			return "";
		} else {
			// 删除 原先记录
			users_ncpk.remove(new BasicDBObject("_id", mobile));
			return userdata.get("nc_id").toString();
		}

	}

	/**
	 * 用BASE64加密
	 * 
	 * @param str
	 * @return
	 */
	public static String getBASE64(String str) {
		byte[] b = str.getBytes();
		String s = null;
		if (b != null) {
			s = new sun.misc.BASE64Encoder().encode(b);
		}
		return s;
	}

	/**
	 * 解密BASE64字窜
	 * 
	 * @param s
	 * @return
	 */
	public static String getFromBASE64(String s) {
		byte[] b = null;
		if (s != null) {
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				return new String(b);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new String(b);
	}
	
	/**
	 * 根据时间判断课程状态
	 * @param time
	 * @return 已结束/未开始
	 */
	private static String planMiddleStateString(Long time){
		Long endTime = time+7200*1000;
		Long now = System.currentTimeMillis();
		if(time > now){
			return "未开始";
		}else if(time<now && endTime>now){
			return "直播中";
		}
		else
		{
			return "已结束";
		}
	}
	private static String liveOpenTimeFormate(Long start,Long end){
		if(start > 0&&end>0){
			SimpleDateFormat Startformatter = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
			SimpleDateFormat Endformatter = new SimpleDateFormat ("HH:mm:ss");
			return Startformatter.format(new Date(start))+"~"+Endformatter.format(new Date(end));
		}
		return "-";
	}
	public static void main(String[] args) throws Exception {

		 String key = AES.aesDecrypt("mclj/IYQjqt+njp/8as2cg==", ACAESKEY);
		 System.out.println(key);
//
////		Double.valueOf("0");
////
////		// System.out.println( AccScoreGainType.每日签到登录.ordinal());
////		// // String jsonString = "{\"test\": 1.115}";
////		//
////		//
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date date = sdf.parse("2017-2-22 09:00:00");
		Date date2 = sdf.parse("2017-03-01 00:00:00");
		Date date3 = new Date(1489981562126L);
//		Date date4 = new Date(1481622353243L);
		
		//System.out.println(liveOpenTimeFormate(1491554163000l,1481622353243L));
//
		System.out.println(date2.getTime());
		//System.out.println(date3.getTime());
		
		
		
		
	
		
		//System.out.println(planMiddleStateString(1487667600000L));
	System.out.println(sdf.format(date3));
		System.out.println(new BigDecimal(14).divide(new BigDecimal(127),BigDecimal.ROUND_FLOOR));
		
		
	

		// MD5Util

		// System.out.println(MD5Util.getFileMD5String("C:\\Users\\zhaokun\\Desktop\\上线安装包\\Answer_Student_v2.1.1.apk"));
		//
		// System.out.println(MD5Util.getFileMD5String("C:\\Users\\zhaokun\\Desktop\\上线安装包\\Answer_Student_v2.1.2.apk"));

		// String base64
		// =getFromBASE64("R30qYJ5fx97CQKX/whzjk7KJ8w9DA9+v6FxGiW/jiNo=");
		// String orderno =AES.aesDecrypt(base64, ACAESKEY);
		// System.out.println( orderno);
		// Map<String, Object> map =JSONUtil.jsonToMap(jsonString);
		//
		// System.out.println("cal " + double.valueOf("12543.25554"));

		// String result =
		// MsgDigestUtil.MD5.MD5ForDotNet("nc13507719687Jey3mwywG9BnLm5IBdCwbw==大区2广西省南宁市JH209020401南宁西大校区10001A51000000004GQCY%^$AF>.12*******");
		// String resuls =
		// MsgDigestUtil.MD5.MD5ForDotNet("nc13507719687Jey3mwywG9BnLm5IBdCwbw==大区2广西省南宁市JH209020401南宁西大校区10001A51000000004GQCY%^$AF>.12*******");
		// String resul4 =
		// MsgDigestUtil.MD5.digest2HEX("nc13507719687Jey3mwywG9BnLm5IBdCwbw==大区2广西省南宁市JH209020401南宁西大校区10001A51000000004GQCY%^$AF>.12*******");
		// System.out.println(result);
		// System.out.println(resul4);
		// XingeApp xinge = new XingeApp(2200149294L,
		// "55b252ee247f1c9b2e642ac8cc9eec61");
		// System.out.println( xinge.queryTokensOfAccount("10037953"));

		// XingeApp xinge = new XingeApp(2200149294L,
		// "55b252ee247f1c9b2e642ac8cc9eec61");
		// xinge.deleteAllTokensOfAccount("10010943");

		// System.out.println( xinge.deleteAllTokensOfAccount("10010943"));

		// System.out.println(xinge.deleteAllTokensOfAccount("10037953") );;
		// System.out.println(xinge.deleteAllTokensOfAccount("10013089") );;
//
//		String md5str = "13824429749" + AESKEY;
//		String key = MsgDigestUtil.MD5.digest2HEX(md5str);
//		System.out.println("1 " + key);
//
//		String key2 = MsgDigestUtil.MD5.digest2HEX(AESKEY);
//		System.out.println("1 " + key2);
	}
	
	
	public static long startOfyesterday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date=calendar.getTime();
		return date.getTime();
	}

}

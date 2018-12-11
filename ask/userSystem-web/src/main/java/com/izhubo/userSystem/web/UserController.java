package com.izhubo.userSystem.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.izhubo.rest.common.util.school.SchoolUtils;
import com.izhubo.userSystem.mongo.qquser.QQUser;
import com.izhubo.userSystem.mongo.qquser.QQUserRepositery;
import com.izhubo.userSystem.utils.JSONUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@Controller
public class UserController extends MultiActionController {

	private static Logger logger = LoggerFactory
			.getLogger(UserController.class);

	@Autowired
	protected QQUserRepositery qqUserRepositery;

	@Resource
	protected StringRedisTemplate mainRedis;

	@Resource
	protected MongoTemplate mainMongo;

	public Map<String, Object> checkname(HttpServletRequest req,
			HttpServletResponse res) {

		// TODO checkname
		logger.info("checkname");
		Map<String, Object> map = new HashMap<String, Object>();
		String username = req.getParameter("user_name");
		String nickname = req.getParameter("nick_name");

		if (StringUtils.isNotBlank(username)
				&& this.qqUserRepositery.findByUsername(username).size() > 0) {
			map.put("code", "30306");
		} else if (StringUtils.isNotBlank(nickname)
				&& this.qqUserRepositery.findByNickName(nickname).size() > 0) {
			map.put("code", "30307");
		} else {
			Map<String, Object> map1 = new HashMap<>();
			map1.put("tuid", UUID.randomUUID().toString());
			map1.put("nick_name", "");
			map.put("data", map1);

			map.put("code", "1");
		}

		return map;
	}

	// 因数据问题，部分学员，有NCID，但是没有校区这种情况多为会计城同步过来，当出现这种情况的时候，尝试去报名表拿校区和校区名称
	private void TryToFindSchoolCode(String tuid) {
		// 先判断是否存在只有NCid同时校区为空的情况
		DBObject user = mainMongo.getCollection("users").findOne(
				new BasicDBObject("tuid", tuid));
		if ((user.get("school_code") == null || user.get("school_code") == "")
				&& user.get("nc_id") != null) {
			// 判断NCid和tuid不一样,这种情况为真实学员
			String nc_user_id = (String) user.get("nc_id");
			if (tuid != nc_user_id) {
				BasicDBObject findobj = new BasicDBObject("nc_user_id",
						nc_user_id).append("status", 1).append("dr", 0);
				DBObject sign = mainMongo.getCollection("signs").findOne(
						findobj,
						new BasicDBObject("school_name", 1).append(
								"school_code", 1));
				if (sign != null) {
					String school_code = sign.get("school_code").toString();
					String school_name = sign.get("school_name").toString();
					BasicDBObject updateapppendBasicDBObject = new BasicDBObject(
							"school_code", school_code).append("school_name",
							school_name);

					BasicDBObject update = new BasicDBObject("$set",
							updateapppendBasicDBObject);

					DBCollection users = mainMongo.getCollection("users");
					BasicDBObject query_id = new BasicDBObject("tuid", tuid);
					users.findAndModify(query_id, update);
				}
			}

		}

	}

	public Map<String, Object> info(HttpServletRequest req,
			HttpServletResponse res) {
		Map<String, Object> map = new HashMap<String, Object>();

		// TODO info
		// Map<String, Object> map1 = new HashMap<String, Object>();
		// map.put("data", map1);

		HuanxinController hx = new HuanxinController();
		// 获得access_token
		String access_token = req.getParameter("access_token");
		if (StringUtils.isBlank(access_token)) {
			logger.info("access_token is blank: {}", access_token);
			map.put("code", "30405");
			return map;
		}

		// 获得username (access_token如何保护？ 不能每次调用这个都返回正确的)
		String username = (String) mainRedis.opsForHash().get(
				LoginController.ACCESS_TOKEN_TO_USERNAME_KEY, access_token);
		if (StringUtils.isBlank(username)) {
			logger.info(
					"username not found in redis, clean token, is blank: username: {}, access_token: {}",
					username, access_token);

			map.put("code", "30405");
			// 清空token缓存
			mainRedis.delete("token:" + access_token);
			return map;
		}
		List<QQUser> list = qqUserRepositery.findByUsername(username);
		if (list.size() == 0) {
			logger.info(
					"username not found in mongo, clean token, username: {}",
					username);
			map.put("code", "30405");
			mainRedis.delete("token:" + access_token);
			return map;
		}
		QQUser qqUser = list.get(0);

		TryToFindSchoolCode(qqUser.getTuid());

		Map<String, Object> map1 = JSONUtil.beanToMap(qqUser);
		// 注意转换，确保有数据

		DBObject user = mainMongo.getCollection("users").findOne(
				new BasicDBObject("tuid", qqUser.getTuid()));

		// 校区编码
		String school_code = (String) (user.containsField("school_code") ? user
				.get("school_code") : "");
		// 判断是否为学员
		if (school_code != "") {
			map1.put("is_school_student", 1);
		} else {
			map1.put("is_school_student", 0);
		}

		String school_name = "校区";
		// 校区电话
		String[] school_telephone = SchoolUtils.phonearray(null);
		// 校区经纬度
		Double school_longitude = 0d;
		Double school_latitude = 0d;
		if (StringUtils.isBlank(school_code)) {
			// 校区编码
			mainMongo.getCollection("area").findOne(
					new BasicDBObject("code", school_code));
			DBObject constantsTab = mainMongo.getCollection("constants")
					.findOne(new BasicDBObject("dr", 0));
			if (null != constantsTab
					&& null != constantsTab.get("zong_bu_code")) {
				school_code = (String) constantsTab.get("zong_bu_code");
			}
		}

		if (StringUtils.isNotBlank(school_code)) {
			// 查询校区
			DBObject area = mainMongo.getCollection("area").findOne(
					new BasicDBObject("code", school_code));
			if (null != area) {
				school_telephone = SchoolUtils.phonearray((String) area
						.get("telephone"));
				school_name = (String) area.get("name");
				Map location = (Map) area.get("location");
				if (null != location) {
					school_longitude = Double.valueOf(location.get("longitude")
							.toString());
					school_latitude = Double.valueOf(location.get("latitude")
							.toString());
				}
			}

		}

		// 用户头像
		map1.put("pic", user.get("pic"));
		// 昵称
		map1.put("nick_name", user.get("nick_name"));
		// 用户id
		map1.put("_id", user.get("_id"));
		// 真是姓名-账号
		map1.put("real_name",
				user.containsField("real_name") ? user.get("real_name") : "");
		map1.put(
				"alipay_account",
				user.containsField("alipay_account") ? user
						.get("alipay_account") : "");

		// 校区编码
		map1.put("school_code", school_code);
		// 校区名称
		map1.put("school_name", school_name);
		map1.put("priv", user.get("priv"));
		map1.put("ac", hx.ConventPasswordToMD5(qqUser.getPassword()));

		// 校区电话-数组
		map1.put("school_telephone", school_telephone);
		// 校区经纬度
		map1.put("school_longitude", school_longitude);
		map1.put("school_latitude", school_latitude);
		// 加入了获取提问数
		long topics = mainMongo.getCollection("topics").count(
				new BasicDBObject("author_id", user.get("_id")));
		map1.put("topic_num", topics);
		//新增sso_userid
		String sso_userid = "";
		if(StringUtils.isNotBlank(user.get("sso_userid").toString())){
			sso_userid = user.get("sso_userid").toString();
		}
		map1.put("sso_userid", sso_userid);
		
		map.put("data", map1);
		map.put("code", 1);

		return map;
	}

}

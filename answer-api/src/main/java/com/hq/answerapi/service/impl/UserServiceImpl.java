package com.hq.answerapi.service.impl;

import com.hq.answerapi.config.props.LocalConfigProperties;
import com.hq.answerapi.dao.xyqquser.QQUserRepository;
import com.hq.answerapi.entity.QQUser;
import com.hq.answerapi.model.HK;
import com.hq.answerapi.model.KGS;
import com.hq.answerapi.model.Priv;
import com.hq.answerapi.model.Privs;
import com.hq.answerapi.pojo.UserInfoPOJO;
import com.hq.answerapi.service.UserService;
import com.hq.answerapi.util.JSONUtil;
import com.hq.answerapi.util.PassWordUtil;
import com.hq.answerapi.util.RandomUtil;
import com.hq.common.enumeration.TransactionStatus;
import com.hq.http.HttpConnManager;
import com.hq.http.HttpPlainResult;
import com.hq.http.HttpResultDetail;
import com.hq.http.HttpResultHandler;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hq.answerapi.model.MongoKey.$setOnInsert;
import static com.hq.answerapi.model.MongoKey._id;

/**
 * @author hq
 */
@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

	private static final String ACCESS_TOKEN_TO_USERNAME_KEY = "access_token_to_username";
	private static final String USERNAME_TO_ACCESS_TOKEN_KEY = "username_to_access_token";

	@Autowired
	private StringRedisTemplate mainRedis;
	
	@Autowired
	private QQUserRepository qqUserRepository;

	@Autowired
	private MongoTemplate xyMongoTemplate;

	@Autowired
	private KGS userKGS;

	@Autowired
	private HttpConnManager httpConnManager;

	@Autowired
	private LocalConfigProperties config;

	private DBCollection users(){
		return xyMongoTemplate.getCollection("users");
	}

	@Override
	public UserInfoPOJO getUserInfoByToken(String token, String schoolId) {
		HashMap<String, Object> map = new HashMap<>(1);
		if(token != null) {
			map.put("token", token);
		}
		try {
			HttpPlainResult result = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost()+"/inner/userInfo", map,schoolId);
			LOG.info(result.getResult());
			HttpPlainResult result2 = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost()+"/inner/userMobileNo", map,schoolId);
			LOG.info(result2.getResult());
			HttpResultDetail<UserInfoPOJO> entry = HttpResultHandler.handle(result,UserInfoPOJO.class);
			HttpResultDetail<UserInfoPOJO> entry2 = HttpResultHandler.handle(result2,UserInfoPOJO.class);
			if(entry.isOK() && entry2.isOK()){
				entry.getResult().setMobileNo(entry2.getResult().getMobileNo());
				return entry.getResult();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void synUserToMongo(UserInfoPOJO userInfo, String passWord) {
		if(null != userInfo) {
			userInfo.setMobileNo(userInfo.getMobileNo());
			passWord = PassWordUtil.encrypted(passWord);
			QQUser qqUser = new QQUser();
			qqUser.setNickName(userInfo.getNickName());
			qqUser.setPassword(passWord);
			qqUser.setQd("kuaida");
			qqUser.setTuid(RandomUtil.getTuid());
			qqUser.setUsername(userInfo.getMobileNo());
			this.qqUserRepository.save(qqUser);
			this.addUser(qqUser.getTuid(), userInfo);
		}
	}

	private DBObject addUser(String tuid, UserInfoPOJO userInfo) {
		BasicDBObject queryTuid = new BasicDBObject("tuid", tuid);
		DBObject user = users().findOne(queryTuid);
		if (null == user) {
			user = new BasicDBObject(_id, userKGS.nextId());
			user.put("tuid", tuid);
			user.put("sso_userid", userInfo.getUid()+"");
			user.put("nick_name", userInfo.getNickName());
			user.put("real_name_from_nc", userInfo.getNickName());
			user.put("is_hq",1);
			user.put("priv", Priv.Student.getValue());
			user.put("status", Boolean.TRUE);
			user.put("timestamp", System.currentTimeMillis());
			user.put("pic", userInfo.getAvatar());
			user.put("topic_count", 0);
			user.put("topic_evaluation_count", 0);
			user.put("vlevel", 0);
			user.put("is_show_update", 0);
			user.put("mobile", userInfo.getMobileNo());
			user.put("qd", "kuaida");
			user.put(Privs.学员.getKey(), 1);
			user.put(Privs.抢答.getKey(), 0);
			user.put(Privs.招生.getKey(), 0);
			user.put(Privs.特殊标签老师.getKey(), 0);
			try {
				return users().findAndModify(
						queryTuid.append(_id, user.removeField(_id)), null,
						null, false, new BasicDBObject($setOnInsert, user),
						true, true); // upsert
			} catch (MongoException e) {
				e.printStackTrace();
				queryTuid.remove(_id);
				return users().findOne(queryTuid);
			}
		} else{
			return user;
		}
	}

	@Override
	public Map<String, Object> mongoCheck(String token, String passWord, String schoolId) {
		Map<String,Object> map = new HashMap<>();
		map.put("code", TransactionStatus.ANSWER_NOT_PERMISSION_LOGIN_EMPLOYEES_APP.value());

		UserInfoPOJO userInfo = this.getUserInfoByToken(token,schoolId);
		if(null == userInfo) {
			return map;
		}
		String mobileNo = userInfo.getMobileNo();
		List<QQUser> list = this.qqUserRepository.findByUsername(mobileNo);
		if (null == list || list.size() == 0) {
			//对于SSO中存在，但Mongodb中不存在的用户，需要把SSO的用户信息同步到Mongodb,但是同步后priv=3,不能登陆教师端
			this.synUserToMongo(userInfo, passWord);
			return map;
		}
		QQUser qqUser = list.get(0);
		DBObject user = users().findOne(new BasicDBObject("tuid", qqUser.getTuid()));

		if (user != null) {
			String privTemp = user.get("priv").toString();
			if(Integer.valueOf(privTemp) != Priv.Teacher.getValue()) {
				return map;
			}
			//判断账号是否禁用
			if (user.get("status").toString().startsWith("true")) {
				map.put("code", TransactionStatus.OK.value());
				Map<String, Object> userMap = JSONUtil.beanToMap(qqUser);

				userMap.put("access_token", token);
				userMap.put("user_id", user.get("_id").toString());

				if (user.get("is_show_update") == null) {
					userMap.put("is_show_update", 1);
				} else {
					userMap.put("is_show_update", user.get("is_show_update"));
				}
				// 用户头像
				userMap.put("pic", user.get("pic"));
				map.put("data", userMap);

				LOG.info("login success username: {}, access_token: {}", mobileNo, token);

				// 如果之前有access_token，并且不相等，找到并删除掉，
				if (mainRedis.opsForHash().hasKey(USERNAME_TO_ACCESS_TOKEN_KEY,mobileNo)) {
					String oldToken = (String) mainRedis.opsForHash().get(USERNAME_TO_ACCESS_TOKEN_KEY, mobileNo);
					if (!token.equals(oldToken)) {
						mainRedis.opsForHash().delete(ACCESS_TOKEN_TO_USERNAME_KEY, oldToken);
						mainRedis.opsForHash().delete("token:" + oldToken,
								"priv", "_id", "nick_name", "status", "tag",
								"pic", "vlevel", "nc_id", "school_code",
								"privs", HK.LOGIN.PRIV0, HK.LOGIN.PRIV1,
								HK.LOGIN.PRIV2);
					}
				}
				// 写入username_to_access_token
				mainRedis.opsForHash().put(USERNAME_TO_ACCESS_TOKEN_KEY, mobileNo, token);
				// 写入access_token_to_username
				mainRedis.opsForHash().put(ACCESS_TOKEN_TO_USERNAME_KEY, token, mobileNo);
				String hash = HK.LOGIN.hash(token);
				mainRedis.opsForHash().put(hash, HK.LOGIN.PRIV, user.get("priv").toString());
				mainRedis.opsForHash().put(hash, HK.LOGIN.ID, user.get("_id").toString());
				mainRedis.opsForHash().put(hash, HK.LOGIN.NICK_NAME, user.get("nick_name"));
				mainRedis.opsForHash().put(hash, HK.LOGIN.STATUS, "1");
				mainRedis.opsForHash().put(hash, HK.LOGIN.TAG, "");
				mainRedis.opsForHash().put(hash, HK.LOGIN.PIC, user.get("pic"));
				if(null != user.get("real_name_from_nc")) {
					mainRedis.opsForHash().put(hash, HK.LOGIN.REAL_NAME_FROM_NC, user.get("real_name_from_nc"));
				}
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
			} else {
				map.put("code", TransactionStatus.ANSWER_ACCOUNT_HAS_BEEN_BANNED_DUE_TO_VIOLATION.value());
			}
		}
		return map;
	}

	@Override
	public boolean isTeacher(String mobileNo) {
		List<QQUser> list = this.qqUserRepository.findByUsername(mobileNo);
		if (null == list || list.size() == 0) {
			return false;
		}
		QQUser qqUser = list.get(0);
		DBObject user = users().findOne(new BasicDBObject("tuid", qqUser.getTuid()),new BasicDBObject("priv", 1));
		if (user != null) {
			String privTemp = user.get("priv").toString();
			if (Integer.valueOf(privTemp) == Priv.Teacher.getValue()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getMongoUidByToken(String token) {
		String hash = HK.LOGIN.hash(token);
		Object obj = mainRedis.opsForHash().get(hash, HK.LOGIN.ID);
		int mongoUid = Integer.parseInt(obj == null ? "0" : obj+"");
		return mongoUid;
	}

	@Override
	public String getRealNameByToken(String token) {
		String hash = HK.LOGIN.hash(token);
		Object obj = mainRedis.opsForHash().get(hash, HK.LOGIN.REAL_NAME_FROM_NC);
		String realName = obj == null ? null : obj+"";
		return realName;
	}
}

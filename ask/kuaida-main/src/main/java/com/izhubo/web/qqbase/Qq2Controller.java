package com.izhubo.web.qqbase;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.naming.spi.DirStateFactory.Result;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;

import com.izhubo.userSystem.mongo.qquser.QQUser;
import com.izhubo.userSystem.mongo.qquser.QQUserRepositery;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.qq.open.OpenApiV3;
import com.qq.open.OpensnsException;
import com.izhubo.rest.AppProperties;
import com.izhubo.rest.common.util.JSONUtil;
import com.izhubo.rest.common.util.MsgDigestUtil;
import com.izhubo.rest.common.util.http.HttpClientUtil;
import com.izhubo.common.doc.Param;
import com.izhubo.common.util.KeyUtils;
import com.izhubo.web.BaseController;
import com.izhubo.web.api.Web;

@Controller
public class Qq2Controller extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(Qq2Controller.class);

	@Autowired
	protected QQUserRepositery qqUserRepositery;

	private static final String TTUS_URL = AppProperties.get("us.domain");

	public static final String namespace = "q2-";
	public static final String RAN_TOKEN_SEED = "#@#qq"
			+ DateFormatUtils.format(System.currentTimeMillis(), "yMMdd")
			+ "%xi>YY";

	public Object login(HttpServletRequest req, HttpServletResponse res) {
		logger.info("Qq2 Login, url: {}", req.getRequestURI());

		try {

			Map<String, Object> map = this.checkNewQQUser(req, res);
			String access_token = (String)map.get("access_token");

			String app_contract_id = req.getParameter("app_contract_id");// 任务id，标识用户通过那个任务进入应用；
			logger.info("Qq2 Login, app_contract_id: {}", app_contract_id);
			if (StringUtils.isNotBlank(app_contract_id)) {
				// 增加用户任务集市信息
				addUserTaskMarket(req, res);
			}

			String url = "";
			// 如果有参数app_umid
			String umid = req.getParameter("app_umid");
			if (StringUtils.isNotBlank(umid)) {
				url = SHOW_URL + new String(Base64.getDecoder().decode(umid));
				logger.info("app_umid: {}, decode url: {}", umid, url);
				if (url.indexOf("?") > -1) {
					url += "&";
				} else {
					url += "?";
				}
			} else {
				url = SHOW_URL + "?";
			}
			// 添加其他字符串
			url += "r=" + System.currentTimeMillis() + "&qd=qqapp";
			url += StringUtils.isNotEmpty(access_token) ? "&access_token="
					+ access_token : "";
			logger.info("url: {}", url);
			//打印登录信息
			String openId = (String) map.get("openId");
			String openKey = (String) map.get("openKey");
			String username = (String) map.get("username");
			String tuid = (String) map.get("tuid");
			String ip = Web.getClientIp(req);
			logger.info("login_log access_token: {}, openid: {}, openkey: {}, username: {}, tuid: {}, ip: {}", 
					access_token, openId, openKey, username, tuid, ip);
			res.sendRedirect(url);
		} catch (Exception e) {
			logger.error("", e);
			try {
				String url = SHOW_URL + "/error.html";
				res.sendRedirect(url);
			} catch (Exception e2) {
				logger.error("", e2);
			}

		}
		return null;
		// 通过前端页面转跳，便于写入gourl到cookie中
		// ModelAndView modelAndView = new ModelAndView("qq2Login");
		// return modelAndView;

	}

	private Map<String, Object> checkNewQQUser(HttpServletRequest req,
			HttpServletResponse res) throws Exception {

		Map<String, Object> result = new HashMap<>();
		
		String openId = req.getParameter("openid");
		String openKey = req.getParameter("openkey");
		String pf = req.getParameter("pf");
		String pfkey = req.getParameter("pfkey");
		String access_token = "";
		String password = "";

		logger.info("openId: {}, openKey: {}, pf: {}, pfkey: {}", openId,
				openKey, pf, pfkey);
		if (openId == null || openKey == null || pf == null) {
			result.put("access_token", access_token);
			return result;
		}

		String nickName = ""; // openAPI查询
		QQUserInSession qqUserInSession = null;
		QQUser qqUser = null;
		List<QQUser> list = null;
		if ((qqUserInSession = QQUserInSession.getUserInSession(
				req.getSession(), openId, openKey)) == null) {

			logger.info("qqUserInSession 不存在");

			String username = "qq2-" + openId;
			// 检查用户是否存在，注意和 新的用户系统是同一个数据库，直接添加
			if ((list = this.qqUserRepositery.findByUsername(username)).size() == 0) {

				logger.info("用户 username:{} 不存在, 准备根据openid增加", username);

				password = "qq2-pw-" + openId;
				String encode_password = MsgDigestUtil.MD5.digest2HEX(password);
				String qd = "qqapp";
				String tuid = UUID.randomUUID().toString();
				logger.info("Qq2Controller------tuid:{}", tuid);
				// 用户不存在
				qqUser = new QQUser();
				qqUser.setOpenId(openId);
				qqUser.setPassword(encode_password);
				qqUser.setUsername(username);
				qqUser.setQd(qd);
				qqUser.setTuid(tuid);

			} else {
				logger.info("用户 username:{} 存在", username);

				qqUser = list.get(0);

			}

			// 添加新用户
			qqUserInSession = new QQUserInSession();
			qqUserInSession.setOpenId(openId);
			qqUserInSession.setOpenKey(openKey);
			qqUserInSession.setPf(pf);
			qqUserInSession.putUserInSession(req.getSession());
			qqUserInSession.setQqUser(qqUser);

		} else {
			logger.info("qqUserInSession 已经存在");
			qqUser = qqUserInSession.getQqUser();
		}

		// TODO 更新
		// 不论是否是新用户，都需要调用 OpenSDK获得用户信息
		logger.info("appid: {}, appkey: {}, appServerDomain: {}", appid,
				appkey, appServerDomain);
		OpenApiV3 apiV3 = new OpenApiV3(appid, appkey);
		apiV3.setServerName(appServerDomain);
		Map<String, Object> map = this.v3_user_getinfo(apiV3, openId, openKey,
				pf);
		// 检查异常的情况
		Integer ret = (Integer) map.get("ret");
		if (ret != 0) {
			throw new Exception((String) map.get("msg"));
		}
		// 任何异常都返回到异常页面
		nickName = (String) map.get("nickname");
		qqUser.setNickName(nickName);

		String figureurl = (String) map.get("figureurl");
		figureurl.replaceAll("\\/", "/");
		figureurl += "?v=" + System.currentTimeMillis();
		qqUser.setQqpicUrl(figureurl);

		// 保存
		this.qqUserRepositery.save(qqUser);

		// 更新主库的昵称和用户头像
		// 如果同tuid的 用户已经找到，并且不是默认
		logger.info("qqUser.getTuid():{}", qqUser.getTuid());
		DBObject q = new BasicDBObject("tuid", qqUser.getTuid());
		DBObject f = new BasicDBObject("nick_name", "1");
		f.put("pic", "1");
		DBObject r = users().findOne(q, f);
		if (r != null) {
			// 准备更新
			boolean needUpdate = false;
			DBObject s = new BasicDBObject();

			String rnick = (String) r.get("nick_name");
			if (rnick != null && (rnick.indexOf("ttpod_") > -1 || rnick.indexOf("izhubo_") > -1)) {
				needUpdate = true;
				s.put("nick_name", nickName);
				logger.info("needUpdate tuid: {}, rnick: {}, nickName: {}",
						qqUser.getTuid(), rnick, nickName);
			}

			String pic = (String) r.get("pic");
			if (StringUtils.isBlank(pic)) {
				needUpdate = true;
				s.put("pic", figureurl);

				logger.info("needUpdate tuid: {}, pic: {}, figureurl: {}",
						qqUser.getTuid(), pic, figureurl);
			}

			if (needUpdate) {
				DBObject set = new BasicDBObject("$set", s);
				WriteResult writeResult = users().update(q, set);
				logger.info("writeResult:{}", writeResult);
			}
		} else {
			logger.info("nor found users tuid: {}", qqUser.getTuid());
		}

		// 调用登录
		String url = String.format("%s/login?user_name=%s&password=%s",
				TTUS_URL, qqUser.getUsername(), "qq2-pw-" + openId);
		logger.info("login url:{}", url);
		String json = HttpClientUtil.get(url, null, HttpClientUtil.UTF8);
		logger.info(" json: {}", json);
		map = JSONUtil.jsonToMap(json);
		Map<String, Object> map1 = (Map<String, Object>) map.get("data");

		// 写入access_token
		// access_token = namespace
		// + MD5.digest2HEX(RAN_TOKEN_SEED + openKey
		// + Calendar.getInstance().get(Calendar.DAY_OF_YEAR));

		access_token = (String) map1.get("access_token");
		res.addCookie(new Cookie(Param.access_token, access_token));
		res.addCookie(new Cookie(Param.cookie_key_openId, openId));
		res.addCookie(new Cookie(Param.cookie_key_openKey, openKey));

		String openIdAndOpenKey = openId + ",,,," + openKey + ",,,," + pf
				+ ",,,," + pfkey;
		this.mainRedis.opsForHash().put(
				Param.redis_username_to_openIdAndOpenKey, qqUser.getUsername(),
				openIdAndOpenKey);
		logger.info("qqUser.getUsername():{}", qqUser.getUsername());
		logger.info("openIdAndOpenKey:{}", openIdAndOpenKey);

		this.mainRedis.opsForHash().put("openid_to_accesstoken", openId,
				access_token);
		logger.info("put openid_to_accesstoken openId: {}, access_token: {}",
				openId, access_token);
		// 迫使原来的缓存过期，让主系统同步
		// String key = KeyUtils.accessToken(access_token);
		// mainRedis.delete(key);

		result.put("access_token", access_token);
		result.put("openId", openId);
		result.put("openKey", openKey);
		result.put("username", qqUser.getUsername());
		result.put("tuid", qqUser.getTuid());
		return result;
	}

	private Map<String, Object> v3_user_getinfo(OpenApiV3 sdk, String openid,
			String openkey, String pf) {
		Map<String, Object> map = null;
		// 指定OpenApi Cgi名字
		String scriptName = "/v3/user/get_info";

		// 指定HTTP请求协议类型
		String protocol = "http";

		// 填充URL请求参数
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("openid", openid);
		params.put("openkey", openkey);
		params.put("pf", pf);

		try {
			String resp = sdk.api(scriptName, params, protocol);
			logger.info("v3_user_getinfo resp: " + resp);
			map = JSONUtil.jsonToMap(resp);
		} catch (OpensnsException e) {
			logger.error(
					String.format("Request Failed. code:%d, msg:%s\n",
							e.getErrorCode(), e.getMessage()), e);
		}
		return map;
	}

	public void setQqUserRepositery(QQUserRepositery qqUserRepositery) {
		this.qqUserRepositery = qqUserRepositery;
	}

	public void addUserTaskMarket(HttpServletRequest req,
			HttpServletResponse res) {
		logger.info("into addUserTaskMarket()");
		String app_contract_id = req.getParameter("app_contract_id");// 任务id，标识用户通过那个任务进入应用；
		String openId = req.getParameter("openid");
		Map<Object, Object> map = new HashMap<>();

		// 通过openid查询userid
		try {
			BasicDBObject query_tuid = new BasicDBObject("openId", openId);
			DBObject qquser = qquserMongo.getCollection("qQUser").findOne(
					query_tuid);
			String tuid = (String) qquser.get("tuid");
			BasicDBObject query_id = new BasicDBObject("tuid", tuid);
			DBObject user = mainMongo.getCollection("users").findOne(query_id);
			long userid = Long.parseLong(user.get("_id").toString());

			DBObject temp = new BasicDBObject("_id", userid);
			DBObject obj = adminMongo.getCollection("taskmarket").findOne(temp);

			logger.info("userid:{},obj:{}", userid, obj);
			if (obj == null) {
				map.put("_id", userid);
				map.put("app_contract_id", app_contract_id);
				map.put("task_state", 1);

				if (adminMongo.getCollection("taskmarket")
						.save(new BasicDBObject(map)).getN() == 1) {
					logger.info("user_id：{},app_contract_id:{}", userid,
							app_contract_id);
				} else {
					logger.info("taskmarket save fail");
				}
			} else {
				logger.info("user_id Already exists：{}", userid);
			}
		} catch (Exception e) {
			logger.error("e", e);
		}

	}

}

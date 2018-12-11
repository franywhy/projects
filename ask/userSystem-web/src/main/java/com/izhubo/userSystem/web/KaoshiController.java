package com.izhubo.userSystem.web;

import com.izhubo.rest.AppProperties;
import com.izhubo.rest.common.util.JSONUtil;
import com.izhubo.rest.common.util.MsgDigestUtil;
import com.izhubo.rest.common.util.http.HttpClientUtil;
import com.izhubo.rest.common.util.http.HttpClientUtil4_3;
import com.izhubo.userSystem.mongo.qquser.QQUser;
import com.izhubo.userSystem.mongo.qquser.QQUserRepositery;
import com.izhubo.userSystem.utils.AES;
import com.izhubo.userSystem.utils.Constant;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class KaoshiController {

	@Resource
	protected StringRedisTemplate mainRedis;
	@Resource
	protected MongoTemplate mainMongo;
	@Autowired
	protected QQUserRepositery qqUserRepositery;

	private String tiku_domain;

	/** 错题重做 */
	private String tiku_title_reform;
	/** 做题记录 */
	private String tiku_title_record;
	/** 评估报告 */
	private String tiku_title_report;
	/** 我的收藏 */
	private String tiku_title_mycollection;

	/** 会计从业 */
	private String tiku_accountants_tourl;
	/** 初级职称 */
	private String tiku_primary_title_tourl;
	/** 中级职称 */
	private String tiku_middle_title_tourl;

	/** 注册会计师 */
	private String tiku_certified_public_accountant_tourl;
	/** 初级会计实操 */
	private String tiku_primary_accounting_field_tourl;
	/** 中级会计财务 */
	private String tiku_middle_accounting_field_tourl;

	/** 高级会计实操 */
	private String tiku_senior_accountant_field_tourl;
	/** 本科学历教育 */
	private String tiku_bachelor_degree_tourl;
	/** 专科学历教育 */
	private String tiku_college_degree_tourl;

	/** 会计证 */
	private String tiku_accountant_card_tourl;
	/** 会计上岗 */
	private String tiku_accountant_up_tourl;

	/** 我的购买 */
	private String tiku_myshop_tourl;
	/** 继续上次练习 */
	private String tiku_lastexam_tourl;

	/** 会计实操校区专用 */
	private String tiku_shicao_tourl;

	private static final String SSO_TOKENEXPIRED_GET = AppProperties.get("sso_tokenExpired_get");

	@Value("#{application['tk.url.trainingcourse']}")
	private String TK_URL = "http://w.kjcity.com/ajax/TrainingCourse.ashx?openid=";

	@Value("#{application['us.domain']}")
	private String usdomain = "http://passport.kjcity.com/";

	@Value("#{application['kjcityh5.domain']}")
	private String kjcityh5domain = "http://m.kjcity.com/";

	@Value("#{application['tiku.domain.new']}")
	private String tikuDomainNew = "https://mtiku.hqjy.com/page/home.html";

	@Value("#{application['tiku.domain']}")
	private void setTikuDomain(String domain) {
		tiku_domain = domain;

		tiku_title_reform = tiku_domain + "historylist.aspx?type=errorhistory";
		tiku_title_record = tiku_domain + "classlist.aspx";
		tiku_title_report = tiku_domain + "evaluate.aspx";
		tiku_title_mycollection = tiku_domain + "historylist.aspx?type=fav";

		// 会计从业
		tiku_accountants_tourl = tiku_domain
				+ "catalog.aspx?gid=AA07FFA6-13F2-42BB-B4A0-7B905F88E3B3";
		// 初级职称
		tiku_primary_title_tourl = tiku_domain
				+ "catalog.aspx?gid=E56AA8E5-B18B-4515-9342-BFC2655A7EEA";
		// 中级职称
		tiku_middle_title_tourl = tiku_domain
				+ "catalog.aspx?gid=9104334D-111F-44FB-B3B0-1EE71AF66201";

		// 注册会计师
		tiku_certified_public_accountant_tourl = tiku_domain
				+ "catalog.aspx?gid=152706B1-812C-48B8-B332-48E9BA5B1009";
		// 初级会计实操
		tiku_primary_accounting_field_tourl = tiku_domain
				+ "catalog.aspx?gid=5CAF5FA7-D32C-4AD1-9C21-A77B50CB1446";
		// 中级会计财务
		tiku_middle_accounting_field_tourl = tiku_domain
				+ "catalog.aspx?gid=2E04A72B-1DC5-477E-82F6-65C5F7691299";

		// 高级会计实操
		tiku_senior_accountant_field_tourl = tiku_domain
				+ "catalog.aspx?gid=34E6A358-427C-48DA-9CC6-FAC71F14BD91";
		// 本科学历教育
		tiku_bachelor_degree_tourl = tiku_domain
				+ "catalog.aspx?gid=F07EA5FD-3D5E-4002-8B70-A6DE3A260CB9";
		// 专科学历教育
		tiku_college_degree_tourl = tiku_domain
				+ "catalog.aspx?gid=164F23A9-AD44-42A9-BBCC-1E32D996121F";

		// 会计证
		tiku_accountant_card_tourl = tiku_domain
				+ "catalog.aspx?gid=578A428A-805E-47C9-A7BE-67BF5560047B";
		// 会计上岗
		tiku_accountant_up_tourl = tiku_domain
				+ "catalog.aspx?gid=7787F7BD-82D6-42DC-808F-8855E8C90919";

		// 我的购买
		tiku_myshop_tourl = tiku_domain + "order.aspx";
		// 继续上次练习
		tiku_lastexam_tourl = tiku_domain + "lastexam.aspx";

		// 会计实操（校区专用）
		tiku_shicao_tourl = tiku_domain
				+ "catalog.aspx?gid=620CA552-D342-4212-8F1B-F4759E9179AF";
	}

	/*
	 * 杨瑞年 2015/11/30 9:47:08 string openid =
	 * SecurityHelper.SymEncrypt("13824429749"); string openkey =
	 * SecurityHelper.SymEncrypt("123456"); string token =
	 * SecurityHelper.MD5Encrypt(openid + openkey + "ap02061");
	 * 
	 * 杨瑞年 2015/11/30 9:47:53 string openid =
	 * SecurityHelper.SymEncrypt("13824429749");对称加密AES之前调试过的
	 * http://exam.kjcity.
	 * com/SignLogin.aspx?openid=hX2pX21cLZaqBnH8Spxi4Q==&openkey
	 * =oLNuVUoBPJ/LeQUNyFp4YQ==&token=8A1669AF3C6EAE2D536B1BDEC278A85C
	 */

	@RequestMapping(value = "/loginKaoShi/{access_token}", method = RequestMethod.POST)
	public Map<Object, Object> loginKaoShi(@PathVariable String access_token,
			HttpServletRequest req, HttpServletResponse res) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("code", "30701");
		map.put("msg", "access_token失效");
		try {
			Map<Object, Object> userRedis = mainRedis.opsForHash().entries(
					"token:" + access_token);

			if (null != userRedis && null != userRedis.get("_id")) {
				Integer user_id = Integer.valueOf(userRedis.get("_id")
						.toString());
				DBObject user = mainMongo.getCollection("users").findOne(
						new BasicDBObject("_id", user_id));
				List<QQUser> qqlist;
				QQUser qquser;
				if (null != user) {
					if (null != user.get("ackey")) {
						qqlist = qqUserRepositery.findByTuid(user.get("tuid")
								.toString());
						if (qqlist != null && qqlist.size() > 0) {
							qquser = qqlist.get(0);
							// username
							String username = qquser.getUsername();
							// ackey
							String ackey = (String) user.get("ackey");

							String openid = AES.aesEncrypt(username,
									Constant.TiKu.AESKEY);
							// 按照会答密钥解密 再通过会计城密钥加密
							String openkey = AES.aesEncrypt(AES.aesDecrypt(
									ackey, Constant.TiKu.ACAESKEY),
									Constant.TiKu.AESKEY);
							//
							String token = MsgDigestUtil.MD5
									.MD5ForDotNet(openid + openkey
											+ Constant.TiKu.SECRET_KEY);

							map.put("code", "1");
							map.put("msg", "请求成功!");
							map.put("data", tiku_domain
									+ "SignLogin.aspx?openid=" + openid
									+ "&openkey=" + openkey + "&token=" + token
									+ "&tourl=http://www.baidu.com");
						}
					} else {
						map.put("code", "30702");
						map.put("msg", "账号未同步到题库!");
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	// 错题重做
	@RequestMapping(value = "/ks/redoErr/{access_token}", method = RequestMethod.POST)
	private Map<Object, Object> redoErr(@PathVariable String access_token,
			HttpServletRequest req, HttpServletResponse res) {
		return kaoshiUrl(0, access_token, req, res);
	}

	// 做题记录
	@RequestMapping(value = "/ks/topicNote/{access_token}", method = RequestMethod.POST)
	private Map<Object, Object> topicNote(@PathVariable String access_token,
			HttpServletRequest req, HttpServletResponse res) {
		return kaoshiUrl(1, access_token, req, res);
	}

	// 评估报告
	@RequestMapping(value = "/ks/assessReport/{access_token}", method = RequestMethod.POST)
	private Map<Object, Object> assessReport(@PathVariable String access_token,
			HttpServletRequest req, HttpServletResponse res) {
		return kaoshiUrl(2, access_token, req, res);
	}

	// 我的收藏
	@RequestMapping(value = "/ks/myCollection/{access_token}", method = RequestMethod.POST)
	private Map<Object, Object> myCollection(@PathVariable String access_token,
			HttpServletRequest req, HttpServletResponse res) {
		return kaoshiUrl(3, access_token, req, res);
	}

	/**
	 * 
	 * @param type
	 *            0.错题重做 1.做题记录 2.评估报告 3.我的收藏
	 */
	private Map<Object, Object> kaoshiUrl(int type, String access_token,
			HttpServletRequest req, HttpServletResponse res) {
		// access_token = "q2-d0be04915af8801857b9845f8a6d7175";
		// access_token = "q2-d0be04915af8801857b9845f8a6d7175";

		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("code", "30701");
		map.put("msg", "access_token失效");
		try {
			Map<Object, Object> userRedis = mainRedis.opsForHash().entries(
					"token:" + access_token);

			if (null != userRedis && null != userRedis.get("_id")) {
				Integer user_id = Integer.valueOf(userRedis.get("_id")
						.toString());
				DBObject user = mainMongo.getCollection("users").findOne(
						new BasicDBObject("_id", user_id));
				List<QQUser> qqlist;
				QQUser qquser;
				if (null != user) {
					if (null != user.get("ackey")) {
						qqlist = qqUserRepositery.findByTuid(user.get("tuid")
								.toString());
						if (qqlist != null && qqlist.size() > 0) {
							qquser = qqlist.get(0);
							// username
							String username = qquser.getUsername();
							// ackey
							String ackey = (String) user.get("ackey");

							String openid = AES.aesEncrypt(username,
									Constant.TiKu.AESKEY);
							// 按照会答密钥解密 再通过会计城密钥加密
							String openkey = AES.aesEncrypt(AES.aesDecrypt(
									ackey, Constant.TiKu.ACAESKEY),
									Constant.TiKu.AESKEY);
							//
							String token = MsgDigestUtil.MD5.getMD5(openid
									+ openkey + Constant.TiKu.SECRET_KEY);
							// 跳转的URL
							String tourl = getTKtourl(type);

							map.put("code", "1");
							map.put("msg", "请求成功!");
							map.put("data", tiku_domain
									+ "SignLogin.aspx?openid=" + openid
									+ "&openkey=" + openkey + "&token=" + token
									+ "&tourl=" + tourl);
						}
					} else {
						map.put("code", "30702");
						map.put("msg", "账号未同步到题库!");
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * @param type
	 *            0.错题重做 1.做题记录 2.评估报告 3.我的收藏
	 * @return
	 */
	private String getTKtourl(int type) {
		// 跳转的URL
		String tourl = "";

		switch (type) {
		case 0:
			tourl = tiku_title_reform;
			break;
		case 1:
			tourl = tiku_title_record;
			break;
		case 2:
			tourl = tiku_title_report;
			break;
		case 3:
			tourl = tiku_title_mycollection;
			break;
		}
		return tourl;
	}

	@RequestMapping(value = "/ks/kaoshiUrlType/{access_token}/{type}", method = RequestMethod.POST)
	private Map<Object, Object> kaoshiUrlByType(
			@PathVariable String access_token, @PathVariable int type,
			HttpServletRequest req, HttpServletResponse res) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("code", "30701");
		map.put("msg", "access_token失效");
		try {
			Map<Object, Object> userRedis = mainRedis.opsForHash().entries(
					"token:" + access_token);

			if (null != userRedis && null != userRedis.get("_id")) {
				Integer user_id = Integer.valueOf(userRedis.get("_id")
						.toString());
				DBObject user = mainMongo.getCollection("users").findOne(
						new BasicDBObject("_id", user_id));
				List<QQUser> qqlist;
				QQUser qquser;
				if (null != user) {
					if (null != user.get("ackey")) {
						qqlist = qqUserRepositery.findByTuid(user.get("tuid")
								.toString());
						if (qqlist != null && qqlist.size() > 0) {
							qquser = qqlist.get(0);
							// username
							String username = qquser.getUsername();
							// ackey
							String ackey = (String) user.get("ackey");

							String openid = AES.aesEncrypt(username,
									Constant.TiKu.AESKEY);
							// 按照会答密钥解密 再通过会计城密钥加密
							String openkey = AES.aesEncrypt(AES.aesDecrypt(
									ackey, Constant.TiKu.ACAESKEY),
									Constant.TiKu.AESKEY);
							//
							String token = MsgDigestUtil.MD5.getMD5(openid
									+ openkey + Constant.TiKu.SECRET_KEY);
							// 跳转的URL
							String tourl = kaoshiUrlByType(type);

							map.put("code", "1");
							map.put("msg", "请求成功!");
							map.put("data", tiku_domain
									+ "SignLogin.aspx?openid=" + openid
									+ "&openkey=" + openkey + "&token=" + token
									+ "&tourl=" + tourl);
						}
					} else {
						map.put("code", "30702");
						map.put("msg", "账号未同步到题库!");
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;

	}

	private String kaoshiUrlByType(int type) {
		// 跳转的URL
		String tourl = "";

		switch (type) {
		case 0:
			tourl = tiku_accountants_tourl;
			break;
		case 1:
			tourl = tiku_primary_title_tourl;
			break;
		case 2:
			tourl = tiku_middle_title_tourl;
			break;

		case 3:
			tourl = tiku_certified_public_accountant_tourl;
			break;
		case 4:
			tourl = tiku_primary_accounting_field_tourl;
			break;
		case 5:
			tourl = tiku_middle_accounting_field_tourl;
			break;

		case 6:
			tourl = tiku_senior_accountant_field_tourl;
			break;
		case 7:
			tourl = tiku_bachelor_degree_tourl;
			break;
		case 8:
			tourl = tiku_college_degree_tourl;
			break;

		case 9:
			tourl = tiku_accountant_card_tourl;
			break;
		case 10:
			tourl = tiku_accountant_up_tourl;
			break;
		case 11:
			tourl = tiku_lastexam_tourl;
			break;
		case 12:
			tourl = tiku_shicao_tourl;
			break;

		}
		return tourl;
	}

	/**
	 * 从前台获取toUrl 跳转到题库的toUrl
	 * 
	 * @param access_token
	 * @param type
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "/ks/kaoshiUrl/{access_token}/{type}", method = RequestMethod.POST)
	private Map<Object, Object> kaoshiUrl(@PathVariable String access_token,
			@PathVariable int type, HttpServletRequest req,
			HttpServletResponse res) {
		String toUrl = req.getParameter("toUrl");
		if (StringUtils.isBlank(toUrl)) {
			Map map = new HashMap();
			map.put("code", 30406);
			map.put("msg", "参数无效");
			return map;
		}

		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("code", "30701");
		map.put("msg", "access_token失效");
		try {
			Map<Object, Object> userRedis = mainRedis.opsForHash().entries(
					"token:" + access_token);
			if (null != userRedis && null != userRedis.get("_id")) {
				Integer user_id = Integer.valueOf(userRedis.get("_id")
						.toString());
				DBObject user = mainMongo.getCollection("users").findOne(
						new BasicDBObject("_id", user_id));
				List<QQUser> qqlist;
				QQUser qquser;
				if (null != user) {
					if (null != user.get("ackey")) {
						qqlist = qqUserRepositery.findByTuid(user.get("tuid")
								.toString());
						if (qqlist != null && qqlist.size() > 0) {
							qquser = qqlist.get(0);
							// username
							String username = qquser.getUsername();
							// ackey
							String ackey = (String) user.get("ackey");

							String openid = AES.aesEncrypt(username,
									Constant.TiKu.AESKEY);
							// 按照会答密钥解密 再通过会计城密钥加密
							String openkey = AES.aesEncrypt(AES.aesDecrypt(
									ackey, Constant.TiKu.ACAESKEY),
									Constant.TiKu.AESKEY);
							//
							String token = MsgDigestUtil.MD5.getMD5(openid
									+ openkey + Constant.TiKu.SECRET_KEY);
							// 跳转的URL
							String tourl = toUrl;

							map.put("code", "1");
							map.put("msg", "请求成功!");
							map.put("data", tiku_domain
									+ "SignLogin.aspx?openid=" + openid
									+ "&openkey=" + openkey + "&token=" + token
									+ "&tourl=" + tourl);
						}
					} else {
						map.put("code", "30702");
						map.put("msg", "账号未同步到题库!");
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;

	}

	/**
	 * 我的购买
	 */
	@RequestMapping(value = "/ks/myShopUrl/{access_token}", method = RequestMethod.POST)
	private Map<Object, Object> myShopUrl(@PathVariable String access_token,
			HttpServletRequest req, HttpServletResponse res) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("code", "30701");
		map.put("msg", "access_token失效");
		try {
			Map<Object, Object> userRedis = mainRedis.opsForHash().entries(
					"token:" + access_token);
			if (null != userRedis && null != userRedis.get("_id")) {

				map.put("code", "1");
				map.put("msg", "请求成功!");
				map.put("data", usdomain + "/redirect_to_other?access_token="
						+ access_token + "&rd_url=" + kjcityh5domain
						+ "/UserManage/myorders.aspx");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;

	}

	// /**
	// * 我的购买
	// */
	// @RequestMapping(value = "/ks/myShopUrl/{access_token}", method =
	// RequestMethod.POST)
	// private Map<Object , Object> myShopUrl(@PathVariable String access_token,
	// HttpServletRequest req,HttpServletResponse res){
	//
	// Map<Object , Object> map = new HashMap<Object , Object>();
	// map.put("code", "30701");
	// map.put("msg", "access_token失效");
	// try {
	// Map<Object , Object> userRedis = mainRedis.opsForHash().entries("token:"
	// + access_token);
	// if(null != userRedis && null != userRedis.get("_id")){
	// Integer user_id = Integer.valueOf(userRedis.get("_id").toString());
	// DBObject user = mainMongo.getCollection("users").findOne(new
	// BasicDBObject("_id", user_id));
	// List<QQUser> qqlist;
	// QQUser qquser ;
	// if(null != user){
	// if(null != user.get("ackey")){
	// qqlist = qqUserRepositery.findByTuid(user.get("tuid").toString());
	// if(qqlist != null && qqlist.size() > 0){
	// qquser = qqlist.get(0);
	// //username
	// String username = qquser.getUsername();
	// //ackey
	// String ackey = (String) user.get("ackey");
	//
	// String openid = AES.aesEncrypt(username, Constant.TiKu.AESKEY);
	// //按照会答密钥解密 再通过会计城密钥加密
	// String openkey = AES.aesEncrypt(AES.aesDecrypt(ackey,
	// Constant.TiKu.ACAESKEY) , Constant.TiKu.AESKEY);
	// //
	// String token = MsgDigestUtil.MD5.getMD5(openid + openkey +
	// Constant.TiKu.SECRET_KEY);
	// //跳转的URL
	// String tourl = tiku_myshop_tourl;
	//
	// map.put("code", "1");
	// map.put("msg", "请求成功!");
	// map.put("data", tiku_domain + "SignLogin.aspx?openid=" + openid +
	// "&openkey=" + openkey + "&token=" + token + "&tourl=" + tourl);
	// }
	// }else{
	// map.put("code", "30702");
	// map.put("msg", "账号未同步到题库!");
	// }
	// }
	//
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return map;
	//
	// }

	/**
	 * 题库地址列表V2.0
	 */
	/*
	 * @RequestMapping(value = "/ks/tikuList/{access_token}", method =
	 * RequestMethod.GET) public Map<Object , Object> tikuList(@PathVariable
	 * String access_token, HttpServletRequest req,HttpServletResponse res){
	 * Map<Object , Object> map = new HashMap<Object , Object>();
	 * map.put("code", 30405); map.put("msg", "access_token失效");
	 * 
	 * 
	 * try { //地址列表 List<DBObject> tikuList =
	 * mainMongo.getCollection("tiku_url").find( new BasicDBObject("is_use",
	 * true), new BasicDBObject("name", 1 ).append("url", 1) ).sort(new
	 * BasicDBObject("order", 1)).limit(100).toArray(); if(null == tikuList ||
	 * tikuList.size() == 0){ map.put("code", "1"); map.put("data", null);
	 * return map; } //用户信息 Map<Object , Object> userRedis =
	 * mainRedis.opsForHash().entries("token:" + access_token); if(null !=
	 * userRedis && null != userRedis.get("_id")){ Integer user_id =
	 * Integer.valueOf(userRedis.get("_id").toString()); DBObject user =
	 * mainMongo.getCollection("users").findOne(new BasicDBObject("_id",
	 * user_id)); List<QQUser> qqlist; QQUser qquser ;
	 * 
	 * if(null != user){ if(null != user.get("tuid")){ qqlist =
	 * qqUserRepositery.findByTuid(user.get("tuid").toString()); if(qqlist !=
	 * null && qqlist.size() > 0){ qquser = qqlist.get(0); //username String
	 * username = qquser.getUsername();
	 * 
	 * String url = "http://w.kjcity.com/ajax/TrainingCourse.ashx?openid=" +
	 * username; String json = HttpClientUtil.get(url, null,
	 * HttpClientUtil.UTF8);
	 * 
	 * } }
	 * 
	 * 
	 * if(null != user.get("ackey")){ qqlist =
	 * qqUserRepositery.findByTuid(user.get("tuid").toString()); if(qqlist !=
	 * null && qqlist.size() > 0){ qquser = qqlist.get(0); //username String
	 * username = qquser.getUsername(); //ackey String ackey = (String)
	 * user.get("ackey");
	 * 
	 * String openid = AES.aesEncrypt(username, Constant.TiKu.AESKEY);
	 * //按照会答密钥解密 再通过会计城密钥加密 String openkey =
	 * AES.aesEncrypt(AES.aesDecrypt(ackey, Constant.TiKu.ACAESKEY) ,
	 * Constant.TiKu.AESKEY); // String token = MsgDigestUtil.MD5.getMD5(openid
	 * + openkey + Constant.TiKu.SECRET_KEY);
	 * 
	 * //拼装 List<Map<String , Object>> list = new ArrayList<Map<String ,
	 * Object>>(tikuList.size()); for(DBObject dbo : tikuList){ Map<String ,
	 * Object> m = new HashMap<String , Object>(); //名称 m.put("name",
	 * dbo.get("name")); //地址 m.put("url", tiku_domain +
	 * String.format(dbo.get("url").toString() , openid , openkey , token));
	 * list.add(m); }
	 * 
	 * map.put("code", "1"); map.put("msg", "请求成功!"); map.put("data", list); }
	 * }else{ map.put("code", "30702"); map.put("msg", "账号未同步到题库!"); } }
	 * 
	 * } } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * return mapb; }
	 */

	public List<Map<String, Object>> tikuUrlList(String access_token) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String _gid = null;
		String openid = null;
		String openkey = null;
		String token = null;
		// 根据token获取用户手机号
		Map<Object, Object> userRedis = mainRedis.opsForHash().entries(
				"token:" + access_token);
		if (null != userRedis && null != userRedis.get("_id")) {
			// 用户id
			Integer user_id = Integer.valueOf(userRedis.get("_id").toString());
			// 用户信息
			DBObject user = mainMongo.getCollection("users").findOne(
					new BasicDBObject("_id", user_id));
			List<QQUser> qqlist;
			QQUser qquser;

			if (null != user) {

				if (null != user.get("tuid")) {
					// tuid获取手机号
					qqlist = qqUserRepositery.findByTuid(user.get("tuid")
							.toString());
					if (qqlist != null && qqlist.size() > 0) {
						qquser = qqlist.get(0);
						// username
						String username = qquser.getUsername();

						String url = TK_URL + username;
						String json = null;
						try {
							// 请求题库 获取gid
							// System.out.println("url------>:" + url);
							json = HttpClientUtil4_3.get(url,
									new HashMap<String, String>(),
									HttpClientUtil.UTF8);
						} catch (IOException e) {
							e.printStackTrace();
						}

						// 解析gid
						try {
							if (StringUtils.isNotBlank(json)) {
								Map<String, Object> _jsonMap = JSONUtil
										.jsonToMap(json);

								if (null != _jsonMap
										&& null != _jsonMap.get("Gid")) {
									_gid = _jsonMap.get("Gid").toString();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						// ackey
						String ackey = (String) user.get("ackey");

						try {
							openid = AES.aesEncrypt(username,
									Constant.TiKu.AESKEY);
						} catch (Exception e) {
							e.printStackTrace();
						}
						// 按照会答密钥解密 再通过会计城密钥加密
						try {
							openkey = AES.aesEncrypt(AES.aesDecrypt(ackey,
									Constant.TiKu.ACAESKEY),
									Constant.TiKu.AESKEY);
						} catch (Exception e) {
							e.printStackTrace();
						}
						//
						try {
							token = MsgDigestUtil.MD5.getMD5(openid + openkey
									+ Constant.TiKu.SECRET_KEY);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		if (StringUtils.isBlank(openid) || StringUtils.isBlank(openkey)
				|| StringUtils.isBlank(token)) {
			return null;
		}

		// 地址列表
		List<DBObject> tikuList = mainMongo
				.getCollection("tiku_url")
				.find(new BasicDBObject("is_use", true),
						new BasicDBObject("name", 1).append("url", 1).append(
								"gid", 1)).sort(new BasicDBObject("order", 1))
				.limit(100).toArray();
		if (null == tikuList || tikuList.size() == 0) {
			return null;
		}

		// 占住第一个结果集
		list.add(null);
		// 拼装
		for (DBObject dbo : tikuList) {
			Map<String, Object> m = new HashMap<String, Object>();
			// gid
			String gid = dbo.get("gid").toString();

			// 名称
			m.put("name", dbo.get("name"));

			// 地址
			String url = tiku_domain
					+ String.format(dbo.get("url").toString(), openid, openkey,
							token);
			// 修改https为http 开发线专用  正式线要记得去掉
			url.replaceFirst("https", "http");
			m.put("url", url);
			// 如果考试网有排序 放在首位
			if (StringUtils.isNotBlank(_gid) && gid.equals(_gid)) {
				// 如果第一位还是站位 删除
				if (list.get(0) == null) {
					list.remove(0);
				}
				list.add(0, m);
			} else {

				list.add(m);
			}
		}

		// 如果第一位还是站位 删除
		if (list.get(0) == null) {
			list.remove(0);
		}

		return list;
	}

	@RequestMapping(value = "/ks/tikuList/{access_token}", method = RequestMethod.GET)
	public Map<Object, Object> tikuList(@PathVariable String access_token,
			HttpServletRequest req, HttpServletResponse res) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("code", 1);
		map.put("data", tikuUrlList(access_token));

		return map;
	}

	/**
	 * 题库限制地址列表V2.0
	 */
	@RequestMapping(value = "/ks/tikuListV201/{access_token}", method = RequestMethod.GET)
	public Map<Object, Object> tikuList_v201(@PathVariable String access_token,
			HttpServletRequest req, HttpServletResponse res) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		
		map.put("code", "30405");
		map.put("msg", "access_token失效");
		map.put("data", data);
		try {
			boolean expired = true;
			String result = HttpClientUtil4_3.get(SSO_TOKENEXPIRED_GET+"?token="+access_token,null);
			JSONObject object = JSONObject.fromObject(result);
			if(200 == object.getInt("code")) {
				expired = object.getJSONObject("data").getBoolean("expired");
			}
			if(expired) {
				return map;
			}
			Map<Object, Object> userRedis = mainRedis.opsForHash().entries(
					"token:" + access_token);
			/*2018-02-26 写死为一个地址数据*/
			//List<Map<String, Object>> list = tikuUrlList(access_token);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String,Object> mapTiku = new HashMap<String,Object>();
			mapTiku.put("name", "在线题库");
			//mapTiku.put("url","https://mtiku.hqjy.com/page/home.html?token="+access_token);
			mapTiku.put("url",tikuDomainNew+"?token="+access_token);
			list.add(mapTiku);
			
			// 限制地址集合
			List<String> limitList = new ArrayList<String>();

			// 过滤地址列表
			List<DBObject> tikuLimitList = mainMongo
					.getCollection("tiku_limit_url")
					.find(null, new BasicDBObject("url", 1)).limit(1000)
					.toArray();

			if (null != tikuLimitList && tikuLimitList.size() > 0) {
				for (DBObject dbo : tikuLimitList) {
					if (dbo != null) {
						limitList.add(dbo.get("url").toString());
					}
				}
			}

			data.put("tikuList", list);
			data.put("tikuLimitList", limitList);

			map.put("code", "1");
			map.put("msg", "请求成功!");
			map.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 题库限制地址列表V2.0
	 */
	@RequestMapping(value = "/ks/tikuLimitList", method = RequestMethod.GET)
	public Map<Object, Object> tikuLimitList_v300(HttpServletRequest req,
			HttpServletResponse res) {
		Map<Object, Object> map = new HashMap<Object, Object>();

		// 限制地址集合
		List<String> limitList = new ArrayList<String>();

		// 过滤地址列表
		List<DBObject> tikuLimitList = mainMongo
				.getCollection("tiku_limit_url")
				.find(null, new BasicDBObject("url", 1)).limit(1000).toArray();

		if (null != tikuLimitList && tikuLimitList.size() > 0) {
			for (DBObject dbo : tikuLimitList) {
				if (dbo != null) {
					limitList.add(dbo.get("url").toString());
				}
			}
		}

		map.put("code", "1");
		map.put("msg", "请求成功!");
		map.put("data", limitList);

		return map;
	}

}

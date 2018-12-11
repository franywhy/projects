package com.izhubo.userSystem.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hqonline.model.HK;
import com.izhubo.rest.common.util.MsgDigestUtil;
import com.izhubo.rest.persistent.KGS;
import com.izhubo.userSystem.mongo.qquser.QQUser;
import com.izhubo.userSystem.mongo.qquser.QQUserRepositery;
import com.izhubo.userSystem.utils.AES;
import com.izhubo.userSystem.utils.HttpUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Controller
public class TeacherApplyController {

	@Resource
	protected StringRedisTemplate mainRedis;

	@Resource
	protected MongoTemplate mainMongo;
	@Resource
	protected MongoTemplate qquserMongo;
	
	@Resource
	protected MongoTemplate adminMongo;
	

	@Autowired
	protected QQUserRepositery qqUserRepositery;

	@Resource
	protected RegisterController registerController;
	
	
	@Resource
	protected UserApiController userApiController;
	
	private static final String AESKEY = "%^$AF>.12*******";

	@Resource
	KGS userKGS;

	@RequestMapping("/api_teacher_apply")
	public Map<String, Object> api_teacher_apply(HttpServletRequest req,
			HttpServletResponse res) throws Exception {

		// HuanxinHelper hx = new HuanxinHelper();

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String json = req.getParameter("json");

		String map = "";
		JSONObject item = new JSONObject();
		item = item.fromObject(json);
		String username = (String) item.get("username");
		String checkcode = (String) item.get("checkcode");
		String nickname = (String) item.get("nickname");
		 JSONArray insJson = new  JSONArray();
		 insJson = insJson.fromObject(item.get("insjson"));

		if (!CheckCode(username, checkcode)) {
			resultMap.put("code", "0");
			resultMap.put("data", "验证码错误");

		} else {

		
			if (this.qqUserRepositery.findByUsername(username).size() > 0) {

					QQUser qqUser = this.qqUserRepositery.findByUsername(
							username).get(0);
					DBObject user = mainMongo.getCollection("users").findOne(
							new BasicDBObject("tuid", qqUser.getTuid()));

					if (user.get("priv").toString().equals("2")) {
						resultMap.put("data",
								"您已经具备抢答权限，请尝试用恒企教育员工APP登录，如无法登录，请尝试在恒企教育员工APP中重置密码");
						resultMap.put("code", 3);
						
						AddApply(username,nickname,"","结果：申请失败，原因：已经开通过，无需再申请");
						
					} else {
						
						JSONObject result = CheckTeacher(username);
						if(result.get("code").toString().equals("1"))
						{
							userApiController.updateTeacher(qqUser.getTuid(),nickname,insJson.toString());
							resultMap.put("data","开通成功，您可以登录恒企员工端了，密码是原先会答的密码");
							resultMap.put("code", 1);
							
							AddApply(username,nickname,"","结果：申请成功");
						}
						else
						{
							resultMap.put("data","您的账号不具备会答教师资格，请联系运营");
							resultMap.put("code", 3);
							
							AddApply(username,nickname,"","结果：申请失败，原因：nc审核不通过，该老师填写的手机号，在NC里面没有收录。不具备教师资格");
						}
						
					
					}

				} else {
					
					
					JSONObject result = CheckTeacher(username);
					if(result.get("code").toString().equals("1"))
					{
						AddTeacher(item);
						resultMap.put("data","开通成功，您可以登录会答教师端了，第一次登录，可以用找回密码功能设置密码");
						resultMap.put("code", 1);
						AddApply(username,nickname,"","结果：申请成功");
					}
					else
					{
						resultMap.put("data","您的账号不具备会答教师资格，请联系运营");
						resultMap.put("code", 3);
						AddApply(username,nickname,"","结果：申请失败，原因：nc审核不通过，该老师填写的手机号，在NC里面没有收录。不具备教师资格");
					}
				}
			
		}

		return resultMap;

	}
	
	private static JSONObject CheckTeacher(String username)
	{
		   Map<String , String> parameter = new HashMap<String , String>();
	        parameter.put("loginname", username);
	        parameter.put("token", MsgDigestUtil.MD5.MD5ForDotNet(username+AESKEY).toLowerCase());
	        
	        String sendRes = HttpUtils.sendGet("http://nc.kjcity.com/api/SyncTeaching.ashx", parameter);
	        
	        

			
	        JSONObject checkresult = new JSONObject();
	        checkresult = checkresult.fromObject(sendRes);
	        
	       return checkresult;
	}
	
	
	private Map<String, Object> AddTeacher(JSONObject item) throws Exception
	{
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String temppsw = "acb123";
		temppsw = MsgDigestUtil.MD5.digest2HEX(temppsw);
		
		
		String username = (String) item.get("username");
		String nickname = (String) item.get("nickname");// 昵称和用户名一样啦
		String city = (String) item.get("city");
		String school = (String) item.get("school");
		String star = (String) item.get("star").toString();
		String isPro = (String) item.get("ispro");
		String checkcode = (String) item.get("checkcode");
		JSONArray jinsjson = (JSONArray) item.get("insjson");

		QQUser qqUser = new QQUser();
		qqUser.setNickName(nickname);
		qqUser.setPassword(temppsw);
		qqUser.setQd("teacherapp");
		qqUser.setTuid(UUID.randomUUID().toString());
		qqUser.setUsername(username);
		this.qqUserRepositery.save(qqUser);
		
	
		
		DBObject user =  userApiController.addTeacher(qqUser.getTuid(), nickname, temppsw, "", jinsjson.toString());
		
	    HuanxinController hx = new HuanxinController();
		hx.AddUser(user.get("_id").toString(), temppsw, mainRedis);
        
        return resultMap;
		
	}
		
		
	
	
	private void AddApply(String username,String nickname,String star,String result)
	{
		DBObject obj = new BasicDBObject();
		
		obj.put("_id", UUID.randomUUID().toString());
		obj.put("username", username);
		obj.put("nickname", nickname);
		obj.put("city", "");
		obj.put("star", star);
		obj.put("school", "");
		obj.put("result", result);
		
		Map<String, Object> varMap = new HashMap<String, Object>();
		
		varMap.put("create_user_id", 0);
		varMap.put("update_user_id", 0);
		varMap.put("audit_flag", false);
		varMap.put("update_date", System.currentTimeMillis());
		varMap.put("upload_flag", 1);
		varMap.put("timestamp", System.currentTimeMillis());
	
		obj.put("manage_info", varMap);

		adminMongo.getCollection("teacher_apply").save(obj);
	}

	// 校验验证码
	public boolean CheckCode(String phone, String security) {
		boolean res = false;
		if (StringUtils.isNotBlank(security) && StringUtils.isNotBlank(phone)) {
			String rs = mainRedis.opsForValue().get(
					HK.SECURITY.getSecurityKey(phone));
			res = security.equals(rs);
		}
		return res;
	}
	
	
	public static void main(String[] args) throws Exception {

		//String key = AES.aesDecrypt("ei9Uy7tpL7zUXTt34xv3/A==", ACAESKEY);
		
		String result = CheckTeacher("15038227339").toString();
		System.out.println("cal " + result);
		


	}

}

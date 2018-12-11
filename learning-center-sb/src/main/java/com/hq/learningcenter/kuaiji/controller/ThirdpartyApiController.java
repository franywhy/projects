package com.hq.learningcenter.kuaiji.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hq.learningcenter.kuaiji.pojo.ZegoResponsePOJO;
import com.hq.learningcenter.school.controller.AbstractBaseController;
import com.hq.learningcenter.utils.JSONUtil;
import com.hq.learningcenter.kuaiji.pojo.ZegoRequestPOJO;
import com.hq.learningcenter.kuaiji.service.LiveLogZegoDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hq.learningcenter.kuaiji.entity.LiveLogZegoDetailEntity;
import com.hq.learningcenter.kuaiji.pojo.ZegoTokenObjPOJO;

/**
 * 
 * toekn验证类
 * @author Vince
 * @email 
 * @date 2017-09-28
 */
@Controller
@RequestMapping("thirdpartyApi")
public class ThirdpartyApiController extends AbstractBaseController {
	
	@Resource
	protected StringRedisTemplate mainRedis;
	
	@Autowired
	private LiveLogZegoDetailService liveLogZegoDetailService;
	
	/**
	 * toekn验证
	 */
	@ResponseBody
	@RequestMapping("/tokenVerify")
	public ResponseEntity<Object> tokenVerify(HttpServletRequest request,HttpServletResponse response,ZegoRequestPOJO zegoRequest){
		//参数验证
//		Integer version = zegoRequest.getVersion();
//		Integer seq = zegoRequest.getSeq();
//		Integer appid = zegoRequest.getAppid();
		
		String zkey = zegoRequest.getToken();
		Map<Object, Object> successCoure = new HashMap<Object, Object>();
		if(zkey==null || "".equals(zkey)){
			successCoure.put("code", 101);
			successCoure.put("message", "token not exist");
			return success("",successCoure);
		}
		
		//后门处理START
		TimeUnit validTime = TimeUnit.MILLISECONDS;
		if(zkey.equals("simulationtoken123")){
			ZegoTokenObjPOJO zegoTokenObj = new ZegoTokenObjPOJO();
			zegoTokenObj.setCourse_id("7fb2ea3be02a44c599f4a4ac0235a018");//非固定
			zegoTokenObj.setCourse_name("直播间");
			zegoTokenObj.setTimestamp(System.currentTimeMillis());
			zegoTokenObj.setUser_id("10011043");
			zegoTokenObj.setUser_name("测试人员_老师");
			zegoTokenObj.setRole(1);
			
			String zjson = JSONUtil.beanToJson(zegoTokenObj);
			mainRedis.opsForValue().set("simulationtoken123", zjson, System.currentTimeMillis(),validTime);
		}else if(zkey.equals("student1")){
			ZegoTokenObjPOJO zegoTokenObj2 = new ZegoTokenObjPOJO();
			zegoTokenObj2.setCourse_id("7fb2ea3be02a44c599f4a4ac0235a018");//非固定
			zegoTokenObj2.setCourse_name("直播间");
			zegoTokenObj2.setTimestamp(System.currentTimeMillis());
			zegoTokenObj2.setUser_id("10011044");
			zegoTokenObj2.setUser_name("测试人员_学员");
			zegoTokenObj2.setRole(2);
			
			String zjson2 = JSONUtil.beanToJson(zegoTokenObj2);
			//保存一个redis
			mainRedis.opsForValue().set("student1", zjson2, System.currentTimeMillis(),validTime);
		}
		//后门处理END
		
		//在redis中获取一个对象
		String ZegoToken = mainRedis.opsForValue().get(zkey);
		if(ZegoToken==null || "".equals(ZegoToken)){
			successCoure.put("code", 102);
			successCoure.put("message", "redis not exist");
			return success("",successCoure);
		}
		Map<String, Object> Rmap = JSONUtil.jsonToMap(ZegoToken);
		
		Integer version = zegoRequest.getVersion();
		Integer seq = zegoRequest.getSeq();
		Long appid = 3525410038L;
		Integer biz_type = 0;
		String room_id = (String) Rmap.get("course_id");
		String room_name = (String) Rmap.get("course_name");
		String user_id = (String) Rmap.get("user_id");
		String user_name = (String) Rmap.get("user_name");
		Integer role =  (Integer) Rmap.get("role");
		String avatar = "";
		Integer app_version = 0;
		String app_url = "";
		Integer upgrade = 0;
		String app_signature = "bd8e989ea83a755e150f64c3539e12bdf7144926270bf4e275044d4a0dd54d46";

		successCoure.put("code", 0);
		successCoure.put("message", "success");
		
		ZegoResponsePOJO zegoResponse = new ZegoResponsePOJO();
		zegoResponse.setVersion(version);
		zegoResponse.setSeq(seq);
		zegoResponse.setAppid(appid);
		zegoResponse.setBiz_type(biz_type);
		zegoResponse.setRoom_id(room_id);
		zegoResponse.setRoom_name(room_name);
		zegoResponse.setUser_id(user_id);
		zegoResponse.setUser_name(user_name);
		zegoResponse.setRole(role);
		zegoResponse.setAvatar(avatar);
		zegoResponse.setApp_version(app_version);
		zegoResponse.setApp_url(app_url);
		zegoResponse.setUpgrade(upgrade);
		zegoResponse.setApp_signature(app_signature);
		
		return success(zegoResponse,successCoure);
	}
	
	
	/**
	 * 用户登录房间回调接口
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/inlog_room")
	public ResponseEntity<Object> inlogRoom(HttpServletRequest request, HttpServletResponse response,LiveLogZegoDetailEntity liveLogZegoDetail) {
		Map<Object, Object> successCoure = new HashMap<Object, Object>();
		liveLogZegoDetail.setIntoOut(1);
		if(liveLogZegoDetailService.save(liveLogZegoDetail)>0){
			successCoure.put("code", 1);
		}else{
			successCoure.put("code", 2);
		}
		
		return success("", successCoure);
	}
	
	/**
	 * 用户退出房间回调接口
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/outlog_room")
	public ResponseEntity<Object> outlogRoom(HttpServletRequest request, HttpServletResponse response,LiveLogZegoDetailEntity liveLogZegoDetail) {
		Map<Object, Object> successCoure = new HashMap<Object, Object>();
		liveLogZegoDetail.setIntoOut(2);
		if(liveLogZegoDetailService.save(liveLogZegoDetail)>0){
			successCoure.put("code", 1);
		}else{
			successCoure.put("code", 2);
		}
		return success("", successCoure);
	}
}

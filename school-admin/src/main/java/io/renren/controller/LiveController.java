package io.renren.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.entity.SysUserEntity;
import io.renren.pojo.ZegoTokenObjPOJO;
import io.renren.service.CourseClassplanLivesService;
import io.renren.service.MallLiveRoomService;
import io.renren.utils.HttpUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;
import io.renren.utils.JSONUtil;


/**
 * 直播
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 15:01:23
 */
@Controller
@RequestMapping("live")
public class LiveController extends AbstractController {
	@Autowired
	private MallLiveRoomService mallLiveRoomService;
	@Autowired
	private CourseClassplanLivesService courseClassplanLivesService; 
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final  String K_VAULE = "k_value:";
	
	private static Integer kTimeout = 0;
	@Value("#{application['pom.gensee.k.timeout']}")
	private void setKTimeout(Integer kTimeout){
		LiveController.kTimeout = kTimeout;
	}
	
	/**
	 * 获取直播跳转地址
	 * @throws UnsupportedEncodingException 
	 */
	@ResponseBody
	@RequestMapping("/getLiveUrl")
	public R getLiveUrl(HttpServletRequest request) throws UnsupportedEncodingException{
//		public R getLiveUrl(String classplanLiveId, HttpServletRequest request) throws UnsupportedEncodingException{
		String classplanLiveId = request.getParameter("classplanLiveId");
		String schoolId = SchoolIdUtils.getSchoolId(request);
		String nickName = null;
		SysUserEntity user = getUser();
		if(null != user){
			nickName = "后台管理员_" + user.getUsername();
		}
        if (null == classplanLiveId || null == nickName) {
            return R.error("缺少有效参数");
        } else {
        	return R.ok().put(getLiveUrl(classplanLiveId, nickName, schoolId));
        }
	}
	/**
	 * 获取直播跳转地址
	 * @throws UnsupportedEncodingException 
	 */
	private String getLiveUrl(String classplanLiveId, String nickName , String schoolId) throws UnsupportedEncodingException{
		if (null == classplanLiveId || null == nickName) {
			return null;
		} else {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("nickName", URLEncoder.encode(nickName,"UTF-8"));
			parameters.put("k", buildKValue(nickName));
			String url = mallLiveRoomService.queryLiveUrl(classplanLiveId, schoolId,parameters);
			return url;
		}
	}
	
	/**
	 * 获取直播回放地址
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@ResponseBody
	@RequestMapping("/getReplayUrl")
	private R getReplayUrl(HttpServletRequest request) throws UnsupportedEncodingException{
		String classplanLiveId = request.getParameter("classplanLiveId");
		String schoolId = SchoolIdUtils.getSchoolId(request);
		String nickName = null;
		SysUserEntity user = getUser();
		if(null != user){
			nickName = "后台管理员_" + user.getUsername();
		}
        if (null == classplanLiveId) {
            return R.error("缺少有效参数");
        } else {
        	//查询用参数
        	Map<String, Object> queryParam = new HashMap<String, Object>();
        	queryParam.put("classplanLiveId", classplanLiveId);
        	queryParam.put("schoolId", schoolId);
        	queryParam.put("dr", 0);
        	//url拼接参数
        	Map<String, Object> urlParam = new HashMap<String, Object>();
        	urlParam.put("nickName", URLEncoder.encode(nickName,"UTF-8"));
        	urlParam.put("k", buildKValue(String.valueOf(nickName)));
        	String replayUrl = courseClassplanLivesService.queryReplayUrl(queryParam, urlParam);
            
            return R.ok().put(replayUrl);
        }
	}
	private String buildKValue(String uid){
        //用#将liveId,uid和毫秒级时间戳拼接成k值原码
        Long timestamp = new Date().getTime();
        String keyStr = uid + "#" + timestamp;
        //用md5加密后得到k值
        String k = DigestUtils.md5Hex(keyStr);
        String mapKey = K_VAULE + k;
        //保存k值到redis
        //this.setToCache(mapKey, this.kTimeout, timestamp);
        this.mainRedis.opsForValue().set(mapKey, String.valueOf(timestamp), LiveController.kTimeout, TimeUnit.MILLISECONDS);
        return k;
    }
	
	/**
	 * 调起学来学往桌面应用
	 * @param classplanLiveId 直播房间ID
	 * 
	 */
	
	@ResponseBody
	@RequestMapping("/getPOLYVUrl")
	private R getPOLYVUrl(HttpServletRequest request,String classplanLiveId,String classplanLiveName) throws UnsupportedEncodingException{
		String zkey = io.renren.utils.ProduceToken.getRandomStr(16);
		SysUserEntity sysuser = getUser();
		
		ZegoTokenObjPOJO zegoTokenObj = new ZegoTokenObjPOJO();
		zegoTokenObj.setCourse_id(classplanLiveId);
		zegoTokenObj.setCourse_name(classplanLiveName);
		zegoTokenObj.setTimestamp(System.currentTimeMillis());
		zegoTokenObj.setUser_id(sysuser.getUserId().toString());
		zegoTokenObj.setUser_name(sysuser.getNickName());
		zegoTokenObj.setRole(1);
		
		String zjson = io.renren.utils.JSONUtil.beanToJson(zegoTokenObj);
		schoolLearningRedis.opsForValue().set(zkey,zjson,180000,TimeUnit.MILLISECONDS);
		
		String URL = "xuelxuew://login/?lang=zh&sign=9cbcf1c8321ca5ef31b803dc49530918&token="+zkey;
		return R.ok().put(URL);
	}
	
	/**
	 * 获取并保存录制件
	 * @param request
	 * @return
	 * @throws ParseException 
	 * @throws ServletRequestBindingException 
	 */
	@ResponseBody
	@RequestMapping("/savePlaybackData")
	public R savePlaybackData(HttpServletRequest request){
		try {
			Integer datePicker = ServletRequestUtils.getIntParameter(request, "datePicker");
			String startCountTime = ServletRequestUtils.getStringParameter(request, "startCountTime");
			String endCountTime = ServletRequestUtils.getStringParameter(request, "endCountTime");
			logger.info(String.format("\nsavePlaybackData param is %s\n%s\n%s",datePicker,startCountTime,endCountTime));
			Map<String, Object> result = courseClassplanLivesService.updataPlaybackData(datePicker,startCountTime, endCountTime);
			if (null == result || "".equals(result)) {
				return R.error();
			}
			if (result.get("code").equals(0)) {
				return R.ok();
			} else if (result.get("code").equals(1)) {
				return R.error(result.get("message").toString());
			} else if (result.get("code").equals(2)) {
				return R.error(result.get("message").toString());
			}else if (result.get("code").equals(3)) {
				return R.error(result.get("message").toString());
			}			else {
				return R.error();
			}
		} catch (Exception e) {
			return R.error();
		}
	}

	
}

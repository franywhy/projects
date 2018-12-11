package com.school.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.school.entity.CourseClassplanLivesEntity;
import com.school.service.CourseClassplanLivesService;
import com.school.service.LiveService;
import com.school.service.MessageService;
import com.school.utils.DateUtils;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/gensee")
public class CallBackController {

	@Autowired
	private LiveService liveService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private CourseClassplanLivesService courseClassplanLivesService;
	
	private static final  String K_VAULE = "k_value:";
	
	@RequestMapping(value = "/liveCallback", method = RequestMethod.GET)
    public void genseeLiveCallBack(HttpServletRequest request, HttpServletResponse response) {
        
		String liveId = request.getParameter("ClassNo");
		String userId = request.getParameter("Operator");
		String action = request.getParameter("Action");
		
		if(null != action){
			switch(action){
				case "101" : 
					pushLiveInfo2Queue(liveId,userId,0,101);
					break;
				case "107" : 
					pushLiveInfo2Queue(liveId,userId,1,107);
					break;
				case "110" : 
					pushLiveInfo2Queue(liveId,userId,1,110);
					break;
				case "105" : 
					pushLiveInfo2Queue(liveId,null,2,105);
					//updateClassplanLiveStartTime(liveId);
					break;
			}
		}
    }
	
	@RequestMapping(value = "/replayCallback", method = RequestMethod.GET)
    public void genseeReplayCallBack(HttpServletRequest request, HttpServletResponse response) {
        
		String videoId = request.getParameter("ClassNo");
		String userId = request.getParameter("Operator");
		String action = request.getParameter("Action");
		
		if(null != action){
			switch(action){
				case "111" :
					pushReplayInfo2Queue(videoId,userId,0);
					break;
				case "112" :
					pushReplayInfo2Queue(videoId,userId,1);
					break;
			}
		}
    }
	
	@ApiOperation(value = "展示互动K值验证")
    @RequestMapping(value = "/genseeKValueVerify", method = RequestMethod.POST)
	@ResponseBody
    public String polyvOliveVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            String k = request.getParameter("k");
            String mapKey = K_VAULE + k;
            return liveService.verifyGenseeKey(mapKey);

        } catch (Throwable t) {
            return "fail";
        }
    }
	
	private void pushLiveInfo2Queue(String liveId, String userId, int type, Integer action){
		if(type == 2){
			Map<String,Object> messageMap = new HashMap<String,Object>();
			messageMap.put("liveId", liveId);
			messageMap.put("createTime", new Date().getTime());
			messageMap.put("type", type);
			messageMap.put("action", action);
			messageService.pushLiveMessageToQueue(messageMap);
		}else{
			Pattern pattern = Pattern.compile("10[0-9]{8}");
			Matcher matcher = pattern.matcher(userId);
			if(matcher.find()){
				Map<String,Object> messageMap = new HashMap<String,Object>();
				messageMap.put("liveId", liveId);
				messageMap.put("userId", Long.valueOf(userId) - 1000000000);
				messageMap.put("createTime", new Date().getTime());
				messageMap.put("type", type);
				messageMap.put("action", action);
				messageService.pushLiveMessageToQueue(messageMap);
			}
		}
	}
	
	private void pushReplayInfo2Queue(String videoId, String userId, int type){
		Pattern pattern = Pattern.compile("10[0-9]{8}");
		Matcher matcher = pattern.matcher(userId);
		if(matcher.find()){
            Map<String,Object> messageMap = new HashMap<String,Object>();
            messageMap.put("videoId", videoId);
            messageMap.put("userId", Long.valueOf(userId) - 1000000000);
            messageMap.put("createTime", new Date().getTime());
            //type:时间的类型 0进入 1退出 2离线(有进入和退出时间)
            messageMap.put("type", type);
            messageMap.put("joinTime", 100);
            messageMap.put("leaveTime", 100);
            messageMap.put("videoStartTime", 100);
            messageMap.put("videoEndTime", 100);
            messageMap.put("videoTotalTime", 100);
            //是否离线  0离线(缓存)  1回放回调
            messageMap.put("isOfflive", 1);
            messageService.pushReplayMessageToQueue(messageMap);
		}
	}
	
	//根据教室关闭直播间时间,更新classplanLive的结束时间
	private void updateClassplanLiveStartTime(String liveId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("liveId", liveId);
		map.put("startTime", DateUtils.format(new Date())+" 00:00:00");
		map.put("endTime", DateUtils.format(new Date())+" 23:59:59");
		CourseClassplanLivesEntity classplanLive = courseClassplanLivesService.queryByLiveId(map);
		if(null != classplanLive){
			classplanLive.setEndTime(new Date());
			
			courseClassplanLivesService.update(classplanLive);
		}
	}
}

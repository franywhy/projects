package io.renren.task;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.renren.entity.MsgContentEntity;
import io.renren.service.MsgContentService;
import io.renren.service.XinggePushService;
import io.renren.utils.SpringContextUtils;

@Component("io.renren.task.PushMsgContentJob")
public class PushMsgContentJob {

	private static Logger logger = LoggerFactory.getLogger(PushMsgContentJob.class);
	
	public void execute(Map<String,Object> params) {
		String contentId = (String) params.get("contentId");
		String schoolId = (String) params.get("schoolId");
		logger.info(
				"PushMsgContentJob启动：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " " + contentId);
		// TODO 根据contentId查询消息主表
		// 获取消息内容
		//实例化MsgContentService
		MsgContentService msgContentService = (MsgContentService)SpringContextUtils.getBean("msgContentService");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schoolId", schoolId);
		map.put("contentId", Long.parseLong(contentId));
		
		MsgContentEntity msgContentEntity = msgContentService.queryObject(map);
		String title = msgContentEntity.getContentTitle();
		
		//TODO 发送给指定用户群组
		//获取tag标签
		String tag = (String) params.get("tagList");
		String[] tags = tag.split(",");
		List<String> tagList = Arrays.asList(tags);
		//实例化XinggePushService
		XinggePushService xinggePushService = (XinggePushService) SpringContextUtils.getBean("xinggePushService");
		//TODO 调用信鸽接口
		xinggePushService.pushTagAndroidOrIos(tagList, title);
	}
	
	public void execute2(Map<String,Object> params){
		String contentId = (String) params.get("contentId");
		String schoolId = (String) params.get("schoolId");
		// TODO 根据contentId查询消息主表
		// 获取消息内容
		//实例化MsgContentService
		MsgContentService msgContentService = (MsgContentService)SpringContextUtils.getBean("msgContentService");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schoolId", schoolId);
		map.put("contentId", Long.parseLong(contentId));
		
		MsgContentEntity msgContentEntity = msgContentService.queryObject(map);
		String title = msgContentEntity.getContentTitle();
		
		//实例化XinggePushService
		XinggePushService xinggePushService = (XinggePushService) SpringContextUtils.getBean("xinggePushService");
		//TODO 调用信鸽接口
		xinggePushService.pushAllAccount(title);
	}
}

package io.renren.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.renren.entity.VideoInfoEntity;
import io.renren.service.CourseClassplanLivesService;
import io.renren.service.VideoInfoService;
import io.renren.utils.DateUtils;
import io.renren.utils.HttpUtils;
import io.renren.utils.JSONUtil;
import io.renren.utils.SpringContextUtils;

@Component("io.renren.task.SynchronizeVideoInfoJob")
public class SynchronizeVideoInfoJob {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final Long HOUR_LONG = 3600000L;
	
	private static String loginName = "";
	@Value("#{application['pom.gensee.loginname']}")
	private void setLoginName(String loginName){
		SynchronizeVideoInfoJob.loginName = loginName;
	}
	
	private static String password = "";
	@Value("#{application['pom.gensee.password']}")
	private void setPassword(String password){
		SynchronizeVideoInfoJob.password = password;
	}
	
	private static String url = "";
	@Value("#{application['pom.gensee.webcast.vod.info']}")
	private void setUrl(String url){
		SynchronizeVideoInfoJob.url = url;
	}
	
	public void execute(Map<String,Object> params) {
		String schoolId = (String) params.get("schoolId");
		String date = (String) params.get("date");//日期参数用于手动操作
		logger.info("SynchronizeLiveLogJob启动：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		
		try{
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("schoolId", schoolId);
			if(null == date || date.equals("")){
				params1.put("startTime", DateFormatUtils.format(DateUtils.getDateBefore(new Date(), 1), "yyyy-MM-dd 00:00:00"));
				params1.put("endTime", DateFormatUtils.format(DateUtils.getDateBefore(new Date(), 1), "yyyy-MM-dd 23:59:59"));
			}else{
				if(DateUtils.matchDateString(date)){
					params1.put("startTime", date + " 00:00:00");
					params1.put("endTime", date + " 23:59:59");
				}else{
					logger.error("SynchronizeVideoLogJob matchDate:{}","parameter date no match format");
					return;
				}
			}
			
			CourseClassplanLivesService courseClassplanLivesService = (CourseClassplanLivesService)SpringContextUtils.getBean("courseClassplanLivesService");
			
			VideoInfoService videoInfoService = (VideoInfoService)SpringContextUtils.getBean("videoInfoService");
			
			//根据日期获取该日期的当天的所有排课明细
			List<Map<String, Object>> classplanLives = courseClassplanLivesService.queryByDate(params1);
			
			if(null != classplanLives && classplanLives.size() > 0){
				
				String classplanLiveId = null;
				String webcastId = null;
				Date liveStartTime = null;
				Date liveEndTime = null;
				String param = null;
				String logResult = null;
				Map<String,Object> logResultMap = null;//请求返回的结果
				
				List<Map<String,Object>> infoList = null;//请求返回的结果中的信息数据
				Map<String,Object> tmpVideoMapList = null;//临时缓存 (录播件:id-信息数据:map) 键值对
				Long createdTime = null;
				Map<String,Object> tmpVideoMap = null;//信息数据:map
				Long tmpCreatedTime = null;
				
				Map<String,Object> video = null;
				VideoInfoEntity videoInfoEntity = null;
				Long recordStartTime = null;
				Long recordEndTime = null;
				
				for(Map<String,Object> classplanLive : classplanLives){
					
					try {
						classplanLiveId = (String) classplanLive.get("classplanLiveId");
						webcastId = (String) classplanLive.get("liveId");
						liveStartTime = (Date) classplanLive.get("startTime");
						liveEndTime = (Date) classplanLive.get("endTime");
						param = "loginName="+loginName+"&password="+password+"&webcastId="+webcastId;
						
						logResult = HttpUtils.sendGet(url, param);
						
						//请求展示互动直播日志接口,得到返回数据
						logResultMap = JSONUtil.jsonToMap(logResult);
						if(((String)logResultMap.get("code")).equals("0")){
							//从返回结果,提取日志数据
							infoList = (List<Map<String, Object>>) logResultMap.get("recordList");
							tmpVideoMapList = new HashMap<String, Object>();
							if(null != infoList && infoList.size() > 0){
								//同一直播可能有多个录播件
								for(Map<String,Object> info : infoList){
									createdTime = (Long) info.get("recordStartTime");//获取该录播件信息的创建时间
									
									//当该录播件开始时间不在该排课的直播的时间段内,跳过该行信息
									if(createdTime < (liveStartTime.getTime() - HOUR_LONG) || createdTime > (liveEndTime.getTime() + HOUR_LONG)) continue;
									
									tmpVideoMap = (Map<String, Object>) tmpVideoMapList.get(classplanLiveId);
									//如果缓存中已有录播件信息,则比较其创建时间,保存更早的
									if(null != tmpVideoMap){
										tmpCreatedTime = (Long) tmpVideoMap.get("recordStartTime");//获取缓存中的录播件的创建时间
										if(tmpCreatedTime > createdTime) tmpVideoMapList.put(classplanLiveId, info);//如果该行记录创建时间更早,则覆盖之前的缓存,否则不处理
									}else {
										tmpVideoMapList.put(classplanLiveId, info);//缓存没有记录,则保存之
									}
								}
							}
							
							//保存每个直播间的最早创建的录播件信息
							if(tmpVideoMapList.size() > 0){
								for(Map.Entry<String,Object> videoEntry : tmpVideoMapList.entrySet()){
									try {
										video = (Map<String, Object>) videoEntry.getValue();
										videoInfoEntity = new VideoInfoEntity();
										videoInfoEntity.setVideoId((String)video.get("id"));
										videoInfoEntity.setLiveId(webcastId);
										videoInfoEntity.setBusinessId(classplanLiveId);
										videoInfoEntity.setBusinessType(0);
										
										recordStartTime = (Long)video.get("recordStartTime");
										recordEndTime = (Long)video.get("recordEndTime");
										videoInfoEntity.setStartTime(recordStartTime);
										videoInfoEntity.setEndTime(recordEndTime);
										videoInfoEntity.setVideoDuration(recordEndTime - recordStartTime);
										videoInfoEntity.setUrl((String) video.get("url"));
										videoInfoEntity.setSchoolId(schoolId);
										videoInfoEntity.setCreateTime(new Date());
										videoInfoService.save(videoInfoEntity);
									} catch (Exception ex) {
										logger.error("SynchronizeVideoInfoJob save video_info:{}",ex.getMessage());
									}
								}
							}
						}else{
							
						}
					} catch (Exception ex) {
						logger.error("SynchronizeVideoInfoJob getVideoInfo:{}",ex.getMessage());
					}
				}
			}
		}catch(Exception ex){
			logger.error("SynchronizeVideoInfoJob execute:{}",ex.getMessage());
		}
	}
}

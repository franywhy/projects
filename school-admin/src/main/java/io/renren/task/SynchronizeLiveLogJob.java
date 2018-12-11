package io.renren.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.renren.entity.LiveLogDetailEntity;
import io.renren.entity.LogWatchEntity;
import io.renren.service.CourseClassplanLivesService;
import io.renren.service.LiveLogDetailService;
import io.renren.service.LogWatchService;
import io.renren.utils.DateUtils;
import io.renren.utils.HttpUtils;
import io.renren.utils.JSONUtil;
import io.renren.utils.SpringContextUtils;

@Component("io.renren.task.SynchronizeLiveLogJob")
public class SynchronizeLiveLogJob{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static String loginName = "";
	@Value("#{application['pom.gensee.loginname']}")
	private void setLoginName(String loginName){
		SynchronizeLiveLogJob.loginName = loginName;
	}
	
	private static String password = "";
	@Value("#{application['pom.gensee.password']}")
	private void setPassword(String password){
		SynchronizeLiveLogJob.password = password;
	}
	
	private static String url = "";
	@Value("#{application['pom.gensee.webcast.log.url']}")
	private void setUrl(String url){
		SynchronizeLiveLogJob.url = url;
	}
	
	private static Long attendTime = 0L;
	@Value("#{application['pom.gensee.live.attend.time']}")
	private void setAttendTime(Long attendTime){
		SynchronizeLiveLogJob.attendTime = attendTime;
	}
	
	
	public void execute(Map<String,Object> params) {
		String schoolId = (String) params.get("schoolId");
		String startDate = (String) params.get("startDate");
		String countStr = (String) params.get("count");
		logger.info("SynchronizeLiveLogJob start：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		
		try{
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("schoolId", schoolId);
			if(null == startDate || startDate.equals("")){
				params1.put("startTime", DateFormatUtils.format(DateUtils.getDateBefore(new Date(), 1), "yyyy-MM-dd 00:00:00"));
				params1.put("endTime", DateFormatUtils.format(DateUtils.getDateBefore(new Date(), 1), "yyyy-MM-dd 23:59:59"));
			}else{
				Integer count = Integer.parseInt(countStr);
				if(DateUtils.matchDateString(startDate) && count > 0){
					params1.put("startTime", startDate + " 00:00:00");
					params1.put("endTime",DateUtils.format(DateUtils.getDateAfter(DateUtils.parse(startDate),count-1)) + " 23:59:59");
				}else{
					logger.error("SynchronizeVideoLogJob matchDate:{}","parameter date no match format");
					return;
				}
			}
			
			CourseClassplanLivesService courseClassplanLivesService = (CourseClassplanLivesService)SpringContextUtils.getBean("courseClassplanLivesService");
			LiveLogDetailService liveLogDetailService = (LiveLogDetailService)SpringContextUtils.getBean("liveLogDetailService");
			//LiveLogService liveLogService = (LiveLogService)SpringContextUtils.getBean("liveLogService");
			LogWatchService logWatchService = (LogWatchService)SpringContextUtils.getBean("logWatchService");
			
			//根据日期获取该日期的当天的所有排课明细
			List<Map<String, Object>> classplanLives = courseClassplanLivesService.queryByDate(params1);
			
			if(null != classplanLives && classplanLives.size() > 0){
				
				String classplanLiveId = null;
				String webcastId = null;
				String liveNum = null;
				Date liveStartTime = null;
				Date liveEndTime = null;
				String param = null;
				String logResult = null;
				Map<String,Object> logResultMap = null;
				
				List<Map<String,Object>> logList = null;
				LiveLogDetailEntity liveLogDetailEntity = null;
				Map<Long,Object> tmpUserMap = null;//临时缓存 (用户-观看时长) 键值对
				Long uid = null;
				Long joinTime = null;
				Long leaveTime = null;
				Long duration = null;
				Long tmpDuration = null;
				
				//LiveLogEntity liveLogEntity = null;
				LogWatchEntity logWatchEntity = null;
				Long watchDuration = null;
				
				for(Map<String,Object> classplanLive : classplanLives){
					
					try {
						classplanLiveId = (String) classplanLive.get("classplanLiveId");
						webcastId = (String) classplanLive.get("liveId");
						liveNum = (String) classplanLive.get("liveNum");
						liveStartTime = (Date) classplanLive.get("startTime");
						liveEndTime = (Date) classplanLive.get("endTime");
						param = "loginName="+loginName+"&password="+password+"&webcastId="+webcastId+"&startTime="+startTimeParam(liveStartTime)+"&endTime="+endTimeParam(liveEndTime);
						
						logResult = HttpUtils.sendGet(url, param);
						
						//请求展示互动直播日志接口,得到返回数据
						logResultMap = JSONUtil.jsonToMap(logResult);
						if(((String)logResultMap.get("code")).equals("0")){
							//从返回结果,提取日志数据
							logList = (List<Map<String, Object>>) logResultMap.get("list");
							tmpUserMap = new HashMap<Long, Object>();//缓存所有用户:uid - 时长:duration
							if(null != logList && logList.size() > 0){
								
								for(Map<String,Object> log : logList){
									
									try {
										/*tmpUid = (Integer)log.get("uid") - 1000000000;
										uid = tmpUid.longValue();*/
										uid = processUid(log.get("uid"));
										joinTime = (Long)log.get("joinTime");
										leaveTime = (Long)log.get("leaveTime");
										liveLogDetailEntity = new LiveLogDetailEntity();
										liveLogDetailEntity.setLiveId(webcastId);
										liveLogDetailEntity.setLiveNum(liveNum);
										liveLogDetailEntity.setUserId(uid);
										liveLogDetailEntity.setJoinTime(joinTime);
										liveLogDetailEntity.setLeaveTime(leaveTime);
										liveLogDetailEntity.setJoinType((Integer)log.get("joinType"));
										liveLogDetailEntity.setPlatformCode(1);
										liveLogDetailEntity.setCreateTime(new Date());
										liveLogDetailService.save(liveLogDetailEntity);//保存详细日志
										
										duration =  leaveTime - joinTime;
										tmpDuration = (Long) tmpUserMap.get(uid);//从缓存中获取同一用户的观看时长
										if(null != tmpDuration){
											if(tmpDuration < duration) tmpUserMap.put(uid, duration);//如果缓存中有该用户的观看时长,比较之,记录更长的时长
										}else{
											tmpUserMap.put(uid, duration);//缓存一个用户的观看时长
										}
									} catch (Exception e) {
										logger.error("SynchronizeVideoInfoJob save live_log_detail:{}",e.toString());
									}
								}
								
								for (Map.Entry<Long, Object> entry:tmpUserMap.entrySet()) {
									try {
										/*liveLogEntity = new LiveLogEntity();
										liveLogEntity.setLiveId(webcastId);
										liveLogEntity.setLiveNum(liveNum);
										liveLogEntity.setUserId(entry.getKey());
										liveLogEntity.setBusinessId(classplanLiveId);
										liveLogEntity.setBusinessType(0);
										watchDuration = (Long) entry.getValue();
										liveLogEntity.setWatchDuration(watchDuration);
										liveLogEntity.setLiveStartTime(liveStartTime);
										liveLogEntity.setLiveEndTime(liveEndTime);
										liveLogEntity.setLiveDuration(liveEndTime.getTime() - liveStartTime.getTime());
										liveLogEntity.setAttend30(watchDuration >= attendTime ? 1 : 0);
										liveLogEntity.setVersionCode(2);//自考2.0版本
										liveLogEntity.setSchoolId(schoolId);
										liveLogEntity.setCreateTime(new Date());
										liveLogService.save(liveLogEntity);//保存日志										
										*/
										logWatchEntity = new LogWatchEntity();
										logWatchEntity.setLvId(webcastId);
										logWatchEntity.setUserId(entry.getKey());
										logWatchEntity.setBusinessId(classplanLiveId);
										watchDuration = (Long) entry.getValue();
										logWatchEntity.setWatchDuration(watchDuration);
										logWatchEntity.setLiveStartTime(liveStartTime);
										logWatchEntity.setLiveEndTime(liveEndTime);
										logWatchEntity.setLvDuration(liveEndTime.getTime() - liveStartTime.getTime());
										logWatchEntity.setAttend30(watchDuration >= attendTime ? 1 : 0);
										logWatchEntity.setVersionCode(2);//自考2.0版本
										logWatchEntity.setSchoolId(schoolId);
										logWatchEntity.setCreateTime(new Date());
										logWatchEntity.setLogType(0);
										logWatchService.save(logWatchEntity);//保存日志	
										} catch (Exception e) {
										logger.error("SynchronizeVideoInfoJob save live_log:{}",e.toString());
									}
						        }
							}
						}
					} catch (Exception e) {
						logger.error("SynchronizeVideoInfoJob getLiveLog:{}",e.toString());
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("SynchronizeVideoInfoJob execute:{}",e.toString());
		}
	}

	/**
	 * 直播日志的开始时间参数
	 * @param date
	 * @return
	 */
	private String startTimeParam(Date date){
		Calendar now = Calendar.getInstance();
        now.setTime(date);  
        now.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY) - 1);
        String dateStr = DateUtils.format(now.getTime(), DateUtils.DATE_TIME_PATTERN);
        
        return dateStr.replaceAll(" ", "%20");
	}
	
	/**
	 * 直播日志的结束时间参数
	 * @param date
	 * @return
	 */
	private String endTimeParam(Date date){
		Calendar now = Calendar.getInstance();  
        now.setTime(date);  
        now.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY) + 1);
        String dateStr = DateUtils.format(now.getTime(), DateUtils.DATE_TIME_PATTERN);
        
        return dateStr.replaceAll(" ", "%20");
	}
	
	/**
	 * 提取uid
	 */
	private Long processUid(Object obj){
		Integer uidI = null;
		Long uidL = null;
		try{
			uidI = Integer.parseInt(obj.toString());
			if(uidI > 1000000000){
				uidI -= 1000000000;
			}
			uidL = uidI.longValue();
		}catch(Exception e){
			uidL = Long.parseLong(obj.toString()) - 10000000000L;
		}
		
		return uidL;
	}
}

package io.renren.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.renren.entity.CourseClassplanLivesEntity;
import io.renren.entity.LiveCallbackDetailEntity;
import io.renren.entity.LogGenseeWatchEntity;
import io.renren.service.CourseClassplanLivesService;
import io.renren.service.GenseeCallbackService;
import io.renren.service.LogGenseeWatchService;
import io.renren.utils.DateUtils;
import io.renren.utils.SpringContextUtils;
/**
 * 展视互动直播观看考勤统计
 * @class io.renren.task.CountGenseeLiveAttendJob.java
 * @Description:
 * @author shanyaofeng
 * @dete 2018年1月4日
 */
@Component("io.renren.task.CountGenseeLiveAttendJob")
public class CountGenseeLiveAttendJob{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private CourseClassplanLivesService _courseClassplanLivesService;
	private LogGenseeWatchService _logGenseeWatchService;
	private GenseeCallbackService _genseeCallbackService;
	
	
	
	public void execute(Map<String,Object> params) {
		String startDate = (String) params.get("startDate");
		String countStr = (String) params.get("count");
		String productIdStr = (String) params.get("productId");
		logger.info("CountGenseeLiveAttendJob start==> startDate:{},countStr:{},bid:{},time:{}" ,
				startDate,countStr,productIdStr,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()))
				);
		try{
			//产品线非空
			if(StringUtils.isNotBlank(productIdStr)){
				Long productId = Long.valueOf(productIdStr);
					
				Map<String, Object> params1 = new HashMap<String, Object>();
				if(StringUtils.isBlank(startDate)){
					startDate = DateFormatUtils.format(DateUtils.getDateBefore(new Date(System.currentTimeMillis()), 1), "yyyy-MM-dd");
					params1.put("startTime", startDate + " 00:00:00");
					params1.put("endTime", startDate + " 23:59:59");
				}else{
					Integer count = Integer.parseInt(countStr);
					if(DateUtils.matchDateString(startDate) && count > 0){
						params1.put("startTime", startDate + " 00:00:00");
						params1.put("endTime",DateUtils.format(DateUtils.getDateAfter(DateUtils.parse(startDate),count-1)) + " 23:59:59");
					}else{
						logger.error("CountGenseeLiveAttendJob matchDate:{}","parameter date no match format");
						return;
					}
				}
				CourseClassplanLivesService courseClassplanLivesService = getCourseClassplanLivesService();
				LogGenseeWatchService logGenseeWatchService = getLogGenseeWatchService();
				GenseeCallbackService genseeCallbackService = getGenseeCallbackService();
				//根据日期获取该日期的当天的所有排课明细
				List<Map<String, Object>> classplanLives = courseClassplanLivesService.queryByDate(params1);

				if(null != classplanLives && classplanLives.size() > 0){

					String classplanLiveId = null;//排课ID
					String webcastId = null;
					Date liveStartTime = null;//直播开始时间
					Date liveEndTime = null;//直播结束时间

					Map<Long,Object> tmpUserMap = null;//临时缓存 (用户-观看时长) 键值对
					Long duration = null;
					Long tmpDuration = null;

					LogGenseeWatchEntity logGenseeWatchEntity = null;
					Long watchDuration = null;

					//缓存当天所有直播间的最后一节课
					Map<String,Object> lastClassplanLivesMap = getLastClassplanLives(classplanLives);

					//当天所有排课-循环
					for(Map<String,Object> classplanLive : classplanLives){
						try {
							//获取结束直播时间
							Map<String,Object> endTimeParams = new HashMap<>();
							String liveId = (String)classplanLive.get("liveId");
							endTimeParams.put("liveId", liveId);
							endTimeParams.put("startTime", DateUtils.parse(startDate + " 00:00:00", DateUtils.DATE_TIME_PATTERN).getTime());
							endTimeParams.put("endTime", DateUtils.parse(startDate + " 23:59:59", DateUtils.DATE_TIME_PATTERN).getTime());
							LiveCallbackDetailEntity liveEnd = genseeCallbackService.queryClassLivesEndTime(endTimeParams);
							endTimeParams.put("classplanLiveId", classplanLive.get("classplanLiveId"));
                            endTimeParams.put("dr", 0);
                            //更新同一个直播间最后一节课结束时间
                            Map<String,Object> lastClassplanLive = (Map<String, Object>) lastClassplanLivesMap.get(liveId);
                            String lastClassplanLiveId = (String) lastClassplanLive.get("classplanLiveId");

                            classplanLiveId = (String) classplanLive.get("classplanLiveId");//排课ID
                            webcastId = (String) classplanLive.get("liveId");//直播间ID
                            liveStartTime = (Date) classplanLive.get("startTime");//直播开始时间

                            Map<String,Object> liveCallbackParams = new HashMap<>();
                            //如果当前循环的课次等于直播间最后的课次才修改结束时间
                            if(null != liveEnd && classplanLiveId.equals(lastClassplanLiveId)){
                                liveEndTime = new Date(liveEnd.getLeaveTime());//直播结束时间
								//更新直播结束时间
								CourseClassplanLivesEntity classLiveEntity = courseClassplanLivesService.queryByClassPlanLiveId(endTimeParams);
								classLiveEntity.setEndTime(new Date(liveEnd.getLeaveTime()));
								courseClassplanLivesService.update(classLiveEntity);

								liveCallbackParams.put("startTime", liveStartTime.getTime());
								liveCallbackParams.put("endTime", liveEndTime.getTime());
								liveCallbackParams.put("liveId", webcastId);
								genseeCallbackService.updateLiveCallbackLeaveTime(liveCallbackParams);
							}else {
                                liveEndTime = (Date)classplanLive.get("endTime");
                                liveCallbackParams.put("startTime", liveStartTime.getTime());
                                liveCallbackParams.put("endTime", liveEndTime.getTime());
                                liveCallbackParams.put("liveId", webcastId);
                            }
							//查询callback明细
							List<LiveCallbackDetailEntity> callbackList = genseeCallbackService.queryByLiveId(liveCallbackParams);
							tmpUserMap = new HashMap<Long, Object>();//缓存所有用户:uid - 时长:duration
							if(null != callbackList && callbackList.size() > 0){
								for(LiveCallbackDetailEntity callback : callbackList){
									duration = callback.getLeaveTime() - callback.getJoinTime();
									tmpDuration = (Long) tmpUserMap.get(callback.getUserId());//从缓存中获取同一用户的观看时长
									if(null != tmpDuration){
										tmpUserMap.put(callback.getUserId(), tmpDuration+duration);//时长求和
									}else{
										tmpUserMap.put(callback.getUserId(), duration);//缓存一个用户的观看时长
									}
								}
								for (Map.Entry<Long, Object> entry:tmpUserMap.entrySet()) {
									try {
										logGenseeWatchEntity = new LogGenseeWatchEntity();
										logGenseeWatchEntity.setVideoId(null);//录播ID
										logGenseeWatchEntity.setLiveId(webcastId);//直播课ID
										logGenseeWatchEntity.setUserId(entry.getKey());//用户ID
										logGenseeWatchEntity.setBusinessId(classplanLiveId);//排课ID
										watchDuration = (Long) entry.getValue();//观看时长
										logGenseeWatchEntity.setFullDur(liveEndTime.getTime() - liveStartTime.getTime());//应出勤时长(毫秒) 直播结束时间-直播开始时间
										logGenseeWatchEntity.setWatchDur(watchDuration);//直播和录播总时长(毫秒)
										logGenseeWatchEntity.setVideoDur(0L);//录播总时长(毫秒)
										logGenseeWatchEntity.setLiveDur(watchDuration);//直播总时长(毫秒)
										logGenseeWatchEntity.setAttendPer(null);//出勤率-service中计算
										logGenseeWatchEntity.setMId(null);
										logGenseeWatchEntity.setCreateTime(new Date(System.currentTimeMillis()));
										logGenseeWatchEntity.setProductId(productId);//产品id
										logGenseeWatchService.saveOrUpdate(logGenseeWatchEntity);//保存日志
									} catch (Exception e) {
										logger.error("save LiveAttend_log:{}",e.toString());
									}
						        }
							}
						} catch (Exception e) {
							logger.error("getLiveCallback:{}",e.toString());
						}
					}
				}
			}
						
		}catch(Exception e){
			e.printStackTrace();
			logger.error("execute:{}",e.toString());
		}
	}

	private Map<String,Object> getLastClassplanLives(List<Map<String, Object>> classplanLives){
		Map<String,Object> lastClassLivesMap = new HashMap<>();
		for(Map<String,Object> classplanLive : classplanLives){
			String liveId = (String)classplanLive.get("liveId");
			Map<String, Object> lastclassplanLive = (Map<String, Object>) lastClassLivesMap.get(liveId);
			if(null == lastclassplanLive){
				lastClassLivesMap.put(liveId, classplanLive);
			}else{
				Date lasttime = (Date)lastclassplanLive.get("endTime");
				Date time = (Date)classplanLive.get("endTime");
				if(null != lasttime && null != time){
					if(time.getTime() > lasttime.getTime()){
						lastClassLivesMap.put(liveId, classplanLive);
					}
				}
			}
		}
		return lastClassLivesMap;
	}

	//GET SERVICE
	private CourseClassplanLivesService getCourseClassplanLivesService(){
		if(null == _courseClassplanLivesService)_courseClassplanLivesService = (CourseClassplanLivesService)SpringContextUtils.getBean("courseClassplanLivesService");
		return _courseClassplanLivesService;
	}
	
	private LogGenseeWatchService getLogGenseeWatchService(){
		if(null == _logGenseeWatchService)_logGenseeWatchService = (LogGenseeWatchService)SpringContextUtils.getBean("logGenseeWatchService");
		return _logGenseeWatchService;
	}
	
	private GenseeCallbackService getGenseeCallbackService(){
		if(null == _genseeCallbackService)_genseeCallbackService = (GenseeCallbackService)SpringContextUtils.getBean("genseeCallbackService");
		return _genseeCallbackService;
	}

}

package io.renren.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.renren.entity.LogWatchEntity;
import io.renren.entity.VideoLogDetailEntity;
import io.renren.service.CourseClassplanLivesService;
import io.renren.service.LogWatchService;
import io.renren.service.VideoLogDetailService;
import io.renren.utils.DateUtils;
import io.renren.utils.JSONUtil;
import io.renren.utils.SpringContextUtils;
import io.renren.utils.http.HttpClientUtil4_3;

@Component("io.renren.task.SynchronizeVideoLogJob")
public class SynchronizeVideoLogJob {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static String loginName = "";
	@Value("#{application['pom.gensee.loginname']}")
	private void setLoginName(String loginName){
		SynchronizeVideoLogJob.loginName = loginName;
	}
	
	private static String password = "";
	@Value("#{application['pom.gensee.password']}")
	private void setPassword(String password){
		SynchronizeVideoLogJob.password = password;
	}
	
	private static String url = "";
	@Value("#{application['pom.gensee.webcast.vod.log']}")
	private void setUrl(String url){
		SynchronizeVideoLogJob.url = url;
	}
	
	private static Long attendTime = 0L;
	@Value("#{application['pom.gensee.video.attend.time']}")
	private void setAttendTime(Long attendTime){
		SynchronizeVideoLogJob.attendTime = attendTime;
	}
	
	public void execute(Map<String,Object> params){
		
		String schoolId = (String) params.get("schoolId");
		String startDate = (String) params.get("startDate");
		String countStr = (String) params.get("count");
		logger.info("SynchronizeLiveLogJob启动：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		
		try{
			Date dateParam = null;
			Integer count = 1;
			if(null == startDate || startDate.equals("")){
				dateParam = DateUtils.getDateBefore(new Date(), 1);//DateFormatUtils.format(DateUtils.getDateBefore(new Date(), 1), "yyyy-MM-dd 00:00:00");
			}else{
				count = Integer.parseInt(countStr);
				if(DateUtils.matchDateString(startDate)){
					dateParam = DateUtils.parse(startDate);
				}else{
					logger.error("SynchronizeVideoLogJob matchDate:{}","parameter date no match format");
					return;
				}
			}
			
			int totalPage=1;
			Map<String,Object> pageMap = null;
			for(int i=0;i<count;i++){
				totalPage = 1;
				for(int j=1;j<=totalPage;j++){
					String dateParamStr = DateUtils.format(DateUtils.getDateAfter(dateParam, i)) + "%2000:00:00";
					String param = "loginName="+loginName+"&password="+password+"&date="+dateParamStr+"&pageNo="+j;
					
					String logResult = HttpClientUtil4_3.get(url+"?"+param, new HashMap<String,String>());
					
					//请求展示互动直播日志接口,得到返回数据
					Map<String,Object> logResultMap = JSONUtil.jsonToMap(logResult);
					if(((String)logResultMap.get("code")).equals("0")){
						
						pageMap = (Map<String, Object>) logResultMap.get("page");
						totalPage = (int) pageMap.get("totalPages");
						
						VideoLogDetailService videoLogDetailService = (VideoLogDetailService)SpringContextUtils.getBean("videoLogDetailService");
						//VideoLogService videoLogServcie = (VideoLogService)SpringContextUtils.getBean("videoLogService");
						LogWatchService logWatchService = (LogWatchService)SpringContextUtils.getBean("logWatchService");
						CourseClassplanLivesService courseClassplanLivesService = (CourseClassplanLivesService)SpringContextUtils.getBean("courseClassplanLivesService"); 
						
						String nickName = null;
						Long _uid = null;
						String _videoId = null;
						Long _startTime = null;
						Long _leaveTime = null;
						VideoLogDetailEntity videoLogDetailEntity = null;
						
						Long duration = null;//观看录播时长
						Long tmpDuration = null;//缓存用户最长的观看时长 
						Long tmpStartTime = null;
						Long tmpEndTime = null;
						
						//从返回结果,提取日志数据
						List<Map<String,Object>> logList = (List<Map<String, Object>>) logResultMap.get("list");
						Map<String, Object> tmpLogList = new HashMap<String,Object>();//缓存所有用户:uid - 时长:duration
						Map<String, Object> tmpLog = null;
						
						//VideoLogEntity videoLogEntity = null;
						LogWatchEntity logWatchEntity = null;
						Map<String, Object> videoLog = null;
						
						String videoId = null;
						Long uid = null;
						Long watchDuration = null;
						Map<String, Object> logWatchParams = null;
						Map<String, Object> classplanLiveParams = null;
						Map<String, Object> classplanLive = null;
						
						if(null != logList && logList.size() > 0){
							for(Map<String,Object> log : logList){
								try{
									_uid = processUid(log.get("uid"));//测试阶段,数据有误,需提取
									//如果此行数据name为M_开头且uid大于2000000000,则此数据无效
									nickName = (String)log.get("name");
									if(nickName.startsWith("M_") || _uid > 100000000) continue;
									_videoId = (String)log.get("vodId");
									_startTime = (Long)log.get("startTime");
									_leaveTime = (Long)log.get("leaveTime");
									videoLogDetailEntity = new VideoLogDetailEntity();
									videoLogDetailEntity.setVideoId(_videoId);
									videoLogDetailEntity.setUserId(_uid);
									videoLogDetailEntity.setStartTime(_startTime);
									videoLogDetailEntity.setLeaveTime(_leaveTime);
									duration =  _leaveTime - _startTime;
									videoLogDetailEntity.setDuration(duration);
									videoLogDetailEntity.setDevice((Integer)log.get("device"));
									videoLogDetailEntity.setPlatformCode(1);//平台代号 gensee:1
									videoLogDetailEntity.setCreateTime(new Date());
									videoLogDetailService.save(videoLogDetailEntity);//保存详细日志
									
									tmpLog = (Map<String,Object>) tmpLogList.get(tmpKeyName(_videoId,_uid));//从缓存中获取同一用户的观看时长
									if(null != tmpLog){
										tmpStartTime = (Long)tmpLog.get("startTime");
										tmpEndTime = (Long)tmpLog.get("leaveTime");
										if(null != tmpStartTime && null != tmpEndTime){
											tmpDuration = tmpEndTime - tmpStartTime;
											if(tmpDuration < duration) tmpLogList.put(tmpKeyName(_videoId,_uid), log);//如果缓存中有该用户的观看时长,比较之,记录更长的时长
										}
									}else{
										tmpLogList.put(tmpKeyName(_videoId,_uid), log);//缓存一个用户的观看时长
									}
								}catch (Exception ex){
									logger.error("SynchronizeVideoLogJob save video_log_detail:{}",ex.toString());
								}
							}
							
							for (Map.Entry<String, Object> entry:tmpLogList.entrySet()) {
								try{
									
									videoLog = (Map<String, Object>) entry.getValue();
									uid = processUid(videoLog.get("uid"));
									videoId = (String)videoLog.get("vodId");
									
									logWatchParams = new HashMap<String, Object>();
									logWatchParams.put("lvId", videoId);
									logWatchParams.put("userId", uid);
									logWatchParams.put("schoolId", schoolId);
									//根据videoId,userId查询video_log
									logWatchEntity = logWatchService.queryByLvId(logWatchParams);
									watchDuration = (Long)videoLog.get("leaveTime") - (Long)videoLog.get("startTime");
									if(null != logWatchEntity){//如果同一videoid,uesrid有观看记录,比较时间
										
										if(logWatchEntity.getWatchDuration() < watchDuration){//如果新获取的观看时长更长,则更新之
											logWatchEntity.setWatchDuration(watchDuration);
											logWatchEntity.setAttend30(watchDuration >= attendTime ? 1 : 0);
											logWatchService.update(logWatchEntity);
										}
									}else{//如果没有记录,则新增一行
										//通过videoId 查询course_classplan_lives 获取startTime,endTime 得到直播时长
										classplanLiveParams = new HashMap<String, Object>();
										classplanLiveParams.put("backId", videoId);
										classplanLiveParams.put("backType", 1);
										classplanLiveParams.put("schoolId",schoolId);
										classplanLive = courseClassplanLivesService.queryByBackId(classplanLiveParams);
										
										if(null != classplanLive){
											logWatchEntity = new LogWatchEntity();
											logWatchEntity.setLvId(videoId);
											logWatchEntity.setUserId(uid);
											logWatchEntity.setWatchDuration(watchDuration);
											logWatchEntity.setBusinessId((String)classplanLive.get("classplanLiveId"));
											logWatchEntity.setLvDuration(((Date)classplanLive.get("endTime")).getTime() - ((Date)classplanLive.get("startTime")).getTime());
											logWatchEntity.setAttend30(watchDuration >= attendTime ? 1 : 0);
											logWatchEntity.setVersionCode(2);//版本号 自考2.0
											logWatchEntity.setSchoolId(schoolId);
											logWatchEntity.setCreateTime(new Date());
											logWatchEntity.setLogType(1);
											logWatchService.save(logWatchEntity);//保存日志
										}
									}
								}catch(Exception ex){
									logger.error("SynchronizeVideoLogJob save video_log:{}",ex.toString());
								}
					        }
						}
					}
				}
			}
		}catch(Exception ex){
			logger.error("SynchronizeVideoLogJob execute:{}",ex.toString());
		}
	}

	
	private String tmpKeyName(String videoId, Long uid){
		return "videoId:"+videoId+"&uid:"+uid;
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

package io.renren.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.renren.entity.LiveLogDetailEntity;
import io.renren.entity.LogGenseeWatchEntity;
import io.renren.entity.SysProductEntity;
import io.renren.service.CourseClassplanLivesService;
import io.renren.service.CourseUserplanDetailService;
import io.renren.service.LiveLogDetailService;
import io.renren.service.LogGenseeWatchService;
import io.renren.service.SysProductService;
import io.renren.service.UsersService;
import io.renren.utils.DateUtils;
import io.renren.utils.HttpUtils;
import io.renren.utils.JSONUtil;
import io.renren.utils.SpringContextUtils;
/**
 * 展视互动直播观看日志同步
 * 算法:观看时长求和
 * @class io.renren.task.SynchronizeLiveLogJob2.java
 * @Description:
 * @author shihongjie
 * @dete 2017年8月21日
 */
@Component("io.renren.task.SynchronizeGenseeLiveLogJob")
public class SynchronizeGenseeLiveLogJob{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
//	private static String loginName = "";
//	@Value("#{application['pom.gensee.loginname']}")
//	private void setLoginName(String loginName){
//		SynchronizeGenseeLiveLogJob.loginName = loginName;
//	}
//	
//	private static String password = "";
//	@Value("#{application['pom.gensee.password']}")
//	private void setPassword(String password){
//		SynchronizeGenseeLiveLogJob.password = password;
//	}
//	
//	private static String url = "";
//	@Value("#{application['pom.gensee.webcast.log.url']}")
//	private void setUrl(String url){
//		SynchronizeGenseeLiveLogJob.url = url;
//	}
	
//	private static Long attendTime = 0L;
//	@Value("#{application['pom.gensee.live.attend.time']}")
//	private void setAttendTime(Long attendTime){
//		SynchronizeGenseeLiveLogJob.attendTime = attendTime;
//	}
	
	private static final String GENSEELIVE_CODE = "code";
	private static final String GENSEELIVE_CODE_SUCCESS = "0";
	
	private CourseClassplanLivesService _courseClassplanLivesService;
	private LiveLogDetailService _liveLogDetailService;
	private LogGenseeWatchService _logGenseeWatchService;
	private SysProductService _sysProductService;//产品线
	private CourseUserplanDetailService _courseUserplanDetailService;
	
	
	
	public void execute(Map<String,Object> params) {
//		String schoolId = (String) params.get("schoolId");
		String startDate = (String) params.get("startDate");
		String countStr = (String) params.get("count");
		String productIdStr = (String) params.get("productId");
		logger.info("SynchronizeGenseeLiveLogJob start==> startDate:{},countStr:{},bid:{},time:{}" ,
				startDate,countStr,productIdStr,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()))
				);
		try{
			//产品线非空
			if(StringUtils.isNotBlank(productIdStr)){
				Long productId = Long.valueOf(productIdStr);
				Map<String , Object> queryProductMap = new HashMap<>();
				queryProductMap.put("productId", productId);
				SysProductEntity sysProductEntity = getSysProductService().queryObject(queryProductMap);
				if(null != sysProductEntity){
					//暂时互动参数
					String genseeLoginname = sysProductEntity.getGenseeLoginname();
					String genseePassword = sysProductEntity.getGenseePassword();
					String genseeWebcastLogUrl = sysProductEntity.getGenseeWebcastLogUrl();
					if(StringUtils.isNotBlank(genseeLoginname) && StringUtils.isNotBlank(genseePassword) && StringUtils.isNotBlank(genseeWebcastLogUrl)){
						
						Map<String, Object> params1 = new HashMap<String, Object>();
						if(StringUtils.isBlank(startDate)){
							params1.put("startTime", DateFormatUtils.format(DateUtils.getDateBefore(new Date(System.currentTimeMillis()), 1), "yyyy-MM-dd 00:00:00"));
							params1.put("endTime", DateFormatUtils.format(DateUtils.getDateBefore(new Date(System.currentTimeMillis()), 1), "yyyy-MM-dd 23:59:59"));
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
						CourseClassplanLivesService courseClassplanLivesService = getCourseClassplanLivesService();
						LiveLogDetailService liveLogDetailService = getLiveLogDetailService();
						LogGenseeWatchService logGenseeWatchService = getLogGenseeWatchService();
						
						//根据日期获取该日期的当天的所有排课明细
						List<Map<String, Object>> classplanLives = courseClassplanLivesService.queryByDate(params1);
						
						if(null != classplanLives && classplanLives.size() > 0){
							
							String classplanLiveId = null;//排课ID
							String webcastId = null;
							String liveNum = null;
							Date liveStartTime = null;//直播开始时间
							Date liveEndTime = null;//直播结束时间
							String param = null;
							String logResult = null;
							Map<String,Object> logResultMap = null;//直播缓存数据(userId:观看时长) , 求和
							
							List<Map<String,Object>> logList = null;
							LiveLogDetailEntity liveLogDetailEntity = null;
							Map<Long,Object> tmpUserMap = null;//临时缓存 (用户-观看时长) 键值对
							Long uid = null;
							Long joinTime = null;//观看时间
							Long leaveTime = null;//离开时间
							Long duration = null;
							Long tmpDuration = null;
							
							//LiveLogEntity liveLogEntity = null;
//								LogWatchEntity logWatchEntity = null;
							LogGenseeWatchEntity logGenseeWatchEntity = null;
							Long watchDuration = null;
							//当天所有排课-循环
							for(Map<String,Object> classplanLive : classplanLives){
								
								try {
									classplanLiveId = (String) classplanLive.get("classplanLiveId");//排课ID
									webcastId = (String) classplanLive.get("liveId");//直播间ID
									liveNum = (String) classplanLive.get("liveNum");//
									liveStartTime = (Date) classplanLive.get("startTime");//直播开始时间
									liveEndTime = (Date) classplanLive.get("endTime");//直播结束时间
									//参数
									param = "loginName="+genseeLoginname+"&password="+genseePassword+"&webcastId="+webcastId+"&startTime="+startTimeParam(liveStartTime)+"&endTime="+endTimeParam(liveEndTime);
									//请求展视互动获取直播记录
									logResult = HttpUtils.sendGet(genseeWebcastLogUrl, param);
									
									//请求展示互动直播日志接口,得到返回数据
									logResultMap = JSONUtil.jsonToMap(logResult);
									if(GENSEELIVE_CODE_SUCCESS.equals(((String)logResultMap.get(GENSEELIVE_CODE)))){
										//从返回结果,提取日志数据
										logList = (List<Map<String, Object>>) logResultMap.get("list");
										tmpUserMap = new HashMap<Long, Object>();//缓存所有用户:uid - 时长:duration
										if(null != logList && logList.size() > 0){
											
											for(Map<String,Object> log : logList){
												
												try {
													
													uid = processUid(log.get("uid"),log.get("nickname"),classplanLiveId);//用户ID
													joinTime = (Long)log.get("joinTime");//加入时间
													leaveTime = (Long)log.get("leaveTime");//离开时间
													//判断明细是否重复-重复跳过
													if(liveLogDetailService.selectDetailCount(webcastId, liveNum, uid, joinTime, leaveTime))continue;
													liveLogDetailEntity = new LiveLogDetailEntity();//直播记录明细对象
													liveLogDetailEntity.setLiveId(webcastId);//直播间信息
													liveLogDetailEntity.setLiveNum(liveNum);
													liveLogDetailEntity.setUserId(uid);//用户ID
													liveLogDetailEntity.setJoinTime(joinTime);//加入时间
													liveLogDetailEntity.setLeaveTime(leaveTime);//离开时间
													liveLogDetailEntity.setJoinType((Integer)log.get("joinType"));//加入时间
													liveLogDetailEntity.setPlatformCode(1);//平台
													liveLogDetailEntity.setProductId(productId);//平台ID
													liveLogDetailEntity.setCreateTime(new Date(System.currentTimeMillis()));//创建时间
													liveLogDetailService.save(liveLogDetailEntity);//保存详细日志
													//时长
													duration =  leaveTime - joinTime;
													tmpDuration = (Long) tmpUserMap.get(uid);//从缓存中获取同一用户的观看时长
													if(null != tmpDuration){
														tmpUserMap.put(uid, tmpDuration+duration);//时长求和
													}else{
														tmpUserMap.put(uid, duration);//缓存一个用户的观看时长
													}
												} catch (Exception e) {
													logger.error("SynchronizeGenseeLiveLogJob save live_log_detail:{}",e.toString());
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
														logGenseeWatchService.saveOrUpdateFromLog(logGenseeWatchEntity);//保存日志
													} catch (Exception e) {
														logger.error("save live_log:{}",e.toString());
												}
									        }
										}
									}
								} catch (Exception e) {
									logger.error("getLiveLog:{}",e.toString());
								}
							}
						}
						
					}
				}
			}
						
		}catch(Exception e){
			e.printStackTrace();
			logger.error("execute:{}",e.toString());
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
	private Long processUid(Object obj, Object nickname_obj, String classplanLiveId){
		Integer uidI = null;
		Long uidL = null;
		try{
			uidI = Integer.parseInt(obj.toString());
			
			if(uidI > 1000000000){
				uidI -= 1000000000;
			}
			uidL = uidI.longValue();
		}catch(Exception e){
			uidL = Long.parseLong(obj.toString());
			//如果uid以99开头,即为展示互动给到的uid,即为缺失的数据,通过昵称获得uid
			if(uidL.toString().indexOf("99") == 0){
				//uidI = getCourseUserplanDetailService
				List<Long> userIdList = getCourseUserplanDetailService().getUserIdByClassplanLiveId(classplanLiveId, (String)nickname_obj);
				if(null != userIdList && userIdList.size() > 0){
					uidL = userIdList.get(0);
					return uidL;
				}
			}
			
			uidL = uidL - 10000000000L;
		}
		
		return uidL;
	}
	
	//GET SERVICE
	private CourseClassplanLivesService getCourseClassplanLivesService(){
		if(null == _courseClassplanLivesService)_courseClassplanLivesService = (CourseClassplanLivesService)SpringContextUtils.getBean("courseClassplanLivesService");
		return _courseClassplanLivesService;
	}
	
	private LiveLogDetailService getLiveLogDetailService(){
		if(null == _liveLogDetailService)_liveLogDetailService = (LiveLogDetailService)SpringContextUtils.getBean("liveLogDetailService");
		return _liveLogDetailService;
	}
	
	private LogGenseeWatchService getLogGenseeWatchService(){
		if(null == _logGenseeWatchService)_logGenseeWatchService = (LogGenseeWatchService)SpringContextUtils.getBean("logGenseeWatchService");
		return _logGenseeWatchService;
	}
	private SysProductService getSysProductService(){
		if(null == _sysProductService)_sysProductService = (SysProductService)SpringContextUtils.getBean("sysProductService");
		return _sysProductService;
	}
	private CourseUserplanDetailService getCourseUserplanDetailService(){
		if(null == _courseUserplanDetailService)_courseUserplanDetailService = (CourseUserplanDetailService)SpringContextUtils.getBean("courseUserplanDetailService");
		return _courseUserplanDetailService;
	}
}

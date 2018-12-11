package io.renren.task;

import io.renren.entity.CourseClassplanLivesEntity;
import io.renren.entity.LiveCallbackDetailEntity;
import io.renren.entity.LogGenseeWatchEntity;
import io.renren.pojo.log.ReplayCallbackForLogGenseeWatchPOJO;
import io.renren.service.CourseClassplanLivesService;
import io.renren.service.GenseeCallbackService;
import io.renren.service.LogGenseeWatchService;
import io.renren.service.SysProductService;
import io.renren.utils.DateUtils;
import io.renren.utils.SpringContextUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 展视互动直播回放观看考勤统计
 * @class io.renren.task.CountGenseeLiveAttendJob.java
 * @Description:
 * @author shanyaofeng
 * @dete 2018年1月4日
 */
@Component("io.renren.task.CountGenseeReplayAttendJob")
public class CountGenseeReplayAttendJob {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private CourseClassplanLivesService _courseClassplanLivesService;
	private LogGenseeWatchService _logGenseeWatchService;
	private GenseeCallbackService _genseeCallbackService;
	private SysProductService _sysProductService;
	
	
	
	public void execute(Map<String,Object> params) {
        LogGenseeWatchService logGenseeWatchService = getLogGenseeWatchService();
        GenseeCallbackService genseeCallbackService = getGenseeCallbackService();
        SysProductService sysProductService = getSysProductService();

        String startDate = (String) params.get("startDate");
        String countStr = (String) params.get("count");
        String productIdStr = (String) params.get("productId");
        logger.info("CountGenseeLiveAttendJob start==> startDate:{},countStr:{},bid:{},time:{}",
                startDate, countStr, productIdStr, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
        try {
            //产品线非空
            if (StringUtils.isNotBlank(productIdStr)) {
                Long productId = Long.valueOf(productIdStr);
                //根据productId查询系数
                Float coefficient = sysProductService.queryCoefficient(productId);
                Map<String, Object> map = new HashMap<String, Object>();
                if (StringUtils.isBlank(startDate)) {
                    startDate = DateFormatUtils.format(DateUtils.getDateBefore(new Date(System.currentTimeMillis()), 1), "yyyy-MM-dd");
                    map.put("startTime", DateUtils.parse(startDate + " 00:00:00","yyyy-MM-dd HH:mm:ss").getTime());
                    map.put("endTime", DateUtils.parse(startDate + " 23:59:59","yyyy-MM-dd HH:mm:ss").getTime());
                } else {
                    Integer count = Integer.parseInt(countStr);
                    if (DateUtils.matchDateString(startDate) && count > 0) {
                        map.put("startTime", DateUtils.parse(startDate + " 00:00:00","yyyy-MM-dd HH:mm:ss").getTime());
                        map.put("endTime", DateUtils.parse(DateUtils.format(DateUtils.getDateAfter(DateUtils.parse(startDate), count - 1)) + " 23:59:59","yyyy-MM-dd HH:mm:ss").getTime());
                    } else {
                        logger.error("CountGenseeLiveAttendJob matchDate:{}", "parameter date no match format");
                        return;
                    }
                }
                //缓存所有用户:uid - 时长:duration
                Map<String, LogGenseeWatchEntity> tmpUserMap = new HashMap<>();
                Long duration = null;
                Long fullduration = null;
                LogGenseeWatchEntity logGenseeWatchEntity = null;

                //根据日期获取该日期的当天的对应产品线的回放明细
                map.put("productId",productId);
                List<ReplayCallbackForLogGenseeWatchPOJO> callbackList = genseeCallbackService.queryReplayCallback(map);
                //缓存所有用户:uid - 时长:duration
                tmpUserMap = new HashMap<String, LogGenseeWatchEntity>();
                if (null != callbackList && callbackList.size() > 0) {
                    for (ReplayCallbackForLogGenseeWatchPOJO callback : callbackList) {
                        if (callback.getJoinTime() != null && callback.getLeaveTime() != null){
                            duration = (long) ((callback.getLeaveTime() - callback.getJoinTime()) * coefficient);
                        }
                        if (callback.getEndTime() != null && callback.getStartTime() != null){
                            fullduration = callback.getEndTime().getTime() - callback.getStartTime().getTime();
                        }
                        logGenseeWatchEntity = tmpUserMap.get(tmpKeyName(callback.getClassPlanLiveId(), callback.getUserId()));//从缓存中获取同一用户的观看时长
                        if (null != logGenseeWatchEntity) {
                            logGenseeWatchEntity.setWatchDur(logGenseeWatchEntity.getWatchDur() + duration);//直播和录播总时长(毫秒)
                            logGenseeWatchEntity.setVideoDur(logGenseeWatchEntity.getVideoDur() + duration);//录播总时长(毫秒)
                        } else {
                            logGenseeWatchEntity = new LogGenseeWatchEntity();//观看记录
                            logGenseeWatchEntity.setVideoId(callback.getVideoId());//录播ID
                            logGenseeWatchEntity.setLiveId(null);//直播课ID
                            logGenseeWatchEntity.setUserId(callback.getUserId());//用户ID
                            logGenseeWatchEntity.setBusinessId(callback.getClassPlanLiveId());//TODO 排课ID
                            logGenseeWatchEntity.setFullDur(fullduration);// TODO 应出勤时长(毫秒) 直播结束时间-直播开始时间
                            logGenseeWatchEntity.setWatchDur(duration);// 直播和录播总时长(毫秒)
                            logGenseeWatchEntity.setVideoDur(duration);//录播总时长(毫秒)
                            logGenseeWatchEntity.setLiveDur(0L);//直播总时长(毫秒)
                            logGenseeWatchEntity.setAttendPer(null);//出勤率-service中计算
                            logGenseeWatchEntity.setMId(null);
//                          logGenseeWatchEntity.setCreateTime(new Date(System.currentTimeMillis()));
//							logGenseeWatchService.saveOrUpdate(logGenseeWatchEntity);//保存日志
                            logGenseeWatchEntity.setProductId(productId);
                            tmpUserMap.put(tmpKeyName(callback.getClassPlanLiveId(), callback.getUserId()), logGenseeWatchEntity);//缓存一个用户的观看时长
                        }
                    }
                    for (Map.Entry<String, LogGenseeWatchEntity> entry : tmpUserMap.entrySet()) {
                        try {
                            LogGenseeWatchEntity entity = entry.getValue();
                            entity.setVideoDur(entity.getVideoDur());
                            logGenseeWatchService.saveOrUpdate(entity);//保存日志
                        } catch (Exception e) {
                            logger.error("save LiveAttend_log:{}", e.toString());
                        }
                    }
                }
            }
        } catch(Exception ex){
            logger.error("getLiveCallback:{}", ex.toString());
        }
    }


    private String tmpKeyName(String videoId, Long uid){
        return "videoId:"+videoId+"&uid:"+uid;
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
	
	private SysProductService getSysProductService(){
		if(null == _sysProductService)_sysProductService = (SysProductService)SpringContextUtils.getBean("sysProductService");
		return _sysProductService;
	}
}

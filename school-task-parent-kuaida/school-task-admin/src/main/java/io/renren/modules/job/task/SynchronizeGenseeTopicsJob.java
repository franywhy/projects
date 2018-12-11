package io.renren.modules.job.task;

import io.renren.common.utils.SpringContextUtils;
import io.renren.modules.job.entity.SysProductEntity;
import io.renren.modules.job.service.*;
import io.renren.modules.job.utils.DateUtils;
import io.renren.modules.job.utils.HttpUtils;
import io.renren.modules.job.utils.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 展视互动直播问答数据同步
 * @author hq
 */
@Component("io.renren.modules.job.task.SynchronizeGenseeTopicsJob")
public class SynchronizeGenseeTopicsJob {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final String GENSEELIVE_CODE = "code";
	private static final String GENSEELIVE_CODE_SUCCESS = "0";
	
	private CourseClassplanLivesService _courseClassplanLivesService;
	private LiveLogDetailService _liveLogDetailService;
	private LogGenseeWatchService _logGenseeWatchService;
	//产品线
	private SysProductService _sysProductService;

	private TopicsService topicsService;
	
	public void execute(Map<String,Object> params) {
		String startDate = (String) params.get("startDate");
		String countStr = (String) params.get("count");
		String productIdStr = (String) params.get("productId");
		String productName = (String) params.get("productName");

		logger.info("SynchronizeGenseeLiveLogJob start==> startDate:{},countStr:{},bid:{},time:{}" ,
				startDate,countStr,productIdStr,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));

		try{
			//产品线非空
			if(StringUtils.isNotBlank(productIdStr)){
				Long productId = Long.valueOf(productIdStr);
				Map<String,Object> queryProductMap = new HashMap<>();
				queryProductMap.put("productId", productId);
				SysProductEntity sysProductEntity = getSysProductService().queryObject(queryProductMap);
				if(null != sysProductEntity){
					//展示互动参数
					String genseeLoginname = sysProductEntity.getGenseeLoginname();
					String genseePassword = sysProductEntity.getGenseePassword();
					String genseeWebcastTopicsUrl = sysProductEntity.getGenseeWebcastTopicsUrl();
					if(StringUtils.isNotBlank(genseeLoginname) && StringUtils.isNotBlank(genseePassword) && StringUtils.isNotBlank(genseeWebcastTopicsUrl)){
						
						Map<String,Object> params1 = new HashMap<>();
						if(StringUtils.isBlank(startDate)){
							params1.put("startTime", DateFormatUtils.format(DateUtils.getDateBefore(new Date(System.currentTimeMillis()), 1), "yyyy-MM-dd 00:00:00"));
							params1.put("endTime", DateFormatUtils.format(DateUtils.getDateBefore(new Date(System.currentTimeMillis()), 1), "yyyy-MM-dd 23:59:59"));
						}else{
							Integer count = Integer.parseInt(countStr);
							if(DateUtils.matchDateString(startDate) && count > 0){
								params1.put("startTime", startDate + " 00:00:00");
								params1.put("endTime", DateUtils.format(DateUtils.getDateAfter(DateUtils.parse(startDate),count-1)) + " 23:59:59");
							}else{
								logger.error("SynchronizeGenseeTopicsJob matchDate:{}","parameter date no match format");
								return;
							}
						}

						CourseClassplanLivesService courseClassplanLivesService = getCourseClassplanLivesService();

						//根据日期获取该日期的当天的所有排课明细
						List<Map<String, Object>> classplanLives = courseClassplanLivesService.queryByDate(params1);
						
						if(null != classplanLives && classplanLives.size() > 0){
							
							String classplanLiveId = null;//排课ID
							String webcastId = null;
							Date liveStartTime = null;//直播开始时间
							Date liveEndTime = null;//直播结束时间
							String param = null;
							String topicsResult = null;
							Map<String,Object> topicsResultMap = null;//直播缓存数据(userId:观看时长) , 求和
							List<Map<String,Object>> topicsList = null;

							//当天所有排课-循环
							for(Map<String,Object> classplanLive : classplanLives){
								try {
									classplanLiveId = (String) classplanLive.get("classplanLiveId");//排课ID
									webcastId = (String) classplanLive.get("liveId");//直播间ID
									liveStartTime = (Date) classplanLive.get("startTime");//直播开始时间
									liveEndTime = (Date) classplanLive.get("endTime");//直播结束时间
									//参数
									param = "loginName="+genseeLoginname+"&password="+genseePassword+"&webcastId="+webcastId+"&startTime="+startTimeParam(liveStartTime)+"&endTime="+endTimeParam(liveEndTime);
									//请求展视互动获取直播提问数据记录
									topicsResult = HttpUtils.sendGet(genseeWebcastTopicsUrl, param);
									
									//请求展示互动直播提问数据接口,得到返回数据
									topicsResultMap = JSONUtil.jsonToMap(topicsResult);
									if(GENSEELIVE_CODE_SUCCESS.equals(((String)topicsResultMap.get(GENSEELIVE_CODE)))){
										//从返回结果,提取提问数据
										topicsList = (List<Map<String,Object>>) topicsResultMap.get("qaList");
										if(null != topicsList && topicsList.size() > 0){
                                            logger.error("SynchronizeGenseeLiveLogJob  execute  classplanLiveId={},genseeTopicsSize={}",classplanLiveId,topicsList.size());
											for(Map<String,Object> topics : topicsList){
												if(null != topics.get("answerBy")) {
													getTopicsService().save(topics, classplanLiveId, productName);
												}
											}
										}
									}
								} catch (Exception e) {
									logger.error("getLiveTopics:{}",e.toString());
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

	//GET SERVICE
	private CourseClassplanLivesService getCourseClassplanLivesService(){
		if(null == _courseClassplanLivesService)_courseClassplanLivesService = (CourseClassplanLivesService)SpringContextUtils.getBean("courseClassplanLivesService");
		return _courseClassplanLivesService;
	}
	private LiveLogDetailService getLiveLogDetailService(){
		if(null == _liveLogDetailService)_liveLogDetailService = (LiveLogDetailService) SpringContextUtils.getBean("liveLogDetailService");
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
	private TopicsService getTopicsService(){
		if(null == topicsService) topicsService = (TopicsService) SpringContextUtils.getBean("topicsService");
		return topicsService;
	}
}

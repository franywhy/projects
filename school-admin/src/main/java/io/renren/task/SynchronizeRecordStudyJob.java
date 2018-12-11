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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.druid.support.json.JSONUtils;

import io.renren.entity.LiveLogDetailEntity;
import io.renren.entity.LogWatchEntity;
import io.renren.service.CourseClassplanLivesService;
import io.renren.service.LiveLogDetailService;
import io.renren.service.LogWatchService;
import io.renren.service.RecordStudyService;
import io.renren.utils.DateUtils;
import io.renren.utils.HttpUtils;
import io.renren.utils.JSONUtil;
import io.renren.utils.SpringContextUtils;

@Component("io.renren.task.SynchronizeRecordStudyJob")
public class SynchronizeRecordStudyJob{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	 
	private RecordStudyService recordStudyService;
	 
	/**
	 * 参数 startDate:开始时间 count:运算天数;initStartDate:初始化日期(开班时间)initEndDate:初始化日期(开班时间)
	 *@param params
	 * @author lintf
	 * 2018年8月29日
	 */
	public void execute(Map<String,Object> params) { 
		try {
			
			String startDate = (String) params.get("startDate");
			String countStr = (String) params.get("count");
			String initStartDate=(String) params.get("initStartDate");
			String initEndDate=(String) params.get("initEndDate");
			
		recordStudyService =  (RecordStudyService)SpringContextUtils.getBean("recordStudyService");
 
		
		Map<String, Object> params1 = new HashMap<String, Object>();
		
		if(StringUtils.isBlank(initStartDate)){ //只有初始化日期为空的才进入每天定时任务
		
				if (StringUtils.isBlank(startDate)) {
					params1.put("startTime", DateFormatUtils.format(
							DateUtils.getDateBefore(new Date(System.currentTimeMillis()), 1), "yyyy-MM-dd 00:00:00"));
					params1.put("endTime", DateFormatUtils.format(
							 new Date(System.currentTimeMillis()) , "yyyy-MM-dd 23:59:59"));
					long runTime = new Date().getTime();
					recordStudyService.synchronizeRecordStudy(params1);
					logger.info("SynchronizeRecordStudyJob run successfully ,params is {},tasktime   {} /s.",
							params1,(new Date().getTime() - runTime) / 1000);
					
				} else {
					Integer count = Integer.parseInt(countStr);
					if (DateUtils.matchDateString(startDate) && count > 0) {
						params1.put("startTime", startDate + " 00:00:00");
						params1.put("endTime",
								DateUtils.format(DateUtils.getDateAfter(DateUtils.parse(startDate), count - 1))
										+ " 23:59:59");
						long runTime = new Date().getTime();
						recordStudyService.synchronizeRecordStudy(params1);
						logger.info("SynchronizeRecordStudyJob run successfully ,params is {},tasktime   {} /s.",
								params1,	(new Date().getTime() - runTime) / 1000);

					} else {
						logger.error("SynchronizeRecordStudyJob matchDate:{}", "parameter date no match format");
						return;
					}
				}
		}else {
			
			if(DateUtils.matchDateString(initStartDate)&&DateUtils.matchDateString(initEndDate)  ){
					Date beginDate = DateUtils.parse(DateFormatUtils
							.format(DateUtils.getDateBefore(DateUtils.parse(initStartDate), 1), "yyyy-MM-dd 00:00:00"),
							"yyyy-MM-dd HH:mm:ss");
					Date stopDate = DateUtils.parse(DateFormatUtils
							.format(DateUtils.getDateBefore(DateUtils.parse(initEndDate), 1), "yyyy-MM-dd 23:59:59"),
							"yyyy-MM-dd HH:mm:ss");
				 
					this.RecordStudyRun(beginDate, stopDate, 5);
				
			}else{
				logger.error("SynchronizeRecordStudyJob intit has error, matchDate:{}{},parameter date no match format",initStartDate,initEndDate);
				return;
			}
			
		}
		
		
		
		/*
		String firstDate="2014-01-01";
		Date now_date= DateUtils.parse(firstDate);
       for (int i=0;i<60;i++) {
    	   
    	   long onTime=new Date().getTime();
    		params1.put("startTime",DateFormatUtils.format(DateUtils.getDateBefore(now_date, 1), "yyyy-MM-dd 00:00:00"));
			params1.put("endTime",DateUtils.format(DateUtils.getDateAfter(now_date, 30)) + " 23:59:59");
			
			recordStudyService.synchronizeRecordStudy(params1);
			
			logger.info("SynchronizeRecordStudyJob run successfully,param is {} ,tasktime   {} /s.",params1,(new Date().getTime()-onTime)/1000);
			now_date=DateUtils.getDateAfter(now_date, 30);
			
			if (now_date.after(new Date())) {
				System.out.println("大于今天跳出");
			}
			
			
		}
		
		*/
	 	} catch (Exception es) {
			logger.error("SynchronizeRecordStudyJob run has Errors,the params is {},recordStudyService is {},the errorMessage is {}",
					params,recordStudyService,
					es);
		}
	}

	
	
	/**
	 * 初始化生成学员学习信息
	 *@param startDate 开课日期 开始
	 *@param endDate   开课日期 结束
	 *@param dayCount  循环天数
	 * @author lintf
	 * 2018年8月29日
	 */
	
	 private void   RecordStudyRun(Date startDate,Date endDate,int dayCount) {
		 Date stopTime=DateUtils.addMinute(60, new Date());
		 Date runTime=startDate;//当前运算时间 初始值为开始时间
		 Date nextTime=DateUtils.getDateAfter(runTime, dayCount);//截止时间
		 boolean isOverTime=false;//是否过期
		 try {
		while(	!isOverTime) {//不过期的则进行运算
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("initStartTime",DateUtils.format(runTime)+ " 00:00:00");
			params1.put("initEndTime",DateUtils.format(nextTime) + " 23:59:59");
			long nowTime = new Date().getTime();
        	recordStudyService.synchronizeRecordStudy(params1);
        	logger.info( "SynchronizeRecordStudyJob init by {},task time {} s.",params1,(new Date().getTime() - nowTime) / 1000 );
		 
        	if (nextTime==endDate|| new Date().after(stopTime)) {
				isOverTime=true;
			}
			runTime=DateUtils.getDateAfter(nextTime,1);
			nextTime=DateUtils.getDateAfter(runTime,dayCount);
			
			if (nextTime.after( endDate) ) {
				
				nextTime=endDate;
			}
			
			
		 }
	 }catch(Exception  es ) {
			logger.error("SynchronizeRecordStudyJob init has error,runTime is {},nextTime is {},errorMessage:{}",
					DateUtils.format(runTime),DateUtils.format(nextTime),es);
			es.printStackTrace();
		}
	 }
}

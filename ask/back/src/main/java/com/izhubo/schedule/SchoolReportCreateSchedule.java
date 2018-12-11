package com.izhubo.schedule;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.izhubo.admin.answer.SchoolUserReportController;
import com.izhubo.rest.common.util.http.HttpClientUtil;



public class SchoolReportCreateSchedule extends QuartzJobBean{

	@Value("#{application['admin.domain']}")
	private String admin_domain = "http://adminapi.kjcity.com/";
	
	
	private String reportdomain_school = "schooluserreport/readdatalist.json?type=1";
	
	private String reportdomain_provine = "schooluserreport/readdatalist.json?type=2";
	
	private String reportdomain_area = "schooluserreport/readdatalist.json?type=3";
	
	private String ca_teacher_sort_daily = "teacherorderreport/teacherOrderDayTime.json?channle=0";
	//红包每日统计
	private static final String reportdomain_topicbunus = "topicbunusreport/topicBunusReportDay.json";
	


	private static Logger logger = LoggerFactory.getLogger(SchoolReportCreateSchedule.class);
	  /*业务实现*/
   public void work() {
	   logger.info("执行调度任务："+new Date());
       try {
       	
    	   //schoolUserReportController.GetSchoolReportResult();
		    
    	  // http://ttadmin.app1101168695.twsapp.com/schooluserreport/readdatalist.json
    	   
    	  // HttpClientUtil.get(admin_domain+reportdomain_school, null);
    	  // HttpClientUtil.get(admin_domain+reportdomain_provine, null);
    	  // HttpClientUtil.get(admin_domain+reportdomain_area, null);
    	  // HttpClientUtil.get(admin_domain+reportdomain_area, null);
    	   HttpClientUtil.get(admin_domain+ca_teacher_sort_daily, null);
    	   HttpClientUtil.get(admin_domain+reportdomain_topicbunus, null);
    	   
		  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			  logger.info("执行保报错"+new Date());
			  logger.info("执行保报错"+e.toString());
			e.printStackTrace();
		}
   }

   @Override
   protected void executeInternal(JobExecutionContext arg0)
           throws JobExecutionException {
       this.work();
   }
   
  
}


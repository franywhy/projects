package com.izhubo.schedule;

import java.io.FileWriter;
import java.io.IOException;
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
import com.izhubo.rest.pushmsg.IPushService;
import com.izhubo.rest.pushmsg.XinggePushService;
import com.izhubo.rest.pushmsg.model.PushMsgActionEnum;
import com.izhubo.rest.pushmsg.model.PushMsgBase;
import com.izhubo.rest.pushmsg.model.PushMsgTypeEnum;



public class OtherSchedule extends QuartzJobBean{

	
	
	//@Value("#{application['admin.domain']}")
	private String temp_domain = "http://www.hqtest.com/";
	
	
     private String indexurl = "/index.xhtml";
	
	private   String file = "c:/temp/index.html";




	private static Logger logger = LoggerFactory.getLogger(OtherSchedule.class);
	  /*业务实现*/
   public void work() {
	   logger.info("执行调度任务静态化："+new Date());
       try {
    	   
    	  String html =   HttpClientUtil.get(temp_domain+indexurl, null);
          
    	  saveAsFileWriter(html);
    	
		    
		  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			  logger.info("执行保报错"+new Date());
			e.printStackTrace();
		}
   }

   @Override
   protected void executeInternal(JobExecutionContext arg0)
           throws JobExecutionException {
       this.work();
   }
   
   
   private void saveAsFileWriter(String content) {

	   FileWriter fwriter = null;
	   try {
	    fwriter = new FileWriter(file);
	    fwriter.write(content);
	   } catch (IOException ex) {
	    ex.printStackTrace();
	   } finally {
	    try {
	     fwriter.flush();
	     fwriter.close();
	    } catch (IOException ex) {
	     ex.printStackTrace();
	    }
	   }
	  }
   
  
}


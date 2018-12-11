package com.izhubo.schedule;



import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;


import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.annotation.Resource;









import org.json.JSONArray;
import org.json.JSONObject;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Controller;


import com.izhubo.rest.pushmsg.IPushService;
import com.izhubo.rest.pushmsg.XinggePushService;
import com.izhubo.rest.pushmsg.model.PushMsgActionEnum;
import com.izhubo.rest.pushmsg.model.PushMsgBase;
import com.izhubo.rest.pushmsg.model.PushMsgTypeEnum;
import com.mongodb.BasicDBObject;




public class PushMsgTKRemainSchedule extends QuartzJobBean{

	
//	 @Resource(name="messageProductorService")
//	protected MessageProductorService messageProductorService;
	
	private String[] pushArray = {"最可怕的敌人，就是没有坚强的信念，坚持才有高通过率 ",
            "天行健，君子以自强不息，从题库训练中提升自己对知识的掌握",
            "绳锯木断，水滴石穿，那些拿到证的人都源于不懈的题库训练",
            "壮志与毅力是成功的双翼，学会计的人最懂得积累的宝贵 ",
            "才气就是长期的坚持不懈，获取会计从业证离不开每日的训练",
            "毅力是永久的享受，成功的人都把毅力作为心中的瑰宝",
            "学而时习之，不亦说乎？杰出的人都善于把枯燥化成甘露",
            };
	 public IPushService GetPushService()
	 {
	    	XinggePushService xingePush = new XinggePushService();
	   
			return xingePush;
	    	
	 }


	private static Logger logger = LoggerFactory.getLogger(PushMsgTKRemainSchedule.class);
	  /*业务实现*/
    public void work() {
    	

    	
    	  logger.info("执行调度任务："+new Date());
        try {
        	
        	 IPushService pushService =GetPushService();
        	 pushService.PushServiceInit(1);
        	 
        	 Random rand = new Random();
        	 int i = rand.nextInt(5); //int范围类的随机数\
        	 
        	 String pushmsg = pushArray[i];

        	 
        	PushMsgBase androidmsg = new PushMsgBase();
        	androidmsg.msg_title = "会答";
        	androidmsg.msg_content = pushmsg;	
        	androidmsg.msg_type = PushMsgTypeEnum.openpage.ordinal();
 			androidmsg.push_action_name = PushMsgActionEnum.defult.toString();
 			androidmsg.define_info = "";
 			pushService.PushMsgToAllStudents_android(androidmsg);
 			
 			
 			
 			PushMsgBase iosmsg = new PushMsgBase();
 			iosmsg.msg_title = pushmsg;	
 			iosmsg.msg_type = PushMsgTypeEnum.openpage.ordinal();
 			iosmsg.push_action_name = PushMsgActionEnum.defult.toString();
 			iosmsg.define_info = "";
 			pushService.PushMsgToAllStudents_ios(iosmsg);
		    
		
		    
		  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			  System.out.println("执行保报错"+new Date());
			e.printStackTrace();
		}
    }

    @Override
    protected void executeInternal(JobExecutionContext arg0)
            throws JobExecutionException {
        this.work();
    }
    
   
}





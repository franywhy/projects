package com.izhubo.schedule;



import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;


import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;









import org.json.JSONArray;
import org.json.JSONObject;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Controller;









import com.izhubo.rest.pushmsg.IPushService;
import com.izhubo.rest.pushmsg.XinggePushService;
import com.izhubo.rest.pushmsg.model.PushMsgActionEnum;
import com.izhubo.rest.pushmsg.model.PushMsgBase;
import com.izhubo.rest.pushmsg.model.PushMsgTypeEnum;
import com.mongodb.BasicDBObject;


public class PushMsgMainRemainSchedule extends QuartzJobBean{

	
//	 @Resource(name="messageProductorService")
//	protected MessageProductorService messageProductorService;
	@Value("#{application['push.env']}")
	private int PUSH_ENV = 2;

	private String DEV = "test";


	private String[] pushArray = {"更简单有效的考证战术是题海战术",
			                      "今天做的这道题考试会遇到吗？",
			                      "很多学员说考试碰到了原题",
			                      "多做一题也许就能多拿一分",
			                      "做题更能提高注意力哦",
			                      "错题再巩固一下会有新收获哦",	
			                      "多做题考试会更轻松的",
			                      "合理做题事半功倍",
			                      "做题能检验知识点掌握的程度",
			                      "万水千山总是情，不会做题怎能行",
			                      
			                      "24小时都有老师等着你",
			                      "不懂就要问，害什么羞！",
			                      "学习的意志将创造奇迹般的奇迹",
			                      "也许你需要专业的会计老师",
			                      "我是经验会计，欢迎大家提问",
			                      "教你怎么学会计，无偿回答问题",
			                      "随身的专属会计辅导老师",
			                      "有句话想对你说：做题时间到了",
			                      "1对1免费会计问答",
			                      "我是专业会计老师，免费在线解答疑问",
			                      "学问学问不懂就问",
			                      
			                      "会答是不是挺好的？分享给朋友吧",
			                      "一个能体现你认真程度的APP",
			                      "一个能体现你学习决心的APP",
			                      "你的朋友也有会计疑问吗？",
			                      "有好东西就要跟大家一起分享",
			                      "下课后，跟大家一起来上辅导课",
			                      "真正的友谊，在于一起努力",
			                      "分享你学会计的神器",
			                      "跟朋友一起学习更有趣哦",
			                      "听说邀请好友有福利哦"  
			                      };
	
	 public IPushService GetPushService()
	 {
	    	XinggePushService xingePush = new XinggePushService();
	   
			return xingePush;
	    	
	 }

	private static Logger logger = LoggerFactory.getLogger(PushMsgMainRemainSchedule.class);
	  /*业务实现*/
    public void work() {
    	  logger.info("执行调度任务："+new Date());
        try {
        	
        	PUSH_ENV =Integer.valueOf(com.izhubo.rest.AppProperties.get("dev").toString());
			DEV = com.izhubo.rest.AppProperties.get("dev").toString();
        	if(PUSH_ENV == 1&&"product".equals(DEV))
        	{
        
        	 IPushService pushService =GetPushService();
        	 pushService.PushServiceInit(PUSH_ENV);
        	 
        	 Random rand = new Random();
        	 int i = rand.nextInt(30); //int范围类的随机数\
        	 
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
        	}
		
		    
		  
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
    
    public static void main(String[] args) throws Exception {

		//String key = AES.aesDecrypt("mclj/IYQjqt+njp/8as2cg==", ACAESKEY);
		//System.out.println("cal " + key);
		
    	XinggePushService pushService = new XinggePushService();
    	 pushService.PushServiceInit(1);
    	 
    	 String[] pushArray = {"多识而不穷，畜疑以待问，学会问是一种高效的学习能力",
                 "人非生而知之者，孰能无惑？惑而不从师，其为惑也，终不解矣",
                 "敏而好学，不耻下问，只有会问的人才知道自己的不足之处",
                 "好问的人，只做了五分种的愚人；耻于发问的人，终身为愚人",
                 "玉不磨砻难作器，人非问学岂成贤，多问善问少走弯路",
                 "好问则裕，自用则小，聪明人都用问来充实自己的学问见识",
                 "学问，学问，一学二问，不学不问，是个愚人，足不出户问名师",
                 };
    	 
    	 Random rand = new Random();
    	 int i = rand.nextInt(5); //int范围类的随机数\
    	 
    	 String pushmsg = pushArray[i];

    	 
    	PushMsgBase androidmsg = new PushMsgBase();
    	androidmsg.msg_title = "推给玉平题库，收到请qq回复";
    	androidmsg.msg_content = pushmsg;	
    	androidmsg.msg_type = PushMsgTypeEnum.text.ordinal();
			androidmsg.push_action_name = PushMsgActionEnum.mytest.toString();
			androidmsg.define_info = "";
			
			
			
			
			
			pushService.PushMsgToSingleUser(10010943, androidmsg);
			
			
			//pushService.PushMsgAndroidTest(10361200, androidmsg);
			
			
		
    	//pushService.PushMsgToSingleUser(10037953, pushmsg);
			
	


	}
    
   
    
    
   
}





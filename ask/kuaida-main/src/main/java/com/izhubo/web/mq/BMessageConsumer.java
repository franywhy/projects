package com.izhubo.web.mq;
import java.awt.print.Printable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.izhubo.model.AccScoreGainType;
import com.izhubo.rest.anno.Rest;
import com.izhubo.rest.persistent.KGS;
import com.izhubo.web.BaseController;
import com.izhubo.web.msg.MsgProcess;
import com.izhubo.web.score.ScoreBase;
import com.izhubo.web.teaching.TeachingController;
import com.izhubo.web.teaching.TeachingProcss;



public class BMessageConsumer  implements ChannelAwareMessageListener{


	public BMessageConsumer(MongoTemplate mainMongo,KGS msgKGS) {
		
		_msgKGS = msgKGS;
		_mainMongo = mainMongo;
	}
	
    public BMessageConsumer() {
		
	
	}
	public MongoTemplate _mainMongo;
	


	private KGS _msgKGS;
	
	
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		// TODO Auto-generated method stub
		 System.out.println("收到队列信息" + " [x] Received '" + message + "'"); 
		 
		 try {
			 
			 String body=new String(message.getBody(),"UTF-8");
			 JSONObject JO = new JSONObject(body);
			 MsgProcess  teachering = new MsgProcess(_mainMongo,_msgKGS);
			 
			//{"dbilldate":1381280280000,"student_name":"王玲华","lasttime":1478565302000,"student_state_name":"潜在","student_state":"1","nc_id":"0001A510000000006ZCO","student_ncid":"JC172013100900026068","remark":"未跟进","_id":"N_0001A510000000006ZCO","student_mobile":"15390280658","push_time":1478571610629,"pk_org":"0001A5100000000043S7"}
			 
			
			 //默认总部，有就发给对应校区
				 String School_pk = "0001A510000000000KY0";
				 if(JO.has("pk_org"))
				 {
					 School_pk = JO.getString("pk_org");
				 }
			 teachering.processTeacherMsg("您的商机状态有更新", School_pk, JO.getString("teacher_id"), 0, _mainMongo);
			 
			 channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			 
		} catch (Exception e) {
			// TODO: handle exception
			 System.out.println(e.toString());
			 //记录报错日志
			 channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			// channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	        	
		
	            //发送应答  
	       
		  
	     
		   
		// channel.basicReject(message.getMessageProperties().getDeliveryTag(), true); 
	            	
	            	   
	            
	         
	            
	           

	     
    	
		
    	
	}
}
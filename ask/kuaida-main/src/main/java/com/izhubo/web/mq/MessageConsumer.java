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
import com.izhubo.web.BaseController;
import com.izhubo.web.score.ScoreBase;
import com.izhubo.web.teaching.TeachingController;
import com.izhubo.web.teaching.TeachingProcss;



public class MessageConsumer  implements ChannelAwareMessageListener{


	public MessageConsumer(MongoTemplate mainMongo) {
		
		_mainMongo = mainMongo;
	}
	
    public MessageConsumer() {
		
	
	}
	public MongoTemplate _mainMongo;
	

	
	
	
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		// TODO Auto-generated method stub
		 System.out.println("收到队列信息" + " [x] Received '" + message + "'"); 
		 
		 try {
			 
			 String body=new String(message.getBody(),"UTF-8");
			 JSONObject JO = new JSONObject(body);
			 TeachingProcss  teachering = new TeachingProcss(_mainMongo);
			 teachering.processDatafromNc(JO.toString(), _mainMongo);
			 
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
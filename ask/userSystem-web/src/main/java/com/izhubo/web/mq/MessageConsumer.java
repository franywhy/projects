package com.izhubo.web.mq;
import java.awt.print.Printable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;











import org.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.rabbitmq.client.Channel;
import com.izhubo.rest.AppProperties;
import com.izhubo.rest.anno.Rest;
import com.izhubo.rest.common.util.http.HttpClientUtil4_3;
import com.izhubo.userSystem.App;




public class MessageConsumer  implements ChannelAwareMessageListener{


	public MessageConsumer(MongoTemplate mainMongo,MongoTemplate qqUserMongo) {
		
		_mainMongo = mainMongo;
		_qqUserMongo = qqUserMongo;
	}
	
	protected String usdomain = AppProperties.get("us.domain").toString();
	
	
	public MongoTemplate _mainMongo;
	
	public MongoTemplate _qqUserMongo;
	
	private void ProcessTeacher(JSONObject JO) throws UnsupportedEncodingException
	{
		String url = usdomain+"/api_register_addteacher";
		Map<String, String> params = new HashMap<String, String>();
		String encodeString = URLEncoder.encode(JO.toString(),"UTF-8");
		params.put("json", encodeString);
		
		
		try {
			HttpClientUtil4_3.post(url, params, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void ProcessStudent(JSONObject JO)
	{
		 String username =  JO.get("phone").toString();
		 String school_code =  JO.get("school_code").toString();
		 
			DBObject db = _qqUserMongo.getCollection("qQUser").findOne(
					new BasicDBObject("username", username),
					new BasicDBObject("tuid", 1));
			
		 if(db!=null)
		 {
		 String tuid = db.get("tuid").toString();
		 
			BasicDBObject up = new BasicDBObject("nc_id", JO.get("nc_id").toString());
            up.append("school_code", school_code);
			// nc_id
			_mainMongo.getCollection("users").update(
					new BasicDBObject("tuid", tuid),
					new BasicDBObject("$set", up));  
		
		 }
		 else
		 {
			 BasicDBObject insert = new BasicDBObject("_id", JO.get("phone").toString());
			 insert.append("phone", JO.get("phone").toString());
			 insert.append("nc_id", JO.get("nc_id").toString());
			 insert.append("school_code", JO.get("school_code").toString());
			 _mainMongo.getCollection("users_ncpk").save(insert);
		 }
	}
	
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		// TODO Auto-generated method stub
		 System.out.println("收到队列信息" + " [x] Received '" + message + "'"); 
		
	
		
		 
		 try {
			 
			 String body=new String(message.getBody(),"utf-8");
			 JSONObject JO = new JSONObject(body);
			 
			 if(JO.has("usertype")&&JO.get("usertype").equals(0))
			 {
				 ProcessStudent(JO);
			 }
			 else
			 {
				 if(JO.get("teacher_mobile")!=null)
				 {
				    ProcessTeacher(JO);
				 }
			 }
			
			 
			 channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			 
		} catch (Exception e) {
			// TODO: handle exception
			 System.out.println(e.toString());
			 //记录报错日志
			 
			 channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	        	
		
	
	            	
	            	   
	            
	         
	            
	           

	     
    	
		
    	
	}
}
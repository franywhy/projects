package com.izhubo.rest.pushmsg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.izhubo.rest.pushmsg.model.PushMsgBase;
import com.izhubo.rest.pushmsg.model.PushMsgTypeEnum;
import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.Style;
import com.tencent.xinge.TimeInterval;
import com.tencent.xinge.XingeApp;

public class XinggePushService implements IPushService{
	
	
	private static final long STU_IOS_XINGE_ACCESS_ID = 2200149294L  ;
	
	private static final String STU_IOS_SECRET_KEY = "55b252ee247f1c9b2e642ac8cc9eec61";
	
	
	
    private static final long TEC_IOS_XINGE_ACCESS_ID = 2200162541L  ;
	
	private static final String TEC_IOS_SECRET_KEY = "d1b458c6b708358bddded10c44d68a2a";
	
	
    private static final long STU_ANDROID_XINGE_ACCESS_ID = 2100137624L  ;
	
	private static final String STU_ANDROID_SECRET_KEY = "be6cc9edf050a8e943388f0684a6548b";
	
	
    private static final long TEC_ANDROID_XINGE_ACCESS_ID = 2100162947L  ;
	
	private static final String TEC_ANDROID_SECRET_KEY = "671ed1e791b2be593274ee6980e00636";
	

	private int ENV = 2;
	
	
	

	@Override
	public void PushServiceInit(int isDev) {
		// TODO Auto-generated method stub
	
		ENV = isDev;
		
		
	}

	@Override
	public void PushMsgToAll(PushMsgBase msg) {
		// TODO Auto-generated method stub
		
	

		
	}

	@Override
	public void PushMsgToSingleUser(int UserId, PushMsgBase msg) {
		

		
		PushMsgIOSByUserId(UserId,STU_IOS_XINGE_ACCESS_ID,STU_IOS_SECRET_KEY,msg);
		PushMsgAndroidByUserId(UserId,STU_ANDROID_XINGE_ACCESS_ID,STU_ANDROID_SECRET_KEY,msg);
	}
	
	
	@Override
	public void PushMsgToSingleTeacher(int UserId, PushMsgBase msg) {
		// TODO Auto-generated method stub
		
		PushMsgIOSByUserId(UserId,TEC_IOS_XINGE_ACCESS_ID,TEC_IOS_SECRET_KEY,msg);
		PushMsgAndroidByUserId(UserId,TEC_ANDROID_XINGE_ACCESS_ID,TEC_ANDROID_SECRET_KEY,msg);
	}
	
	

	@Override
	public void PushMsgToAllTeachers(PushMsgBase msg) {
		// TODO Auto-generated method stub
		PushMsgIOS(TEC_IOS_XINGE_ACCESS_ID,TEC_IOS_SECRET_KEY,msg);
		PushMsgAndroid(TEC_ANDROID_XINGE_ACCESS_ID,TEC_ANDROID_SECRET_KEY,msg);

	}

	@Override
	public void PushMsgToAllStudents(PushMsgBase msg) {
		// TODO Auto-generated method stub
		
		PushMsgIOS(STU_IOS_XINGE_ACCESS_ID,STU_IOS_SECRET_KEY,msg);
		PushMsgAndroid(STU_ANDROID_XINGE_ACCESS_ID,STU_ANDROID_SECRET_KEY,msg);

	}

	@Override
	public void PushMsgToStudentsByTag(String tag, PushMsgBase msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void PushMsgToTeachersByTag(String tag, PushMsgBase msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void PushMsgToGroup(String GroupId, PushMsgBase msg) {
		// TODO Auto-generated method stub
		
	}
	
	
	private void PushMsgIOS(long accId,String secret_key,PushMsgBase msg)
	{
		XingeApp xinge = new XingeApp(accId, secret_key);



		MessageIOS iosmessage = new MessageIOS();
		iosmessage.setExpireTime(86400);
		iosmessage.setAlert(msg.msg_title);
		iosmessage.setBadge(1);
		iosmessage.setSound("beep.wav");
		TimeInterval acceptTime1 = new TimeInterval(0,0,23,59);
		iosmessage.addAcceptTime(acceptTime1);
		Map<String, Object> Ioscustom = new HashMap<String, Object>();
		
		Ioscustom.put("msg_type", msg.msg_type);
		Ioscustom.put("msg_url", msg.msg_url);
		Ioscustom.put("push_action_name", msg.push_action_name);
		Ioscustom.put("define_info", msg.define_info);


		//3.0.1app 新增一个自定义字段
		Map<String, Object> newcustom = new HashMap<String, Object>();
		newcustom.put("content", msg.define_info);
		newcustom.put("msg_action",  msg.push_action_name);
		newcustom.put("video_id",  msg.video_id);
		newcustom.put("video_name",  msg.video_name);
		newcustom.put("video_record_id",  msg.video_record_id);
		Ioscustom.put("data",newcustom);
		

		iosmessage.setCustom(Ioscustom);

		JSONObject resultIOS =xinge.pushAllDevice(0, iosmessage, ENV);
		System.out.println("ios:"+resultIOS+" dev:"+String.valueOf(ENV));
		logintxt(resultIOS.toString());
	}
	

	
	private void PushMsgIOSByUserId(int userId,long accId,String secret_key,PushMsgBase msg)
	{
		
		XingeApp xinge = new XingeApp(accId, secret_key);
		
		MessageIOS iosmessage = new MessageIOS();
		iosmessage.setExpireTime(86400);
		iosmessage.setAlert(msg.msg_title);
		iosmessage.setBadge(1);
		iosmessage.setSound("beep.wav");
		TimeInterval acceptTime1 = new TimeInterval(0,0,23,59);
		iosmessage.addAcceptTime(acceptTime1);
		Map<String, Object> Ioscustom = new HashMap<String, Object>();
		
		Ioscustom.put("msg_type", msg.msg_type);
		Ioscustom.put("msg_url", msg.msg_url);
		Ioscustom.put("push_action_name", msg.push_action_name);
		Ioscustom.put("define_info", msg.define_info);

		Map<String, Object> newcustom = new HashMap<String, Object>();
		newcustom.put("content", msg.define_info);
		newcustom.put("msg_action",  msg.push_action_name);
		newcustom.put("video_id",  msg.video_id);
		newcustom.put("video_name",  msg.video_name);
		newcustom.put("video_record_id",  msg.video_record_id);
		Ioscustom.put("data",newcustom);


		iosmessage.setCustom(Ioscustom);
		
		JSONObject resultIOS = xinge.pushSingleAccount(0, String.valueOf(userId), iosmessage, ENV);
		
		
		logintxt(iosmessage.toJson());
		logintxt(resultIOS.toString());
		System.out.println("ios:"+iosmessage.toJson());
		System.out.println("ios:"+resultIOS);
	}
	
	
	
	
	private void PushMsgAndroid(long accId,String secret_key,PushMsgBase msg)
	{
		XingeApp xinge = new XingeApp(accId, secret_key);
       
		Message message = new Message();
		message.setExpireTime(86400);
		message.setType(Message.TYPE_NOTIFICATION);
		message.setTitle(msg.msg_title);
		message.setContent(msg.msg_content);
		message.setStyle(new Style(0,1,1,1,0));

	
		Map<String, Object> androidcustom = new HashMap<String, Object>();
		
		 if(msg.msg_type == PushMsgTypeEnum.adver.ordinal())
		 {
		    	ClickAction action = new ClickAction();
				action.setActionType(ClickAction.TYPE_URL);
				action.setUrl(msg.msg_url);
				message.setAction(action);
		  }
		 else {
				ClickAction action = new ClickAction();
				action.setActionType(ClickAction.TYPE_ACTIVITY);
				action.setActivity("com.kjcity.answer.student.activity.MessActivity");
				message.setAction(action);
		 }
	    
		androidcustom.put("msg_type", msg.msg_type);
		androidcustom.put("msg_url", msg.msg_url);
		androidcustom.put("push_action_name", msg.push_action_name);
		androidcustom.put("define_info", msg.define_info);

		Map<String, Object> newcustom = new HashMap<String, Object>();
		newcustom.put("content", msg.define_info);
		newcustom.put("msg_action",  msg.push_action_name);
		newcustom.put("video_id",  msg.video_id);
		newcustom.put("video_name",  msg.video_name);
		newcustom.put("video_record_id",  msg.video_record_id);
		androidcustom.put("data",newcustom);

		

		message.setCustom(androidcustom);

		JSONObject resultIOS =xinge.pushAllDevice(0, message);
		System.out.println("android:"+resultIOS);

		logintxt(resultIOS.toString());
	}
	
	private void PushMsgAndroidByUserId(int userId,long accId,String secret_key,PushMsgBase msg)
	{
		
		XingeApp xinge = new XingeApp(accId, secret_key);
	       
		Message message = new Message();
		message.setExpireTime(86400);
		message.setType(Message.TYPE_NOTIFICATION);
		message.setTitle(msg.msg_title);
		message.setContent(msg.msg_content);
		message.setStyle(new Style(0,1,1,1,0));
		
		 if(msg.msg_type == PushMsgTypeEnum.adver.ordinal())
		 {
		    	ClickAction action = new ClickAction();
				action.setActionType(ClickAction.TYPE_URL);
				action.setUrl(msg.msg_url);
				message.setAction(action);
		  }
		 else {
				ClickAction action = new ClickAction();
				action.setActionType(ClickAction.TYPE_ACTIVITY);
				action.setActivity("com.kjcity.answer.student.activity.MessActivity");
				message.setAction(action);
		}
	
		Map<String, Object> androidcustom = new HashMap<String, Object>();
		
		androidcustom.put("msg_type", msg.msg_type);
		androidcustom.put("msg_url", msg.msg_url);
		androidcustom.put("push_action_name", msg.push_action_name);
		androidcustom.put("define_info", msg.define_info);

		Map<String, Object> newcustom = new HashMap<String, Object>();
		newcustom.put("content", msg.define_info);
		newcustom.put("msg_action",  msg.push_action_name);
		newcustom.put("video_id",  msg.video_id);
		newcustom.put("video_name",  msg.video_name);
		newcustom.put("video_record_id",  msg.video_record_id);
		androidcustom.put("data",newcustom);
		

		message.setCustom(androidcustom);

		
		JSONObject resultIOS = xinge.pushSingleAccount(0, String.valueOf(userId), message);
		System.out.println("android:"+resultIOS);
		logintxt(resultIOS.toString());
	}

	@Override
	public void PushMsgToAllStudents_android(PushMsgBase msg) {
		// TODO Auto-generated method stub
		PushMsgAndroid(STU_ANDROID_XINGE_ACCESS_ID,STU_ANDROID_SECRET_KEY,msg);
	}

	@Override
	public void PushMsgToAllStudents_ios(PushMsgBase msg) {
		// TODO Auto-generated method stub
		PushMsgIOS(STU_IOS_XINGE_ACCESS_ID,STU_IOS_SECRET_KEY,msg);
	}
	
	
	public void PushMsgAndroidTest(int userId,PushMsgBase msg)
	{
		PushMsgAndroidTest(userId,STU_ANDROID_XINGE_ACCESS_ID,STU_ANDROID_SECRET_KEY,msg);
	}
	
	
	private void PushMsgAndroidTest(int userId,long accId,String secret_key,PushMsgBase msg)
	{
		XingeApp xinge = new XingeApp(accId, secret_key);
       
		Message message = new Message();
		message.setExpireTime(86400);
		message.setType(Message.TYPE_NOTIFICATION);
		message.setTitle(msg.msg_title);
		message.setContent(msg.msg_content);
		message.setStyle(new Style(0,1,1,1,0));
		
		ClickAction action = new ClickAction();
		action.setActionType(ClickAction.TYPE_ACTIVITY);
		action.setActivity("com.kjcity.answer.student.activity.MessActivity");
		message.setAction(action);
	
		Map<String, Object> androidcustom = new HashMap<String, Object>();
		
	
	    
		androidcustom.put("msg_type", msg.msg_type);
		androidcustom.put("msg_url", msg.msg_url);
		androidcustom.put("push_action_name", msg.push_action_name);
		androidcustom.put("video_record_id",  msg.video_record_id);
		androidcustom.put("define_info", msg.define_info);

		

		message.setCustom(androidcustom);

		JSONObject resultIOS = xinge.pushSingleAccount(0, String.valueOf(userId), message);
		System.out.println("android:"+resultIOS);
		logintxt(resultIOS.toString());
	}
	
	
	private void logintxt(String loginfo)
	{
		try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw  
			  
	          
			  
            /* 写入Txt文件 */  
            File writename = new File("log.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件  
            writename.createNewFile(); // 创建新文件 
            
      
            BufferedWriter out = new BufferedWriter( new OutputStreamWriter(new FileOutputStream(writename),"GB2312"));
            
            out.append(new Date().toString()+"|"+loginfo); // \r\n即为换行  
            out.flush(); // 把缓存区内容压入文件  
            out.close(); // 最后记得关闭文件  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}
	
	
	



}

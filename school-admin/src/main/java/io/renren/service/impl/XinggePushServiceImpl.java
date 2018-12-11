package io.renren.service.impl;


import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tencent.xinge.Message;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.XingeApp;

import io.renren.service.XinggePushService;

@Service("xinggePushService")
public class XinggePushServiceImpl implements XinggePushService {
	
	private static final Long ANDROID_ACCESS_Id = 2100258303L;//android应用id
	private static final String ANDROID_SECRET_KEY = "cc238257d1c9a52b710de548b94162c1";//android应用密钥
	private static final Long IOS_ACCESS_Id = 2200260029L;//ios应用id
	private static final String IOS_SECRET_KEY = "e3a2d014bb58259b1be89eae5c39a538";//ios应用密钥
	//信鸽推送 1.生产环境  2.测试环境
	private static int XINGE_APP_IOSENV = 2;
	@Value("#{application['app.xinge.iosenv']}")
	private void setIosnv(String str){
		if(StringUtils.isNotBlank(str)){
			XINGE_APP_IOSENV = Integer.valueOf(str);
		}
	}
/*	
	@Override
	public JSONObject pushAccountAndroid(long accessId, String secretKey, String title, String content,
			String account) {
		return XingeApp.pushAccountAndroid(ANDROID_ACCESS_Id, ANDROID_SECRET_KEY, title, content, account);
	}

	@Override
	public JSONObject pushTagAndroid(long accessId, String secretKey, String title, String content, String tag) {
		return XingeApp.pushTagAndroid(ANDROID_ACCESS_Id, ANDROID_SECRET_KEY, title, content, tag);
	}

	@Override
	public JSONObject PushAccountIos(long accessId, String secretKey, String content, String account, int environment) {
		return XingeApp.pushAccountIos(IOS_ACCESS_Id, IOS_SECRET_KEY, content, account, XingeApp.IOSENV_PROD);
	}

	@Override
	public JSONObject pushTagIos(long accessId, String secretKey, String content, String tag, int environment) {
 		return XingeApp.pushTagIos(IOS_ACCESS_Id, IOS_SECRET_KEY, content, tag, XingeApp.IOSENV_PROD);
	}
*/	
	public static void main(String[] args) {
		System.out.println(XingeApp.IOSENV_PROD);//1
		System.out.println(XingeApp.IOSENV_DEV);//2
	}
	@Override
	public void pushAccountAndroidOrIosByUserId(long userId, String title) {
		PushMsgIOSByUserId(userId, IOS_ACCESS_Id, IOS_SECRET_KEY, title);
		PushMsgAndroidByUserId(userId, ANDROID_ACCESS_Id, ANDROID_SECRET_KEY, title);
	}
	@Override
	public void pushTagAndroidOrIos(List<String> tagList, String title) {
		PushTagIos(tagList, IOS_ACCESS_Id, IOS_SECRET_KEY, title);
		PushTagAndroid(tagList, ANDROID_ACCESS_Id, ANDROID_SECRET_KEY, title);
	}
	@Override
	public void pushTagIos(List<String> tagList, String title) {
		PushTagIos(tagList, IOS_ACCESS_Id, IOS_SECRET_KEY, title);
	}
	@Override
	public void pushTagAndroid(List<String> tagList, String title) {
		PushTagAndroid(tagList, ANDROID_ACCESS_Id, ANDROID_SECRET_KEY, title);
	}
	@Override
	public void pushAllAccount(String title) {
		if(XINGE_APP_IOSENV == 1){
			PushAllIosAccount(IOS_ACCESS_Id, IOS_SECRET_KEY, title);
			PushAllAndroidAccount(ANDROID_ACCESS_Id, ANDROID_SECRET_KEY, title);
		}
	}
	//通过userId发送android消息
	private void PushMsgAndroidByUserId(long userId, long accessId, String secretKey, String title){
		
		XingeApp xinge = new XingeApp(accessId, secretKey);
		Message message = new Message();
		message.setExpireTime(86400);//设置离线消息存活时间为1天（单位是秒）
		message.setType(Message.TYPE_NOTIFICATION);
		message.setTitle(title);
		JSONObject resultAndroid = xinge.pushSingleAccount(0, String.valueOf(userId), message);
		
		System.out.println("android:"+resultAndroid);
	}
	
	//通过UserId发送IOS消息
	private void PushMsgIOSByUserId(long userId, long accessId, String secretKey, String title){
		
		XingeApp xinge = new XingeApp(accessId, secretKey);
		MessageIOS iosmessage = new MessageIOS();
		iosmessage.setExpireTime(86400);//设置离线消息存活时间为1天（单位是秒）
		iosmessage.setAlert(title);
		JSONObject resultIOS = xinge.pushSingleAccount(0, String.valueOf(userId), iosmessage, XINGE_APP_IOSENV);
//		JSONObject resultIOS = xinge.pushSingleAccount(0, String.valueOf(userId), iosmessage, XingeApp.IOSENV_DEV);
		
		System.out.println("ios:"+resultIOS);
	}
	
	//通过tag发送android消息
	private void PushTagAndroid(List<String> tagList, long accessId, String secretKey, String title){
		
		XingeApp xinge = new XingeApp(accessId, secretKey);
		Message message = new Message();
		message.setExpireTime(86400);//设置离线消息存活时间为1天（单位是秒）
		message.setType(Message.TYPE_NOTIFICATION);
		message.setTitle(title);
		JSONObject resultAndroid = xinge.pushTags(0, tagList, "OR", message);
		
		System.out.println("android:"+resultAndroid);
	}
	
	//通过tag发送IOS消息
	private void PushTagIos(List<String> tagList, long accessId, String secretKey, String title){
		
		XingeApp xinge = new XingeApp(accessId, secretKey);
		MessageIOS iosmessage = new MessageIOS();
		iosmessage.setExpireTime(86400);//设置离线消息存活时间为1天（单位是秒）
		iosmessage.setAlert(title);
		iosmessage.setBadge(1);
		iosmessage.setSound("beep.wav");
		JSONObject resultIOS = xinge.pushTags(0, tagList, "OR", iosmessage, XINGE_APP_IOSENV);
		
		System.out.println("ios:"+resultIOS);
	}
	
	//发送消息给所有android用户
	private void PushAllAndroidAccount(long accessId, String secretKey, String title){
		XingeApp xinge = new XingeApp(accessId, secretKey);
		Message message = new Message();
		message.setExpireTime(86400);//设置离线消息存活时间为1天（单位是秒）
		message.setType(Message.TYPE_NOTIFICATION);
		message.setTitle(title);
		JSONObject resultAndroid = xinge.pushAllDevice(0, message);
		
		System.out.println("android:"+resultAndroid);
	}
	
	//发送消息给所有ios用户
	private void PushAllIosAccount(long accessId, String secretKey, String title){
		XingeApp xinge = new XingeApp(accessId, secretKey);
		MessageIOS iosmessage = new MessageIOS();
		iosmessage.setExpireTime(86400);//设置离线消息存活时间为1天（单位是秒）
		iosmessage.setAlert(title);
		iosmessage.setBadge(1);
		iosmessage.setSound("beep.wav");
		JSONObject resultIOS = xinge.pushAllDevice(0, iosmessage, XINGE_APP_IOSENV);
		
		System.out.println("ios:"+resultIOS+"ENV:"+XINGE_APP_IOSENV);
	}
}

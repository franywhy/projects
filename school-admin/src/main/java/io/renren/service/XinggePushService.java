package io.renren.service;

import java.util.List;

public interface XinggePushService {
/*	
	*//**
	 * Android 平台推送消息给单个账号
	 * @param accessId 推送目标应用的id
	 * @param secretKey 目标应用的密钥
	 * @param title 消息标题，android专属
	 * @param content 消息内容
	 * @param account 接受消息的账号
	 * @return
	 *//*
	public JSONObject pushAccountAndroid(long accessId,String secretKey,String title,String
			content,String account);
	*//**
	 * Android 平台推送消息给标签选中设备
	 * @param accessId 推送目标应用的id
	 * @param secretKey 目标应用的密钥
	 * @param title 消息标题，Android专属
	 * @param content 消息内容
	 * @param tag 接收消息的设备标签
	 * @return
	 *//*
	public JSONObject pushTagAndroid(long accessId,String secretKey,String title,String
			content,String tag);
	*//**
	 * IOS 平台推送消息给单个账号
	 * @param accessId 推送目标应用的id
	 * @param secretKey 目标应用的密钥
	 * @param content 消息内容
	 * @param account 接受消息的账号
	 * @param environment 可选值为 XingeApp.IOSENV_PROD 或者 XingeApp.IOSENV_DEV，IOS专属
	 * @return
	 *//*
	public JSONObject PushAccountIos(long accessId,String secretKey,String content,String
			account,int environment);
	*//**
	 * IOS 平台推送消息给标签选中设备
	 * @param accessId 推送目标应用的id
	 * @param secretKey 目标应用的密钥
	 * @param content 消息内容
	 * @param tag 接收消息的设备标签
	 * @param environment 可选值为 XingeApp.IOSENV_PROD 或者 XingeApp.IOSENV_DEV，IOS专属
	 * @return
	 *//*
	public JSONObject pushTagIos(long accessId,String secretKey,String content,String tag,int
			environment);
*/
	
	
	/**
	 * 发送给单独一个账号（不管对方是IOS还是android）
	 * @param userId 用户id
	 * @param title 消息标题题
	 */
	public void pushAccountAndroidOrIosByUserId(long userId, String title);
	
	/**
	 * 群发给指定的tag用户（不管对方是IOS还是android）
	 * @param tagList 指定标签List
	 * @param title 消息标题
	 */
	public void pushTagAndroidOrIos(List<String> tagList, String title);
	
	/**
	 * 群发给指定的tag用户（IOS用户）
	 * @param tagList 指定标签List
	 * @param title 消息标题
	 */
	public void pushTagIos(List<String> tagList, String title);
	
	/**
	 * 群发给指定的tag用户（android用户）
	 * @param tagList 指定标签List
	 * @param title 消息标题
	 */
	public void pushTagAndroid(List<String> tagList, String title);
	
	/**
	 * 群发给所有用户
	 * @param title 消息标题
	 */
	public void pushAllAccount(String title);
	
}

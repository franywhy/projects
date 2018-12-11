package com.izhubo.model;

public class PushMsgModel {

	public String msg_id;
	public String msg_title;
	public String msg_content;
	//这个是推送的tag或者是推送的账号id
	public String to_user;
	public String create_time;
	public String msg_type_name;
	public int msg_type_id;
	//0为单独发给个人的 1是发给全部人的消息
	public int is_single;
	//这个是用于存推送消息的字段，比如某些消息需要一些特殊字段，会以json形式在这里存储
	public String defineContent;
	public String IsPushSuccess;
}

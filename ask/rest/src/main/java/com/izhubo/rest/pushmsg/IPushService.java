package com.izhubo.rest.pushmsg;

import com.izhubo.rest.pushmsg.model.PushMsgBase;

public interface IPushService {

	//初始化
	public void PushServiceInit(int isDev);
	
	//群发推送给所有的设备，会答学生端和教师端，包括android，ios
	public void PushMsgToAll(PushMsgBase msg);
	
	//发送给单独一个账号（不管对方是IOS还是android）
	public void PushMsgToSingleUser(int UserId,PushMsgBase msg);
	
	
	public void PushMsgToSingleTeacher(int UserId,PushMsgBase msg);
	
	//群发给教师端的消息
	public void PushMsgToAllTeachers(PushMsgBase msg);
	
	//群发给学生端的消息(全平台)
	public void PushMsgToAllStudents(PushMsgBase msg);
	
	public void PushMsgToAllStudents_android(PushMsgBase msg);
	
	public void PushMsgToAllStudents_ios(PushMsgBase msg);
	
	//发送给学生端指定的tag
	public void PushMsgToStudentsByTag(String tag, PushMsgBase msg);
	
	//发送给教师端指定的tag
	public void PushMsgToTeachersByTag(String tag, PushMsgBase msg);
	
	//发送给某个群组（预留后期群聊用）
	public void PushMsgToGroup(String GroupId, PushMsgBase msg);
	

}

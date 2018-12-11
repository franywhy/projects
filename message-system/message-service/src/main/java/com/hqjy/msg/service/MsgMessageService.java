package com.hqjy.msg.service;

import com.hqjy.msg.model.MsgMessage;

/**
 * 消息表接口
 * @author Administrator
 *
 */
public abstract class MsgMessageService  extends  BaseService<MsgMessage>{

	/**
	 * 添加消息数据
	 * @param sendTime 发送时间
	 * @param msg 消息josn
	 * @param sendPerson 发送人
	 * @param msgId 消息code
	 * @param sendStatus 发送状态
	 * @param sendType 发送类型
	 * @param recPerson 接收人
	 * @return
	 */
	public abstract int insertMsgMessage(String sendTime, String msg, String sendPerson,String msgId,int sendStatus,int sendType,String recPerson);

	/**
	 * 添加消息数据
	 * @param msgMessage
	 * @return
	 */
	public abstract int insertMsgMessage(MsgMessage msgMessage);


	/**
	 * 根据消息ID获取消息记录
	 * @param msgId
	 * @return
	 */
	public abstract MsgMessage findMsgMessageByCode(String msgId);

	public abstract  int update(MsgMessage message);

}

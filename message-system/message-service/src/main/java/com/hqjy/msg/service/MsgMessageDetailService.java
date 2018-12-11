package com.hqjy.msg.service;

import com.hqjy.msg.model.MsgMessageDetail;

/**
 * 消息详情表
 * @author Administrator
 *
 */

public abstract class MsgMessageDetailService<T> extends  BaseService<T> {



	//根据code来查询消息详情表
	public abstract MsgMessageDetail findMsgMessageDetailByCode(String code);

	/**
	 * 添加消息详情信息
	 * @param msgId 消息唯一代码(设置已读消息状态，持久到数据库)
	 * @param userId 接收人
 	*/
	public abstract void  insertMsgMessage(String msgId,String userId);


}

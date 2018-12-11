package com.hqjy.msg.service.impl;

import com.hqjy.msg.mapper.MsgMessageMapper;
import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.service.MsgMessageService;
import com.hqjy.msg.util.DateUtils;
import com.hqjy.msg.util.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MsgMessageServiceImpl extends  MsgMessageService {

	@Autowired
	public void setMsgMessageMapper(MsgMessageMapper msgMessageMapper) {
		super.setBaseMapper(msgMessageMapper);
	}



	/**
	 *
	 * @param msgId 消息的唯一标识符
	 * @return
	 */
	public MsgMessage findMsgMessageByCode(String msgId) {
		//根据群组查询
		Map map = new HashMap();
		map.put("code", msgId);
		//map.put("dr",Integer.valueOf(0));

		List<MsgMessage> list = super.example(map, MsgMessage.class);
		if(!ListUtils.listIsExists(list)) return null;
		MsgMessage m = list.get(0);
		return m;
	}

	@Override
	public int update(MsgMessage message) {

		/*Map map = new HashMap();
		map.put("code", message.getCode());
		map.put("dr",0);
		List<MsgMessage> list = super.example(map, MsgMessage.class);
		if(!ListUtils.listIsExists(list)) return -1;
		MsgMessage m = list.get(0);*/

		return ((MsgMessageMapper)super.getBaseMapper()).update(message.getId(),message);
	}

	/**
	 * 写入消息到消息表
	 * @param sendTime   发送时间
	 * @param msg        消息josn
	 * @param sendPerson 发送人
	 * @param msgId      消息code
	 * @param sendStatus 发送状态
	 * @param sendType   发送类型
	 * @param recPerson 接收人
	 * @return
	 */

	public int insertMsgMessage(String sendTime, String msg, String sendPerson, String msgId, int sendStatus, int sendType,String recPerson) {
 		int mess = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 		try {
			//创建一个消息对象
			MsgMessage ms = new MsgMessage();
 	 		ms.setCreateTime(new Date()); //创建时间
			//获取消息的接受方
			ms.setRecBy(recPerson);//接收方
			ms.setSendStatus(sendStatus);
			ms.setCode(msgId);
			ms.setSendBy(sendPerson);
			ms.setMessage(msg);  //消息的json格式
			ms.setSendType(sendType); //发送类型0立即 1按时
 		 	ms.setDr(0);
			//设置发送时间
			ms.setSendTime(format.parse(sendTime));

			mess = super.getBaseMapper().insert(ms);

		} catch (Exception e) {
			e.printStackTrace();
		}


		return mess;
	}

	@Override
	public int insertMsgMessage(MsgMessage msgMessage) {

		try {
			//创建一个消息对象

			msgMessage.setCreateTime(new Date()); //创建时间
			//获取消息的接受方
			msgMessage.setDr(0);
			//设置发送时间
			msgMessage.setSendTime(DateUtils.stringToDate(msgMessage.getSendTimeStr(),DateUtils.DATE_FORMAT));
			return super.getBaseMapper().insert(msgMessage);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

}


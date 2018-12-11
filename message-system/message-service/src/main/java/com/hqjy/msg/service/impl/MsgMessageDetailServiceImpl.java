package com.hqjy.msg.service.impl;

import com.hqjy.msg.mapper.MsgMessageDetailMapper;
import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.model.MsgMessageDetail;
import com.hqjy.msg.service.MsgMessageDetailService;
import com.hqjy.msg.service.MsgMessageService;
import com.hqjy.msg.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MsgMessageDetailServiceImpl extends MsgMessageDetailService<MsgMessageDetail> {

	@Autowired
	private MsgMessageService msgMessageService;
	@Autowired
	public void setMsgGroupMapper(MsgMessageDetailMapper msgMessageDetailMapper) {
		super.setBaseMapper(msgMessageDetailMapper);

	}
	/**
	 *
	 * @param msgId 消息的唯一标识符
	 * @return
	 */
	public MsgMessageDetail findMsgMessageDetailByCode(String msgId) {
		//根据群组查询
		Map map = new HashMap();
		map.put("code", msgId);

		List<MsgMessageDetail> list = super.example(map, MsgMessageDetail.class);
		MsgMessageDetail m = list.get(0);
		return m ;
	}
	/**
	 * 添加消息详情信息 (设置已读消息状态，持久到数据库)
	 * @param msgId 消息唯一代码
	 * @param userId 接收人
	 */
	@Override
	public void insertMsgMessage(String msgId, String userId) {
		Map map = new HashMap();
		map.put("code",msgId);
		map.put("recBy",userId);
 		List list = super.example(map,MsgMessageDetail.class);
		if (null==list || list.isEmpty()) {
 			//1.根据msgId查询消息表数据
			MsgMessage message = msgMessageService.findMsgMessageByCode(msgId);

			//2.查询得出的数据设置到消息详情表里
			MsgMessageDetail msd = new MsgMessageDetail();

			msd.setCode(msgId);//'消息唯一代码'
			msd.setSendBy("");//发送方
			msd.setRecBy(userId);//接收方为成员视频
			msd.setSendTime(message.getSendTime());//发送时间
			msd.setCreateTime(new Date());//创建时间
			msd.setMessage(message.getMessage());//消息内容
			msd.setIsRead(Constant.MSG_STATUS_READED);//消息已读

			//3.保存到数据库中
			super.insert(msd);
		}




	}


}

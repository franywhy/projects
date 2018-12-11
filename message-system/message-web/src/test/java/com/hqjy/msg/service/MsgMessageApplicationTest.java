package com.hqjy.msg.service;

import java.util.Date;
import java.util.UUID;

import com.hqjy.msg.service.MsgMessageService;
import com.hqjy.msg.util.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hqjy.msg.model.MsgMessage;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MsgMessageApplicationTest {

	@Autowired
	private MsgMessageService msgMessageService;
	
	//@Test
	public void test01(){
		MsgMessage msgMessage = new MsgMessage();
			msgMessage.setCode(UUID.randomUUID().toString());
			msgMessage.setCreateTime(new Date());
			msgMessage.setMessage("数据准备好了！！！");
			msgMessage.setRecBy("牛牛");
			msgMessage.setSendBy("daniu");
			msgMessage.setSendType(0);
			msgMessage.setSendStatus(0);
		//msgMessageService.insertMsgMessage(msgMessage);
		System.out.println("数据成功插入！！！");
	}
 	@Test
	public void test02(){
		//MsgMessage msgMessage =  msgMessageService.findMsgMessageByCode("5AA4A4316BA52646236930F6A9BE1AB488CB20180203093224");
		MsgMessage msgMessage = new MsgMessage();
		msgMessage.setCode("5AA4A4316BA52646236930F6A9BE1AB488CB20180203093224");
		msgMessage.setCreateTime(new Date());
	 	msgMessage.setVersion(1.3);
	 	msgMessage.setDr(2);

	 	System.out.println(msgMessageService.update(msgMessage));
	 	//System.out.println(StringUtils.objToJsonStr(msgMessage));
		//System.out.println(msgMessageService.deleteByPrimaryKey(msgMessage.getId()));
		//System.out.println(StringUtils.objToJsonStr(msgMessageService.selectAll()));
	}
	@Test
	public void test3(){
		MsgMessage msgMessage =  msgMessageService.findMsgMessageByCode("0597154CR812DR4AF3R9710R2BAFAA6E3FE120180125160922");
 		//String msg = msgMessage.getMessage();
 		System.out.println (StringUtils.objToJsonStr(msgMessage));
	}
}

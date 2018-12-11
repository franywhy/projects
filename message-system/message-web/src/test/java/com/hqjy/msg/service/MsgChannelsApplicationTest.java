package com.hqjy.msg.service;

import com.hqjy.msg.service.MsgChannelsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hqjy.msg.model.MsgChannels;
import com.hqjy.msg.util.StringUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MsgChannelsApplicationTest {


	@Autowired
	private MsgChannelsService msgChannelsService;
//	[{"id":2,"groupChannel":"11","leaguerChannel":"33","remark":"44"},{"id":3,"groupChannel":"11","leaguerChannel":"34","remark":"44"},{"id":4,"groupChannel":"11","leaguerChannel":"35","remark":"44"}]
	@Test
	public void queryData() {
		//System.out.println(StringUtils.objToJsonStr(msgChannelsService.fi));
	}
	
	@Test
	public void selcectByExample() {
		System.out.println(StringUtils.objToJsonStr(msgChannelsService.findMsgChannelsByGroup("test")));
	}
	
	@Test
	public void saveData(){
		MsgChannels msg = new MsgChannels();
	
			msg.setGroupChannel("aa");
			msg.setLeaguerChannel("bb");
			msg.setRemark("cc");
			//msgChannelsService.insertMsgChannels(msg);
		
		System.out.println("插入数据成功！！！");
	}
	
	
}

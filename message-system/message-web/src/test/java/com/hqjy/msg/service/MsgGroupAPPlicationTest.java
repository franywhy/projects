package com.hqjy.msg.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hqjy.msg.exception.DefaultException;
import com.hqjy.msg.model.MsgGroup;
import com.hqjy.msg.util.StringUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MsgGroupAPPlicationTest {

	@Autowired
	private MsgGroupService msgGroupService;
	
	//@Test
	public void findMsgGroupData(){
		System.out.println(StringUtils.objToJsonStr(msgGroupService.selectAll()));
	}
	//@Test
	public void testcheckGroup(){
		List groups = new ArrayList();
		groups.add("aa");
		groups.add("baobao");
		try {
			System.out.println(msgGroupService.checkGroup(groups));
		} catch (DefaultException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存数据
	 */
	//@Test
	public void saveMsgGroup(){
		
		MsgGroup msg = new MsgGroup();
		
			msg.setName("律师");
			msg.setChannel("55");
			msg.setRemark("lvshi");
			msg.setType(0);;
			msgGroupService.insert(msg);
		
		System.out.println("消息插入成功！！！");
	}
	/**
	 * 更新数据
	 */
	//@Test
	public void updateMsgGroup(){
		MsgGroup msg = new MsgGroup();
		msg.setId(7);
		//msg.setChannel(channel);
		
		//int id = msgGroupService.updateMsgGroup(msg);
	}
	
	/**
	 * 删除数据
	 */
	//@Test
	public void delMsgGroup(){
		Integer id = 10;
		//msgGroupService.delMsgGroup(id);
		System.out.println("删除数据成功！！！");
	}

	@Test
	public void updateGroupInfo(){
		List groups = new ArrayList();
		Map map = new HashMap();
		
		/*map.put("channel", "zikao");
		map.put("type", "+");
		groups.add(map);*/
		
		map = new HashMap();
		map.put("channel", "11");
		map.put("type", "-");
		groups.add(map);
		
		msgGroupService.updateMsgGroupInfo("150", groups);
		System.out.println("更改成功！！！");
	}


}

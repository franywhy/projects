package com.hqjy.msg.service;

import com.hqjy.msg.provide.MsgManagerService;
import com.hqjy.msg.util.Constant;
import com.hqjy.msg.util.ListUtils;
import com.hqjy.msg.util.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//@RunWith(SpringRunner.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MsgManagerApplicationTest {

	@Autowired
	private MsgManagerService msgManagerService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	//@Test
	public void sendMsgTest() throws Exception {
	
		List list = new ArrayList();
		list.add("20");
		list.add("21");
		list.add("22");
		list.add("23");
		
		List list1 = new ArrayList();
		list1.add("zikao");
		list1.add("test");
		list1.add("bao");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String d1 = sdf.format(date);
		//msgManagerService.sendMsg(list, d1, "消息已发送！", "liming", list1);
		System.out.println("成功发送消息！！！");	
	}

	//@Test
	public void runTimeTest(){
		String msgId = "510FD028M133FM4587M999FMDF9F16DB753F20180108113219";
		msgManagerService.runByTime(msgId);
		System.out.println("按时推送消息！！！");
	}
	
	//@Test
	public void testMethodGetMsgByUserId(){
		//List list = this.msgManagerService.getMsgByUserId("2018-01-01 00:00:00", "2018-01-04 00:00:00","11", Constant.MSG_STATUS_ALL);
		//System.out.println(StringUtils.objToJsonStr(list));
	}
	@Test
	public void testSetReaded(){
		//74208BEDP2F75P42C1P9260PF39ABAB3275A20180108143556
		String msgId = "E09A3C09SC92BS40C0SB0AFSC3051F09415420180110161453";
		String userId = "";
 		List groupChannels = new ArrayList();
 		groupChannels.add("bao");
		groupChannels.add("test_2");
		groupChannels.add("test_1");
		//groupChannels.add("baobao");
		groupChannels.add("baobao");

		String sql = "select leaguer_channel as `" + Constant.REDIS_KEY + "`  from msg_channels where group_channel in (" + ListUtils.listToSqlIn(groupChannels) + ")  group by leaguer_channel";
		List list = jdbcTemplate.queryForList(sql);
		Map map = null;

		for (int i = 0; i < list.size(); i++) {
			map = (Map) list.get(i);
			userId = (String) map.get(Constant.REDIS_KEY);
			//msgManagerService.setMsgReadedByUserId(msgId,userId);
		}


	}
}

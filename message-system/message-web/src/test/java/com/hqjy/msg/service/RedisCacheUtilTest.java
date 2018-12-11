package com.hqjy.msg.service;

import java.util.*;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.hqjy.msg.provide.RedisMsgService;
import com.hqjy.msg.util.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hqjy.msg.model.Objects;

import com.hqjy.msg.util.Constant;
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisCacheUtilTest {

	@Autowired
    private RedisMsgService<Objects> redisMsgService;
	
	
	//@Test
	public void test(){
		redisMsgService.setCacheObject("test"+Constant.MESSAGE_REDIS,"111");
		for (int i = 0; i < 10000; i++) {
			redisMsgService.setCacheObject("aa"+String.valueOf(i)+ Constant.WAIT_REDIS,"wait"+String.valueOf(i));
        }
        for (int i = 0; i < 10000; i++) {
			redisMsgService.setCacheObject("bb"+String.valueOf(i)+ Constant.MESSAGE_REDIS,"message"+String.valueOf(i));
        }
        for (int i = 0; i < 10000; i++) {
			redisMsgService.setCacheObject("cc"+String.valueOf(i)+ Constant.USER_REDIS,"user"+String.valueOf(i));
        }
        System.out.println("测试成功！！");
	}
	//测试实体类
	//@Test
	public void test1(){
		
		User user = new User();
		user.setAge(19);
		user.setSex("男");
		user.setUsername("lisi");
		//redisCacheUtil.setCacheObject("test"+Constant.MESSAGE_REDIS, 11);
		redisMsgService.setCacheObject("test"+Constant.MESSAGE_REDIS,user);
		Object value = redisMsgService.getCacheObject("test"+Constant.MESSAGE_REDIS);
		System.out.println(value.toString());
	}
	//测试缓存的list集合
	//@Test
	public void test2(){
		List list = new ArrayList();
			list.add("10");
			list.add("11");
			list.add("12");
			list.add("13");
		redisMsgService.setCacheList("test"+ Constant.WAIT_REDIS, list);
		List<Object> value = redisMsgService.getCacheList("test"+ Constant.WAIT_REDIS);
		System.out.println(value);
		}
	
	//测试缓存的list集合&过期时间
	//@Test
	public void test03(){
		List<User> list = new ArrayList<User>();
		User user1 = new User();
		user1.setAge(19);
		user1.setSex("男");
		user1.setUsername("lisi");
		
		User user2 = new User();
		user2.setAge(17);
		user2.setSex("女");
		user2.setUsername("huahua");
		list.add(user1);
		list.add(user2);

		redisMsgService.setCacheList("test"+ Constant.WAIT_REDIS, list,19284140);
	List<Object> value = redisMsgService.getCacheList("test"+ Constant.WAIT_REDIS);
	System.out.println(value);
	}

	//测试缓存的set集合
	
	//@Test
	public void test06(){
		Set dataSet = new HashSet();
		dataSet.add("111");

		redisMsgService.setCacheSet("aa"+Constant.MESSAGE_REDIS, dataSet);
		Set<Objects> value = redisMsgService.getCacheSet("aa"+Constant.MESSAGE_REDIS);
		System.out.println(value);
	}
	@Test
	public void test07(){
		/*Map map = redisMsgService.getCacheMap("channel"+Constant.CHANNEL_REDIS);
		System.out.println(StringUtils.objToJsonStr(map));*/
		/*Map map = new HashMap();
		map.put("test","test");
		redisMsgService.setCacheMap("channel"+Constant.CHANNEL_REDIS,map,-1);*/
		System.out.println(StringUtils.objToJsonStr(redisMsgService.getCacheMap("msg"+Constant.MESSAGE_REDIS)
		));
	}

	@Test
	public void test08(){
		/*Map map = redisMsgService.getCacheMap("channel"+Constant.CHANNEL_REDIS);
		System.out.println(StringUtils.objToJsonStr(map));*/
		/*Map map = new HashMap();
		map.put("test","test");
		redisMsgService.setCacheMap("channel"+Constant.CHANNEL_REDIS,map,-1);*/
		List list = new ArrayList();
		list.add("test_2"+Constant.CHANNEL_REDIS);
		//list.add("test"+Constant.CHANNEL_REDIS);
		redisMsgService.setsIntoCacheZSet("baobao"+Constant.CHANNEL_REDIS,list);
	}

}

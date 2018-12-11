package com.school.accountant.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.school.accountant.service.UserService;
import com.school.accountant.util.RedisUtil;
import com.school.accountant.vo.SSOResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/spring-*.xml"})
@ActiveProfiles("dev")
public class TestSpringAndMybatis {

	/**************测试 Spring与mybatis集成 START**************/
	/*@Autowired
	private UserService userService;
	
	@Test
	public void testGetUserById(){
		User user = userService.getUserById(1);
		System.out.println(user.getId());
	}*/
	/**************测试 Spring与mybatis集成 END**************/
	
	/**************测试 redis START**************/
	/*@Autowired
	private RedisUtil redisUtil;
	
	@Test
	public void testRedis(){
		redisUtil.set("test01", "test01 test01 test01 test01 test01");
		String value = redisUtil.get("test01");
		System.out.println(value);
	}*/
	/**************测试 redis END**************/
	
	@Autowired
	private UserService loginService;
	@Test
	public void testLogin(){
		SSOResult result = loginService.login("13824429749", "1234567", 100);
		System.out.println(result.getCode());
	}
	
	
}
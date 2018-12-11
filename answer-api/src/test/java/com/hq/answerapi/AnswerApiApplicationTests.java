package com.hq.answerapi;

import com.hq.answerapi.dao.xy.UserRepository;
import com.hq.answerapi.dao.xyqquser.QQUserRepository;
import com.hq.answerapi.entity.QQUser;
import com.hq.answerapi.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AnswerApiApplicationTests {

	@Autowired
	private QQUserRepository qqUserRepositery;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MongoTemplate xyMongoTemplate;
	@Autowired
	private MongoTemplate xyQqUserMongoTemplate;

	@Test
	public void contextLoads() {
		List<QQUser> list = qqUserRepositery.findByUsername("15622168302");
		System.out.println(list.get(0).getNickName());
		User user = userRepository.findOne(Long.parseLong("11196929"));
		xyMongoTemplate.getCollection("users");
		xyQqUserMongoTemplate.getCollection("qQUser");
	}

}

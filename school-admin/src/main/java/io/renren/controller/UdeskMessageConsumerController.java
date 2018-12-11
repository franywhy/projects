package io.renren.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;

import io.renren.service.SysUserService;
import io.renren.utils.SendUdeskUtil;

@Controller
public class UdeskMessageConsumerController implements ChannelAwareMessageListener {
	protected Logger logger = LoggerFactory.getLogger(getClass());
//	@Autowired
//	private DataSourceTransactionManager transactionManager;
	@Autowired
	private SysUserService sysUserService;
	
	@Override
	@Transactional
	public void onMessage(Message message, Channel channel) throws Exception {
		logger.info("同步Udesk任务收到队列信息" + " [x] Received '" + new String(message.getBody(), "UTF-8") + "'");
//		TransactionStatus status = null;
//		// 开启事务
//		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务
//		status = transactionManager.getTransaction(def); // 获得事务状态
		String body = new String(message.getBody(), "UTF-8");
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		try {
			Gson gson = new Gson();
			Map<String, Object> mapUdesk = gson.fromJson(body, HashMap.class); 
			String nick_Name = (String) mapUdesk.get("nick_Name");
			String phone = (String) mapUdesk.get("phone");
			String tags = (String) mapUdesk.get("tags");
			Double ownerIdD = (Double) mapUdesk.get("ownerId");
			Integer ownerIdInt = ownerIdD.intValue();
			Integer ownerId = ownerIdInt;
			String payTime = (String) mapUdesk.get("payTime");
			String province = (String) mapUdesk.get("province");
			String level = (String) mapUdesk.get("level");
			String className = (String) mapUdesk.get("className");
			String schoolName = (String) mapUdesk.get("schoolName");
			String profession = (String) mapUdesk.get("profession");
			String commodity = (String) mapUdesk.get("commodity");
			Thread.sleep(1000L);
			SendUdeskUtil.creatUdeskCustomer(nick_Name,phone,tags,ownerId,payTime,province,level,className,schoolName,profession,commodity);
			SendUdeskUtil.updateUdeskCustomer(nick_Name,phone,tags,ownerId,payTime,province,level,className,schoolName,profession,commodity);
		} catch (Exception e) {
//			try {
//				// 回滚
//				transactionManager.rollback(status);
//	            e.printStackTrace();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
		} finally{
			try {
			//	transactionManager.commit(status);
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}

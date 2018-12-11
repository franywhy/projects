package io.renren.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;

import io.renren.entity.CourseUserplanClassEntity;
import io.renren.entity.CourseUserplanEntity;
import io.renren.entity.MallOrderEntity;
import io.renren.entity.MallOrderSyncEntity;
import io.renren.entity.OrderMessageConsumerEntity;
import io.renren.entity.SysDeptEntity;
import io.renren.entity.UsersEntity;
import io.renren.rest.persistent.KGS;
import io.renren.service.CourseUserplanClassService;
import io.renren.service.CourseUserplanService;
import io.renren.service.MallOrderService;
import io.renren.service.MallOrderSyncService;
import io.renren.service.SysDeptService;
import io.renren.service.UsersService;
import io.renren.utils.DateUtils;

@Controller
public class OrderMessageConsumerUpdateController implements ChannelAwareMessageListener {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	KGS orderNoKGS;
	@Resource
	KGS userKGS;
	@Autowired
	private MallOrderService mallOrderService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private CourseUserplanService courseUserplanService;
	@Autowired
	private CourseUserplanClassService courseUserplanClassService; 

	@Autowired
	private DataSourceTransactionManager transactionManager;

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		//System.out.println("收到-更新昵称,部门信息-队列信息" + " [x] Received '" + new String(message.getBody(), "UTF-8") + "'");
		TransactionStatus status = null;
		UsersEntity user=null;
		OrderMessageConsumerEntity order = null;
		//开启事务
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务
		status = transactionManager.getTransaction(def); // 获得事务状态
		
		String body = null;
		try {
			body = new String(message.getBody(), "UTF-8");
			Gson gson = new Gson();
			order = gson.fromJson(body, OrderMessageConsumerEntity.class);
			
			if(mallOrderService.queryOrderExist(null, order.getCode(), order.getOrder_no()) > 0){
				//查询对应校区
				SysDeptEntity deptEntity = sysDeptService.queryObjectByNcId(order.getNc_school_pk());
				if (null != deptEntity) {
					Map<String,Object> mapOrder = new HashMap<>();
					mapOrder.put("orderNo", order.getOrder_no());
					MallOrderEntity orderEntity = mallOrderService.queryObject(mapOrder);
					//更新用户real_name
					//查询用户
					user = usersService.queryObject(orderEntity.getUserId());
					if(null != user){
						user.setRealName(order.getUser_name());
						user.setDeptId(deptEntity.getDeptId());
						usersService.update(user);
					}else{
						//没有找到对应学员
					}
					//更新订单表
					orderEntity.setDeptId(deptEntity.getDeptId());
					mallOrderService.update(orderEntity);
					//更新学员规划
					CourseUserplanEntity userplanEntity = courseUserplanService.queryUserplanObjectByOrderId(orderEntity.getOrderId());
					if(null != userplanEntity){
						userplanEntity.setDeptId(deptEntity.getDeptId());
						courseUserplanService.update(userplanEntity);
						//更新学习计划
						Map<String,Object> mapUserplanClass = new HashMap<>();
						mapUserplanClass.put("userplanId", userplanEntity.getUserPlanId());
						List<CourseUserplanClassEntity> userplanClassList = courseUserplanClassService.queryList(mapUserplanClass);
						if(null != userplanClassList && !userplanClassList.isEmpty()){
							for(CourseUserplanClassEntity entity : userplanClassList){
								entity.setDeptId(deptEntity.getDeptId());
								courseUserplanClassService.update(entity);
							}
						}else{
							logger.error("queue OrderMessageConsumerUpdate : no userplanClass, body:{}",body);
						}
					}else{
						logger.error("queue OrderMessageConsumerUpdate : no userplan, body:{}",body);
					}
				}else{
					logger.error("queue OrderMessageConsumerUpdate : no dept, body:{}",body);
				}
			}
		} catch (Exception e) {
			try {
				e.printStackTrace();
				transactionManager.rollback(status);
				logger.error("queue OrderMessageConsumerUpdate error : {}",e.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
				//如果保存报错日志出错finally不会被执行，需要在此确认消费消息
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				logger.error("queue OrderMessageConsumerUpdate error : {}",e1.toString());
			}
		} finally {
			try {
				// 提交事务
				transactionManager.commit(status);
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				//System.out.println("over!");
			} catch (Exception e) {
				e.printStackTrace();
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				logger.error("queue OrderMessageConsumerUpdate error : {}",e.toString());
			}
		}
	}
}

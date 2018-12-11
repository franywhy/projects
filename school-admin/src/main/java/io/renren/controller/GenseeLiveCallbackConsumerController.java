package io.renren.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.entity.ReplayCallbackDetailEntity;
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

import io.renren.entity.LiveCallbackDetailEntity;
import io.renren.pojo.log.LiveCallbackPOJO;
import io.renren.service.GenseeCallbackService;
import io.renren.utils.DateUtils;

@Controller
public class GenseeLiveCallbackConsumerController implements ChannelAwareMessageListener {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	@Autowired
	private GenseeCallbackService genseeCallbackService;

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		TransactionStatus status = null;
		LiveCallbackPOJO callback = null;
		//开启事务
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务
		status = transactionManager.getTransaction(def); // 获得事务状态
		
		String body = null;
		try {
			body = new String(message.getBody(), "UTF-8");
			Gson gson = new Gson();
			callback = gson.fromJson(body, LiveCallbackPOJO.class);
			if(null != callback){
				if(callback.getType() == 0){//type==0为进入
					LiveCallbackDetailEntity entity = new LiveCallbackDetailEntity();
					entity.setLiveId(callback.getLiveId());
					entity.setUserId(callback.getUserId());
					entity.setJoinTime(callback.getCreateTime());
					entity.setCreateTime(new Date());
					genseeCallbackService.saveLiveCallbackDetail(entity);
				}else if(callback.getType() == 1){//type==1为退出
					Map<String,Object> map = new HashMap<>();
					map.put("liveId", callback.getLiveId());
					map.put("userId", callback.getUserId());
					map.put("startTime", DateUtils.parse(DateUtils.format(new Date())+" 00:00:00",DateUtils.DATE_TIME_PATTERN).getTime());
					map.put("endTime", DateUtils.parse(DateUtils.format(new Date())+" 23:59:59",DateUtils.DATE_TIME_PATTERN).getTime());
					List<LiveCallbackDetailEntity> joinList = genseeCallbackService.queryLiveJoinDetail(map);
					if(null != joinList && !joinList.isEmpty()){
						LiveCallbackDetailEntity entity = joinList.get(0);
						entity.setLeaveTime(callback.getCreateTime());
						genseeCallbackService.updateLiveCallbackDetail(entity);
						if(joinList.size() > 1){//如果查询离开时间为NULL的数据有多条,则补录之前的离开时间
							Long lastLeaveTime = null;
							for(int i=1;joinList.size()>i;i++){
								lastLeaveTime = entity.getJoinTime();
								entity = joinList.get(i);
								entity.setLeaveTime(lastLeaveTime);
								genseeCallbackService.updateLiveCallbackDetail(entity);
							}
						}
					}else {
                        List<LiveCallbackDetailEntity> leaveList = genseeCallbackService.queryLiveLeaveDetail(map);
                        if(null != leaveList && !leaveList.isEmpty()){
                            LiveCallbackDetailEntity entity = leaveList.get(0);
                            LiveCallbackDetailEntity newEntity = new LiveCallbackDetailEntity();
                            newEntity.setCreateTime(new Date());
                            newEntity.setJoinTime(entity.getLeaveTime());
                            newEntity.setLeaveTime(callback.getCreateTime());
                            newEntity.setUserId(entity.getUserId());
                            newEntity.setLiveId(entity.getLiveId());
                            genseeCallbackService.saveLiveCallbackDetail(newEntity);
                        }
                    }
				}else if(callback.getType() == 2) {
                    LiveCallbackDetailEntity entity = new LiveCallbackDetailEntity();
                    entity.setLiveId(callback.getLiveId());
                    entity.setLeaveTime(callback.getCreateTime());
                    entity.setCreateTime(new Date());
                    genseeCallbackService.saveLiveCallbackDetail(entity);
                }
			}
		} catch (Exception e) {
			try {
				e.printStackTrace();
				transactionManager.rollback(status);
				logger.error("queue GenseeLiveCallback error : {}",e.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
				//如果保存报错日志出错finally不会被执行，需要在此确认消费消息
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				logger.error("queue GenseeLiveCallback error : {}",e1.toString());
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
				logger.error("queue GenseeLiveCallback error : {}",e.toString());
			}
		}
	}
}

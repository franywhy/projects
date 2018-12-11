package io.renren.controller;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import io.renren.entity.ReplayCallbackDetailEntity;
import io.renren.pojo.log.ReplayCallbackPOJO;
import io.renren.service.GenseeCallbackService;
import io.renren.service.SysProductService;
import io.renren.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class GenseeReplayCallbackConsumerController implements ChannelAwareMessageListener {
	protected Logger logger = LoggerFactory.getLogger(getClass());


	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	@Autowired
    private GenseeCallbackService genseeCallbackService;
	@Autowired
	private SysProductService sysProductService;


	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		TransactionStatus status = null;
        ReplayCallbackPOJO callback = null;
		//开启事务
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 事物隔离级别，开启新事务
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 获得事务状态
		status = transactionManager.getTransaction(def);
		String body = null;
		try {
			body = new String(message.getBody(), "UTF-8");
			Gson gson = new Gson();
			callback = gson.fromJson(body, ReplayCallbackPOJO.class);
			if(null != callback) {
                //是否离线  0离线(缓存)  1回放回调
                //type:时间的类型 0进入 1退出 2离线(有进入和退出时间)
                //根据videoId 查询课次的productId;
                Long productId = genseeCallbackService.queryProductIdByVideoId(callback.getVideoId());
                //根据productId查询系数
                Float coefficient = this.sysProductService.queryCoefficient(productId);
                
                if (callback.getType() == 2) {
                    //2离线(有进入和退出时间) 直接保存
                    ReplayCallbackDetailEntity entity = new ReplayCallbackDetailEntity();
                    entity.setCreateTime(new Date());
                    entity.setIsOfflive(callback.getIsOfflive());
                    entity.setJoinTime(callback.getJoinTime());
                    entity.setLeaveTime(callback.getLeaveTime());
                    entity.setUserId(callback.getUserId());
                    entity.setVideoEndTime(callback.getVideoEndTime());
                    entity.setVideoId(callback.getVideoId());
                    entity.setVideoStartTime(callback.getVideoStartTime());
                    entity.setVideoTotalTime(callback.getVideoTotalTime());
                    entity.setProductId(productId);
                    entity.setCoefficient(coefficient);
                    genseeCallbackService.saveReplayCallbackDetail(entity);
                } else if (callback.getType() == 0){
                    //type==0为进入,进入时间就是回调创建时间
                        ReplayCallbackDetailEntity entity = new ReplayCallbackDetailEntity();
                        entity.setCreateTime(new Date());
                        entity.setIsOfflive(callback.getIsOfflive());
                        entity.setJoinTime(callback.getCreateTime());
                        entity.setUserId(callback.getUserId());
                        entity.setVideoId(callback.getVideoId());
                        entity.setProductId(productId);
                        entity.setCoefficient(coefficient);
                        genseeCallbackService.saveReplayCallbackDetail(entity);
                }else if(callback.getType() == 1){
                        //type==1为退出
                        Map<String,Object> map = new HashMap<>();
                        map.put("videoId", callback.getVideoId());
                        map.put("userId", callback.getUserId());
                        map.put("startTime", DateUtils.parse(DateUtils.format(new Date())+" 00:00:00",DateUtils.DATE_TIME_PATTERN).getTime());
                        map.put("endTime", DateUtils.parse(DateUtils.format(new Date())+" 23:59:59",DateUtils.DATE_TIME_PATTERN).getTime());
                        //查询当天有没有 离开时间为空的记录
                        List<ReplayCallbackDetailEntity> joinList = genseeCallbackService.queryReplayJoinDetail(map);
                        if(null != joinList && !joinList.isEmpty()){
                            ReplayCallbackDetailEntity entity = joinList.get(0);
                            entity.setLeaveTime(callback.getCreateTime());
                            genseeCallbackService.updateReplayCallbackDetail(entity);
                            if(joinList.size() > 1){
                                //如果查询离开时间为NULL的数据有多条,则补录之前的离开时间
                                Long lastLeaveTime = null;
                                for(int i=1;joinList.size()>i;i++){
                                    lastLeaveTime = entity.getJoinTime();
                                    entity = joinList.get(i);
                                    entity.setLeaveTime(lastLeaveTime);
                                    entity.setCoefficient(coefficient);
                                    genseeCallbackService.updateReplayCallbackDetail(entity);
                                }
                            }
                        }else {
                            //查询当天有没有 进入和退出时间不为空的记录
                            List<ReplayCallbackDetailEntity> leaveList =  genseeCallbackService.queryReplayLeaveDetail(map);
                            if(null != leaveList && !leaveList.isEmpty()){
                                ReplayCallbackDetailEntity entity = leaveList.get(0);
                                ReplayCallbackDetailEntity newEntity = new ReplayCallbackDetailEntity();
                                newEntity.setCreateTime(new Date());
                                newEntity.setIsOfflive(entity.getIsOfflive());
                                newEntity.setJoinTime(entity.getLeaveTime());
                                newEntity.setLeaveTime(callback.getCreateTime());
                                newEntity.setUserId(entity.getUserId());
                                newEntity.setVideoId(entity.getVideoId());
                                newEntity.setProductId(entity.getProductId());
                                newEntity.setCoefficient(entity.getCoefficient());
                                genseeCallbackService.saveReplayCallbackDetail(newEntity);
                            }
                        }
                }
            }
		} catch (Exception e) {
			try {
				e.printStackTrace();
				transactionManager.rollback(status);
				logger.error("queue GenseeReplayCallback error : {}",e.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
				//如果保存报错日志出错finally不会被执行，需要在此确认消费消息
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				logger.error("queue GenseeReplayCallback error : {}",e1.toString());
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
				logger.error("queue GenseeReplayCallback error : {}",e.toString());
			}
		}
	}
}

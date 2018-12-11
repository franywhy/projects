package io.renren.controller;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import io.renren.entity.NcCourseClassplanEntity;
import io.renren.entity.NcCourseClassplanLogEntity;
import io.renren.entity.UsersEntity;
import io.renren.service.NcCourseClassplanLogService;
import io.renren.service.NcCourseClassplanService;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DL on 2018/8/21.
 */
@Controller
public class NCClassUserPlanMessageConsumerController implements ChannelAwareMessageListener{

    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private NcCourseClassplanService courseClassplanService;

    @Autowired
    private NcCourseClassplanLogService courseClassplanLogService;

    @Autowired
    private DataSourceTransactionManager transactionManager;


    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        logger.error("nc open userplanClass message" + " [x] Received '" + new String(message.getBody(), "UTF-8") + "'");
        NcCourseClassplanEntity entity = new NcCourseClassplanEntity();
        TransactionStatus status = null;//初始化状态
        /**
         * 开启事务
         */
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务
        status = transactionManager.getTransaction(def); // 获得事务状态
        String body = new String(message.getBody(), "UTF-8");//获取消息体
        try {
            Gson gson = new Gson();
            entity = gson.fromJson(body, NcCourseClassplanEntity.class);//将消息体转成map
            entity.setIsSuccess(0);
            entity.setCreateTime(new Date());
            courseClassplanService.save(entity);
        } catch (Exception e) {
            try {
                transactionManager.rollback(status);//回滚
                NcCourseClassplanLogEntity logEntity = new NcCourseClassplanLogEntity();
                logEntity.setCreateTime(new Date());
                logEntity.setNcJson(body);
                courseClassplanLogService.save(logEntity);
                e.printStackTrace();
                logger.error("NCClassUserPlanMessageConsumerController onMessage error Cause by:{}, message body=[{}]", e,body);
            } catch (Exception e2) {
                e2.printStackTrace();
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        } finally{
            try {
                transactionManager.commit(status);// 提交事务
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (Exception e) {
                logger.error("finally,事务提交出错! exception message:{}, message body=[{}]", e,body);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        }
    }
}

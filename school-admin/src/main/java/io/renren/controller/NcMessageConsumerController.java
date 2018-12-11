package io.renren.controller;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import io.renren.entity.UsersEntity;
import io.renren.rest.persistent.KGS;
import io.renren.service.UsersService;
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

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class NcMessageConsumerController implements ChannelAwareMessageListener {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    KGS userKGS;
    @Autowired
    private DataSourceTransactionManager transactionManager;
    //默认密码123456
    private static String PSW = "afa651c0a832371d479f8131271e20cc";
    @Autowired
    private UsersService usersService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        logger.info("开通员工学员账号任务收到队列信息" + " [x] Received '" + new String(message.getBody(), "UTF-8") + "'");
        UsersEntity user = new UsersEntity(); //学员实体类
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
            Map<String, Object> mapBody = gson.fromJson(body, HashMap.class);//将消息体转成map
            String nickName = ((String) mapBody.get("user_name")).trim();
            String mobile = ((String) mapBody.get("mobile")).trim();

            //根据电话号码判断用户是否存在
            Map<String, Object> cmap = new HashMap<>();
            cmap.put("mobile", mobile);
            if (!this.usersService.mobileExist(cmap)) { //用户不存在则创建用户
                user.setUserId((long) userKGS.nextId());
                user.setMobile(mobile);
                user.setNickName(nickName);
                user.setPic("http://zikao.hqjy.com/Avatar.png");
                user.setPassword(PSW);
                user.setSex(2);
                user.setCreationTime(new Date());
                user.setChannel(2);
                logger.info("create EmployeesUser: {}", user);
                usersService.save(user);
            }
        } catch (Exception e) {
            try {
                transactionManager.rollback(status);//回滚
                e.printStackTrace();
                logger.error(" NcMessageConsumerController onMessage error Cause by:{}, message body=[{}]", e, body);
            } catch (Exception e2) {
                e2.printStackTrace();
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        } finally {
            try {
                transactionManager.commit(status);// 提交事务
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (Exception e) {
                logger.error(" Nc数据事务提交出错! exception message:{}, message body=[{}]", e, body);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        }
    }

}

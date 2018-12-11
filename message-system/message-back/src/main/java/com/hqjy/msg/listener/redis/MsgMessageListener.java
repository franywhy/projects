package com.hqjy.msg.listener.redis;

import com.hqjy.msg.exception.DefaultException;
import com.hqjy.msg.provide.MsgManagerService;
import com.hqjy.msg.util.Constant;
import com.hqjy.msg.util.ThreadPoolExecutorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2017/11/22 0022.
 */
@Component
public class MsgMessageListener implements MessageListener {

    @Autowired
    private MsgManagerService msgManagerService;

    //ThreadPoolExecutor poolExecutor = ThreadPoolExecutorUtils.getDefaultThreadPoolExecutor();

    @Override
    public void onMessage(Message message, byte[] pattern) {// 客户端监听订阅的topic，当有消息的时候，会触发该方法

        byte[] body = message.getBody();// 请使用valueSerializer
        byte[] channel = message.getChannel();
        String topic = new String(channel);
        String itemValue = new String(body);
        // 请参考配置文件，本例中key，value的序列化方式均为string。
        String msgId = itemValue.split(Constant.MESSAGE_REDIS)[0];
        System.out.println("过期的消息msgId:"+msgId);
        //执行消息的过期操作

                try {
                    msgManagerService.removeMsg(msgId);
                } catch (DefaultException e) {
                    //e.printStackTrace();
                }
    }
}
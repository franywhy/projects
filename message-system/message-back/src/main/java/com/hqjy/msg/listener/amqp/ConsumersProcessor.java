package com.hqjy.msg.listener.amqp;

import com.hqjy.msg.model.Objects;
import com.hqjy.msg.provide.RedisMsgService;
import com.hqjy.msg.util.Constant;
import com.hqjy.msg.util.DateUtils;
import com.hqjy.msg.util.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/5 0005.
 */
@Component

public class ConsumersProcessor {
    @Autowired
    private RedisMsgService<Objects> redisMsgService;
    @RabbitListener(queues = Constant.QUEUE_CONSUMER)
    //@RabbitHandler
    public void consumer1(String message)throws Exception {
        invoke((String) message);
    }
    @RabbitListener(queues = Constant.QUEUE_CONSUMER)
    //@RabbitHandler
    public void consumer2(String message)throws Exception {
        invoke((String) message);
    }
    /*@RabbitListener(queues = Constant.QUEUE_CONSUMER)
    //@RabbitHandler
    public void consumer3(String message)throws Exception {
        invoke((String) message);
    }*/

    /*@RabbitListener(queues = Constant.QUEUE_CONSUMER)
    //@RabbitHandler
    public void consumer4(String message)throws Exception {
        invoke((String) message);
    }*/

    private void invoke(String str){
        System.out.println("msg into redis");
        Map map = (Map) StringUtils.strToObj(str, Map.class);
        List list = (List) map.get("list");
        String msgId = (String)map.get("msg_id");

        String sendTime = (String) map.get("send_time");
        long time = DateUtils.stringToDate(sendTime,DateUtils.DATE_FORMAT).getTime();
        for (int i = 0; i < list.size(); i++) {
            Map maps =  (Map) list.get(i);
            maps.put("value",msgId);
            maps.put("score",time);

        }
               /* ThreadPoolExecutorUtils.getRedisThreadPoolExecutor().execute(()->{
                    messageRunService.saveRelationBetweenUserAndMsg(msgId,(List)list.stream().map(p->{
                        return ((Map)p).get(Constant.REDIS_KEY);
                    }).collect(Collectors.toList()));
                });*/
        try {
            redisMsgService.pipeline(msgId+Constant.USER_REDIS,list);

            Thread.sleep(3000);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }
}

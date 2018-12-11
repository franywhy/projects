package com.hqjy.msg.listener.amqp;

import com.hqjy.msg.model.Objects;
import com.hqjy.msg.provide.MessageRunService;
import com.hqjy.msg.provide.RedisMsgService;
import com.hqjy.msg.util.Constant;
import com.hqjy.msg.util.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/4/2 0002.
 */
@Component
public class UpdateGroupProcessor {

    @Autowired
    private RedisMsgService<Objects> redisMsgService;

    @Autowired
    @Qualifier("redis")
    private MessageRunService messageRunService;

    public void updateGroupVersion(String message)throws Exception {
        Map map = (Map) StringUtils.strToObj(message, Map.class);
        List channels = (List) StringUtils.fromJson((String) map.get("groups"),List.class) ;
        DecimalFormat df = new DecimalFormat("0");
        channels = (List) channels.stream().map(p->{
            Map sorts = (Map)p;
            Double dscore  = new Double(String.valueOf(sorts.get("score")));
            Object test = df.format(dscore);
            long score  = Long.valueOf((String)test);
            sorts.put("score",score);
            return sorts;
        }).collect(Collectors.toList());
        String channel = (String) map.get("channel");
        //messageRunService.updateMsgGroupInfo(userId,groups);
        redisMsgService.pipeline(channel+Constant.CHANNEL_REDIS,channels);

    }


    public void updateGroup(String message)throws Exception {
        Map map = (Map) StringUtils.strToObj(message, Map.class);
        List groups = (List) StringUtils.strToObj((String) map.get("groups"),List.class) ;
        String userId = (String) map.get("user_id");
        messageRunService.updateMsgGroupInfo(userId,groups);
        //redisMsgService.pipeline(channel+Constant.CHANNEL_REDIS,channels);

    }

    @RabbitListener(queues = Constant.QUEUE_UPDATE_GROUP)
    public void updateGroup1(String message)throws Exception {
        updateGroup(message);

    }

    @RabbitListener(queues = Constant.QUEUE_UPDATE_GROUP)
    public void updateGroup2(String message)throws Exception {
        updateGroup(message);

    }

    @RabbitListener(queues = Constant.QUEUE_UPDATE_GROUP)
    public void updateGroup3(String message)throws Exception {
        updateGroup(message);

    }

    @RabbitListener(queues = Constant.QUEUE_UPDATE_GROUP)
    public void updateGroup4(String message)throws Exception {
        updateGroup(message);

    }

    @RabbitListener(queues = Constant.QUEUE_UPDATE_GROUP)
    public void updateGroup5(String message)throws Exception {
        updateGroup(message);

    }

    @RabbitListener(queues = Constant.QUEUE_UPDATE_GROUP_RECORD)
    public void updateGroupVersion1(String message)throws Exception {
        updateGroupVersion(message);

    }

    @RabbitListener(queues = Constant.QUEUE_UPDATE_GROUP_RECORD)
    public void updateGroupVersion2(String message)throws Exception {
        updateGroupVersion(message);

    }

    @RabbitListener(queues = Constant.QUEUE_UPDATE_GROUP_RECORD)
    public void updateGroupVersion3(String message)throws Exception {
        updateGroupVersion(message);

    }

    @RabbitListener(queues = Constant.QUEUE_UPDATE_GROUP_RECORD)
    public void updateGroupVersion4(String message)throws Exception {
        updateGroupVersion(message);

    }

}

package com.hqjy.msg.listener.amqp;

import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.provide.MessageRunService;
import com.hqjy.msg.util.Constant;
import com.hqjy.msg.util.ListUtils;
import com.hqjy.msg.util.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/23 0023.
 */
@Component
public class SyncProcessor {
    @Autowired
    @Qualifier("mysql")
    private MessageRunService mysqlMessageRunService;

    @Autowired
    @Qualifier("redis")
    private MessageRunService reidsMessageRunService;
    @RabbitListener(queues = Constant.QUEUE_SYNC_REDIS_TO_MYSQL)
    public void msgRedisToMysql1(String result)throws Exception {

        //process(result);
    }

   /* @RabbitListener(queues = Constant.QUEUE_SYNC_REDIS_TO_MYSQL)
    public void msgRedisToMysql2(String result)throws Exception {

        process(result);
    }*/

    private void process(String result){
        Map map = (Map) StringUtils.strToObj(result, Map.class);
        String msgId  = (String) map.get("msg_id");
        boolean isFinish = false;
        try {
            MsgMessage message = reidsMessageRunService.findMsgByMsgIdWithNoDr(msgId);
            if (null == message) {
                return;
            }
            if (message.getDr()==Constant.MSG_SEND_STATUS_NOT && message.getSendStatus() == Constant.MSG_SEND_STATUS_NOT) {
                return;
            }
            //每次每條消息更新50000
            List users = reidsMessageRunService.getUsersByMsg(msgId);
            ListUtils.listsToArray(users, 100000).forEach(pp->{
                ListUtils.listsToArray((List)pp, 12000).stream().forEach(ppp->{
                    mysqlMessageRunService.saveMsgDetail(message,(List)ppp,reidsMessageRunService);
                });
            });
            isFinish = true;

        }catch (Exception e){
            return;
        }
        if (isFinish) {
            ///reidsMessageRunService.removeKey(msgId+Constant.MSG_USER_REDIS+Constant.CHANNEL_REDIS);
            reidsMessageRunService.removeZset("detail"+Constant.CHANNEL_REDIS, Arrays.asList(msgId+Constant.MSG_INTO_MQ));
        }
    }
}

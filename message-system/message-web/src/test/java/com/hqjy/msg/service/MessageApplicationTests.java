package com.hqjy.msg.service;

import com.hqjy.msg.interfaces.Aggregator;
import com.hqjy.msg.interfaces.impl.CountAggregator;
import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.provide.GetObjectFromRedisService;
import com.hqjy.msg.provide.MessageRunService;
import com.hqjy.msg.util.Constant;
import com.hqjy.msg.util.DateUtils;
import com.hqjy.msg.util.GroupUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hqjy.msg.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageApplicationTests {

    @Autowired
    private GetObjectFromRedisService getObjectFromRedisService;

    @Autowired
    @Qualifier("redis")
    private MessageRunService messageRunService;
    @Test
    public void test(){
        List list = getObjectFromRedisService.getConsumerMsgFromRedis("10010930", "2018-01-01 00:00:00", "2018-04-01 00:00:00");
        List redisResults = null;
        Random random = new Random();
        try {
            redisResults = (List) list.stream().map(p -> {
                String msgId = (String) p;
                Integer isReaded = 0;
                if (msgId.startsWith(Constant.READED_START_KEY)) {
                    msgId = msgId.split(Constant.READED_START_KEY)[1];
                    isReaded = 1;
                }
                MsgMessage msg = messageRunService.findMsgByMsgId(msgId);
                if (msg != null) {
                    msg.setIsReaded(isReaded);
                    msg.setSendTimeStr(DateUtils.dateToString(msg.getSendTime(),"yyyyMMdd"));
                }else{
                    msg = new MsgMessage();
                    int day = random.nextInt(3);
                    msg.setSendTimeStr(DateUtils.dateToString(DateUtils.addDays(DateUtils.getNowDate(),day),"yyyyMMdd"));
                    msg.setMessage("2222222222222");

                }


                return msg;

            }).collect(Collectors.toList());

        Map<String, Aggregator<MsgMessage>> aggregatorMap = new HashMap<>();
        aggregatorMap.put("count", new CountAggregator<MsgMessage>());
        Map<Object, Map<String, Object>> aggResults = GroupUtils.groupByProperty(redisResults, MsgMessage.class, "sendTimeStr", aggregatorMap);

        Map resultmap = new HashMap();
        List resultlist = new ArrayList();
        for (Object key : aggResults.keySet()) {
            resultmap = new HashMap();
            Map<String, Object> results = aggResults.get(key);
            resultmap.put("date",key);
            //System.out.println("Key:" + key+"->"+results.keySet().size());
            for (String aggKey : results.keySet()) {
                //System.out.println("     aggkey->" + results.get(aggKey));
                resultmap.put("count",results.get(aggKey));
            }
            resultlist.add(resultmap);

        }
            System.out.print(StringUtils.objToJsonStr(resultlist));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

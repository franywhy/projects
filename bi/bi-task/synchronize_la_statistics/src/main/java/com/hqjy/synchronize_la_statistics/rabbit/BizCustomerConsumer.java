package com.hqjy.synchronize_la_statistics.rabbit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hqjy.synchronize_la_statistics.constant.RabbitConst;
import com.hqjy.synchronize_la_statistics.entity.BiLiveAnswerRecord;
import com.hqjy.synchronize_la_statistics.service.BiLiveAnswerRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author zhouyibin
 * @date 2018/12/13
 */
@Component
@Slf4j
public class BizCustomerConsumer {

    private final BiLiveAnswerRecordService service;

    @Autowired
    public BizCustomerConsumer(BiLiveAnswerRecordService service) {
        this.service = service;
    }

    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue(value = RabbitConst.BI_LIVE_ANSWER_RECORD_QUEUE, durable = RabbitConst.TRUE, exclusive = RabbitConst.FALSE, autoDelete = RabbitConst.FALSE),
                            exchange = @Exchange(value = RabbitConst.BI_LIVE_ANSWER_RECORD_QUEUE_EXCHANGE, durable = RabbitConst.TRUE)
                    )
            }
    )
    public void process(String message) {
        try {
            log.info("BI同步mongoDB数据===>" + message);
            //转换成json对象
            JSONObject la = JSON.parseObject(message);
            Long questionCount = la.getLong("questionCount");
            Long answerCount = la.getLong("answerCount");
            String classPlanLiveId = la.getString("classplanLiveId");
            //如果数据已存在
            if (0!=service.total(classPlanLiveId)) {
                log.warn(">>>>>>>>>>>>>>>>>>>>>>[ 数据已存在 ]<<<<<<<<<<<<<<<<<<<");
                return;
            }
            BiLiveAnswerRecord record = new BiLiveAnswerRecord();
            record.setClassplanLiveId(classPlanLiveId);
            record.setLiveAnswerNum(answerCount);
            record.setLiveTotalNum(questionCount);
            service.save(record);
            log.info(">>>>>>>>>>>>>>>>>>>>>>[ 同步完成 ]<<<<<<<<<<<<<<<<<<<");
        } catch (Exception e) {
            log.error("Exception===>" + e.getMessage() + ",message=" + message);
        }
    }
}

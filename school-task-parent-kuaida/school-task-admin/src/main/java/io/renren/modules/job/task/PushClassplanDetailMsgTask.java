package io.renren.modules.job.task;

import com.alibaba.fastjson.JSONObject;
import io.renren.modules.job.entity.PushClassplanDetailRemindEntity;
import io.renren.modules.job.pojo.MessageCardPOJO;
import io.renren.modules.job.service.PushClassplanDetailRemindService;
import io.renren.modules.job.service.SysCheckQuoteService;
import io.renren.modules.job.utils.DateUtils;
import io.renren.modules.job.utils.JSONUtil;
import io.renren.modules.job.utils.SyncDateConstant;
import io.renren.modules.job.utils.http.HttpClientUtil4_3;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推送课次通知消息到消息系统
 * Created by DL on 2018/10/25.
 */
@Component("io.renren.modules.job.controller.PushClassplanDetailMsgTask")
public class PushClassplanDetailMsgTask {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${domain.msg.host}")
    private String msgHost;

    @Autowired
    private SysCheckQuoteService sysCheckQuoteService;
    @Autowired
    private PushClassplanDetailRemindService pushClassplanDetailRemindService;


    public void execute(Map<String,Object> params){

        String millisecond = sysCheckQuoteService.syncDate(new HashMap<String , Object>(), SyncDateConstant.push_classplan_detail_remind);
        //获取变动的通知消息
        List<PushClassplanDetailRemindEntity> remindList = pushClassplanDetailRemindService.queryListByTs(millisecond);
        if ( remindList != null && remindList.size() > 0){
            for (PushClassplanDetailRemindEntity remindEntity : remindList) {
                //审核通过,dr=0的发送信息
                if (remindEntity.getAuditStatus() == 1
                        && remindEntity.getDr() == 0
                        && StringUtils.isBlank(remindEntity.getMsgId())){
                        //封装课次推送内容
                    String msg = getMessageCard(remindEntity);
                    sendMsg(remindEntity, msg);
                }else if (remindEntity.getAuditStatus() == 1
                        && remindEntity.getDr() == 0
                        && StringUtils.isNotBlank(remindEntity.getMsgId())){
                    //修改消息
                    String msg = getMessageCard(remindEntity);
                    updateMsg(remindEntity, msg);
                }else {
                    delMessage(remindEntity);
                }
            }
        }
        sysCheckQuoteService.updateSyncTime(new HashMap<>(),SyncDateConstant.push_classplan_detail_remind);
    }

    private void updateMsg(PushClassplanDetailRemindEntity remindEntity, String msg) {
        Map<String,String> paramsMap = new HashMap<String,String>();
        paramsMap.put("title", "提醒");
        paramsMap.put("content", "提醒");
        paramsMap.put("msg", msg);
        paramsMap.put("msg_id", remindEntity.getMsgId());
        paramsMap.put("send_time",remindEntity.getPushTime());
        paramsMap.put("group_channels", "classplanLive"+remindEntity.getCourseClassplanLivesId());
        String updateUrl = msgHost+"/msg/updateMessage";
        logger.info("PushClassplanDetailMsgTask.updateMsg.updateUrl={}",updateUrl);

        try {
            String updateResult = HttpClientUtil4_3.sendHttpPost(updateUrl, paramsMap);
            JSONObject jasonObject = JSONObject.parseObject(updateResult);
            if (200 == jasonObject.getIntValue("code")) {
                logger.info("PushClassplanDetailMsgTask.updateMsg.successful!,msgId={}",remindEntity.getMsgId());
            }else {
                logger.info("PushClassplanDetailMsgTask.updateMsg.failed!,msgId={},cause:{}",remindEntity.getMsgId(),jasonObject.get("message"));
            }
        } catch (Exception e) {
            logger.error("PushClassplanDetailMsgTask.updateMsg.failed!,msgId={},cause={}",remindEntity.getMsgId(),e.toString());
        }
    }

    private void sendMsg(PushClassplanDetailRemindEntity remindEntity, String msg) {
        Map<String,String> paramsMap = new HashMap<String,String>();
        paramsMap.put("title", "提醒");
        paramsMap.put("content", "提醒");
        paramsMap.put("msg", msg);
        paramsMap.put("send_time",remindEntity.getPushTime());
        paramsMap.put("group_channels", "classplanLive"+remindEntity.getCourseClassplanLivesId());
        String sendUrl = msgHost+"/msg/sendUserMessage";
        logger.info("PushClassplanDetailMsgTask.sendMsg.sendUrl={}",sendUrl);

        try {
            String sendResult = HttpClientUtil4_3.sendHttpPost(sendUrl, paramsMap);
            logger.info("PushClassplanDetailMsgTask.sendMsg.sendResult={}",sendResult);

            if(StringUtils.isNotBlank(sendResult)) {
                JSONObject jasonObject = JSONObject.parseObject(sendResult);
                if (200 == jasonObject.getIntValue("code")) {
                    //更新消息Id
                    Map<String, Object> msgData = (Map<String, Object>) jasonObject.get("data");
                    pushClassplanDetailRemindService.updateMsgId(remindEntity.getId(),msgData.get("msg_id").toString());
                    logger.info("PushClassplanDetailMsgTask.sendMsg.successful!,remindEntity.classplanLiveId={}",remindEntity.getCourseClassplanLivesId());
                }
            }
        } catch (Exception e) {
            logger.error("PushClassplanDetailMsgTask.sendMsg.fail!,cause={}",e.toString());
        }
    }

    private void delMessage(PushClassplanDetailRemindEntity remindEntity) {
        if (remindEntity.getMsgId() != null){
            Map<String,String> paramsMap = new HashMap<String,String>();
            paramsMap.put("msg_id", remindEntity.getMsgId());
            String delUrl = msgHost+"/msg/delMessage";
            logger.info("PushClassplanDetailMsgTask.delMessage.delUrl={}",delUrl);
            try {
                String delResult = HttpClientUtil4_3.sendHttpPost(delUrl, paramsMap);
                JSONObject jasonObject = JSONObject.parseObject(delResult);
                if (200 == jasonObject.getIntValue("code")) {
                    logger.info("PushClassplanDetailMsgTask.delMessage.successful!,msgId={}",remindEntity.getMsgId());
                }else {
                    logger.info("PushClassplanDetailMsgTask.delMessage.failed!,msgId={},cause:{}",remindEntity.getMsgId(),jasonObject.get("message"));
                }
            } catch (Exception e) {
                logger.error("PushClassplanDetailMsgTask.delMessage.failed!,msgId={},cause={}",remindEntity.getMsgId(),e.toString());
            }
        }
    }

    private String getMessageCard(PushClassplanDetailRemindEntity remindEntity) {
        MessageCardPOJO msgCard = new MessageCardPOJO();
        msgCard.setMsgType(5);
        msgCard.setTitle("提醒");
        msgCard.setDescribe("提醒");
        msgCard.setPushText(remindEntity.getPushContent());
        msgCard.setPushTime(DateUtils.parse(remindEntity.getPushTime(),"yyyy-MM-dd hh:mm:ss").getTime());
        HashMap<String,Object> msgData = new HashMap<>();
        msgData.put("pushContent",remindEntity.getPushContent());
        msgData.put("title",msgCard.getTitle());
        msgCard.setMsgData(msgData);
        String msg = JSONUtil.beanToJson(msgCard);
        return msg;
    }
}

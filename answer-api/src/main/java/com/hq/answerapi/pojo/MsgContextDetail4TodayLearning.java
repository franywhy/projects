package com.hq.answerapi.pojo;

import java.util.Map;

/**
 * Created by DL on 2018/1/19.
 */
public class MsgContextDetail4TodayLearning extends UserMsgContextDetailPOJO{

    //学习卡片消息体
    private Map<String,Object> msgData;

    public Map<String, Object> getMsgData() {
        return msgData;
    }

    public void setMsgData(Map<String, Object> msgData) {
        this.msgData = msgData;
    }

    @Override
    public String toString() {
        return "MsgContextDetail4TodayLearning{" +
                "msgData=" + msgData +
                '}';
    }
}

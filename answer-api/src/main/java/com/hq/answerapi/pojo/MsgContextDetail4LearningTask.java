package com.hq.answerapi.pojo;

/**
 * Created by DL on 2018/1/19.
 */
public class MsgContextDetail4LearningTask extends UserMsgContextDetailPOJO {

    //推送文案
    private String pushText;


    public String getPushText() {
        return pushText;
    }

    public void setPushText(String pushText) {
        this.pushText = pushText;
    }

    @Override
    public String toString() {
        return "MsgContextDetail4LearningTask{" +
                "pushText='" + pushText + '\'' +
                '}';
    }
}

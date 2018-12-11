package com.hq.learningapi.pojo;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by DL on 2018/1/19.
 */
public class ReceiveMsgContextDetailPOJO implements Serializable {
    //消息id;
    private String msgId;
    //消息类型
    private Integer msgType;
    //标题;
    private String title;
    //副标题
    private String describe;
    //推送时间
    private Long pushTime;
    //推送文案
    private String pushText;
    //学习卡片消息体
    private Map<String,Object> msgData;

    public String getPushText() {
        return pushText;
    }

    public void setPushText(String pushText) {
        this.pushText = pushText;
    }

    public Map<String, Object> getMsgData() {
        return msgData;
    }

    public void setMsgData(Map<String, Object> msgData) {
        this.msgData = msgData;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }


    public Long getPushTime() {
        return pushTime;
    }

    public void setPushTime(Long pushTime) {
        this.pushTime = pushTime;
    }

    @Override
    public String toString() {
        return "UserMsgContextDetailPOJO{" +
                "msgId='" + msgId + '\'' +
                ", msgType=" + msgType +
                ", title='" + title + '\'' +
                ", describe='" + describe + '\'' +
                ", pushTime='" + pushTime + '\'' +
                '}';
    }
}

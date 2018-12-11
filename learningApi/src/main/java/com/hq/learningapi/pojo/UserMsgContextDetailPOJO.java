package com.hq.learningapi.pojo;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by DL on 2018/1/19.
 */
public class UserMsgContextDetailPOJO implements Serializable {
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
    //用户消息是否已读
    private Integer isReaded;

    public String getMsgId() {
        return msgId;
    }

    public Integer getIsReaded() {
        return isReaded;
    }

    public void setIsReaded(Integer isReaded) {
        this.isReaded = isReaded;
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

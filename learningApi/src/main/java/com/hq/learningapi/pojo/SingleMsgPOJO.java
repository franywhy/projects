package com.hq.learningapi.pojo;

import java.io.Serializable;

/**
 * Created by DL on 2018/4/11.
 */
public class SingleMsgPOJO implements Serializable {
    private Long id;
    private String code;
    private String sendBy;
    private String recBy;
    private Long sendTime;
    private Integer sendType;
    private Integer sendStatus;
    private Long createTime;
    private Integer dr;
    private Integer version;
    private String sendTimeStr;
    private Integer isReaded;
    private String title;
    private String content;
    private Integer msgType;
    private Integer msgSort;
    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    public String getRecBy() {
        return recBy;
    }

    public void setRecBy(String recBy) {
        this.recBy = recBy;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getDr() {
        return dr;
    }

    public void setDr(Integer dr) {
        this.dr = dr;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getSendTimeStr() {
        return sendTimeStr;
    }

    public void setSendTimeStr(String sendTimeStr) {
        this.sendTimeStr = sendTimeStr;
    }

    public Integer getIsReaded() {
        return isReaded;
    }

    public void setIsReaded(Integer isReaded) {
        this.isReaded = isReaded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Integer getMsgSort() {
        return msgSort;
    }

    public void setMsgSort(Integer msgSort) {
        this.msgSort = msgSort;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

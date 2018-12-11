package com.hqjy.msg.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by baobao on 2017/11/20 0020.
 * 消息载体类
 */
public  class MessageEntity implements Serializable {

    private String times;
    private Object message;
    private String mid;
    private Date dates;

    public MessageEntity() {
    }

    public MessageEntity(String time, String mid, Date dates) {
        this.times = time;

        this.mid = mid;
        this.dates = dates;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Date getDates() {
        return dates;
    }

    public void setDates(Date dates) {
        this.dates = dates;
    }
}
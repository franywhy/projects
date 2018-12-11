package com.hq.answerapi.pojo;

import java.io.Serializable;

/**
 * Created by DL on 2018/1/22.
 */
public class UserMsgContextPOJO implements Serializable {
    //用户消息内容
    private String msg;
    //用户消息是否已读
    private Integer is_readed;
    //用户消息id
    private String msg_id;

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getIs_readed() {
        return is_readed;
    }

    public void setIs_readed(Integer is_readed) {
        this.is_readed = is_readed;
    }
}

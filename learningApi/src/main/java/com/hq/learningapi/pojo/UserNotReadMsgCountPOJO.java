package com.hq.learningapi.pojo;

import java.io.Serializable;

/**
 * Created by DL on 2018/1/16.
 */
public class UserNotReadMsgCountPOJO implements Serializable {
    //未读消息总数
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

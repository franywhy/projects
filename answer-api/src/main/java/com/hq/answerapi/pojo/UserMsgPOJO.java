package com.hq.answerapi.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DL on 2018/1/22.
 */
public class UserMsgPOJO implements Serializable{
    //用户消息总数
    private Integer count;
    //用户消息集合
    private List<UserMsgContextPOJO> list;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<UserMsgContextPOJO> getList() {
        return list;
    }

    public void setList(List<UserMsgContextPOJO> list) {
        this.list = list;
    }
}

package com.hq.learningapi.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by DL on 2018/1/20.
 */
public class AppFeedBackEntity implements Serializable {

    private Long feedBackId;
    //用户手机
    private String userMobile;
    //反馈内容
    private String feedBackContext;
    //反馈图片
    private String picture;
    //产品线
    private Long productId;
    //客户端
    private String client;
    //版本号
    private int version;
    //反馈时间
    private Date createTime;

    public Long getFeedBackId() {
        return feedBackId;
    }

    public void setFeedBackId(Long feedBackId) {
        this.feedBackId = feedBackId;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getFeedBackContext() {
        return feedBackContext;
    }

    public void setFeedBackContext(String feedBackContext) {
        this.feedBackContext = feedBackContext;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "AppFeedBackEntity{" +
                "feedBackId=" + feedBackId +
                ", userMobile='" + userMobile + '\'' +
                ", feedBackContext='" + feedBackContext + '\'' +
                ", picture='" + picture + '\'' +
                ", productId=" + productId +
                ", client='" + client + '\'' +
                ", version=" + version +
                ", createTime=" + createTime +
                '}';
    }
}

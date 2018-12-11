package com.hq.learningapi.entity;

public class UserTokenEntity {
     private Integer userId;
     private Integer versionCode;
     private String  clientType;
     private Long    timeStamp;
     private Boolean oneTimeToken;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getClientType() {
        return clientType.toLowerCase();
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Boolean getOneTimeToken() {
        return oneTimeToken;
    }

    public void setOneTimeToken(Boolean oneTimeToken) {
        this.oneTimeToken = oneTimeToken;
    }
}

package com.elise.singlesignoncenter.testpojo;


import com.hq.common.enumeration.ClientTypeEnum;

/**
 * Created by Glenn on 2017/5/6 0006.
 */
public class UserToken {
    private Integer userId;
    private Integer versionCode;
    private ClientTypeEnum clientType;
    private Long timeStamp;
    private Boolean isOneTimeToken;

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

    public ClientTypeEnum getClientType() {
        return clientType;
    }

    public void setClientType(ClientTypeEnum clientType) {
        this.clientType = clientType;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Boolean getOneTimeToken() {
        return isOneTimeToken;
    }

    public void setOneTimeToken(Boolean oneTimeToken) {
        isOneTimeToken = oneTimeToken;
    }
}

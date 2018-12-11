package com.elise.singlesignoncenter.token;


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

    public UserToken(Integer userId, Integer versionCode, ClientTypeEnum clientType, Long timeStamp,Boolean isOneTimeToken) {
        this.userId = userId;
        this.versionCode = versionCode;
        this.clientType = clientType;
        this.timeStamp = timeStamp;
        this.isOneTimeToken = isOneTimeToken;
    }

    public Boolean getOneTimeToken() {
        return isOneTimeToken;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public ClientTypeEnum getClientType() {
        return clientType;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n[UserToken]");
        sb.append("\nUser Id:");
        sb.append(userId);
        sb.append("\nVersion Code:");
        sb.append(versionCode);
        sb.append("\nClient Type:");
        sb.append(clientType);
        sb.append("\nTimeStamp:");
        sb.append(timeStamp);
        sb.append("\nisOneTimeToken:");
        sb.append(isOneTimeToken);
        return sb.toString();
    }
}

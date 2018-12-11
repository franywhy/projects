package com.elise.singlesignoncenter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Glenn on 2017/4/24 0024.
 */

@ConfigurationProperties(prefix="local-info")
@Component
public class LocalConfigEntity {

    private Long     tokenExpiredTime;

    private Long     otpSmsMaxExpireTime;

    private Long     otpSmsMaxGapTime;

    private Integer  optSmsMaxCount ;

    private Boolean  otpSmsSwitch;

    private String   otpPassKey;

    private String   defaultAvatarUrl;

    private Integer  checkPasswordMaxCount;

    private Integer  passwordMaxTimeout;


    public Integer getPasswordMaxTimeout() {
        return passwordMaxTimeout;
    }

    public void setPasswordMaxTimeout(Integer passwordMaxTimeout) {
        this.passwordMaxTimeout = passwordMaxTimeout*60*1000;
    }

    public Integer getCheckPasswordMaxCount() {
        return checkPasswordMaxCount;
    }

    public void setCheckPasswordMaxCount(Integer checkPasswordMaxCount) {
        this.checkPasswordMaxCount = checkPasswordMaxCount;
    }

    public String getOtpPassKey() {
        return otpPassKey;
    }

    public void setOtpPassKey(String otpPassKey) {
        this.otpPassKey = otpPassKey;
    }

    public Boolean getOtpSmsSwitch() {
        return otpSmsSwitch;
    }

    public void setOtpSmsSwitch(Boolean otpSmsSwitch) {
        this.otpSmsSwitch = otpSmsSwitch;
    }

    public Long getOtpSmsMaxExpireTime() {

        return otpSmsMaxExpireTime;
    }

    public void setOtpSmsMaxExpireTime(Long otpSmsMaxExpireTime) {

        this.otpSmsMaxExpireTime = otpSmsMaxExpireTime*60*1000;
    }

    public Long getOtpSmsMaxGapTime() {

        return otpSmsMaxGapTime;
    }

    public void setOtpSmsMaxGapTime(Long otpSmsMaxGapTime) {

        this.otpSmsMaxGapTime = otpSmsMaxGapTime*1000;
    }

    public Integer getOptSmsMaxCount() {

        return optSmsMaxCount;
    }

    public void setOptSmsMaxCount(Integer optSmsMaxCount) {

        this.optSmsMaxCount = optSmsMaxCount;
    }

    public Long getTokenExpiredTime() {

        return tokenExpiredTime;
    }

    public void setTokenExpiredTime(Long tokenExpiredTime) {

        //tokenExpiredTime*1000*60*60*24
        this.tokenExpiredTime = tokenExpiredTime*1000*60*60*24;
    }

    public String getDefaultAvatarUrl() {
        return defaultAvatarUrl;
    }

    public void setDefaultAvatarUrl(String defaultAvatarUrl) {
        this.defaultAvatarUrl = defaultAvatarUrl;
    }
}

package com.hq.learningapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Glenn on 2017/4/17 0017.
 */
@ConfigurationProperties(prefix="local-info")
@Component
public class LocalConfigEntity {


    private String schoolId;

    private Integer maxOffset;

    private Integer msgMaxOffset;

    private String genseeWebcastUrl;

    private String genseeServiceType;

    private Long genseeVerifyAging;

    private String polyvVerifyUrl;

    private String oliveReplayUrl;

    private String replayUrl;

    private String topContentPic;

    private String customerCareUrl;

    private String ssoHost;

    private String msgSystemHost;

    private String apph5Url;

    public String getMsgSystemHost() {
        return msgSystemHost;
    }

    public void setMsgSystemHost(String msgSystemHost) {
        this.msgSystemHost = msgSystemHost;
    }

    public String getSsoHost() {
        return ssoHost;
    }

    public void setSsoHost(String ssoHost) {
        this.ssoHost = ssoHost;
    }

    public String getCustomerCareUrl() {
        return customerCareUrl;
    }

    public void setCustomerCareUrl(String customerCareUrl) {
        this.customerCareUrl = customerCareUrl;
    }

    public Integer getMaxOffset() {
        return maxOffset;
    }

    public void setMaxOffset(Integer maxOffset) {
        this.maxOffset = maxOffset;
    }

    public Integer getMsgMaxOffset() { return msgMaxOffset; }

    public void setMsgMaxOffset(Integer msgMaxOffset) { this.msgMaxOffset = msgMaxOffset; }


    public String getTopContentPic() {  return topContentPic; }

    public void setTopContentPic(String topContentPic) {  this.topContentPic = topContentPic; }

    public String getPolyvVerifyUrl() { return polyvVerifyUrl; }

    public void setPolyvVerifyUrl(String polyvVerifyUrl) { this.polyvVerifyUrl = polyvVerifyUrl; }

    public String getReplayUrl() { return replayUrl; }

    public void setReplayUrl(String replayUrl) { this.replayUrl = replayUrl; }

    public String getOliveReplayUrl() { return oliveReplayUrl; }

    public void setOliveReplayUrl(String oliveReplayUrl) { this.oliveReplayUrl = oliveReplayUrl; }

    public String getGenseeServiceType() { return genseeServiceType; }

    public void setGenseeServiceType(String genseeServiceType) { this.genseeServiceType = genseeServiceType; }

    public Long getGenseeVerifyAging() {
        return genseeVerifyAging;
    }

    public void setGenseeVerifyAging(Long genseeVerifyAging) {
        this.genseeVerifyAging = genseeVerifyAging;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getGenseeWebcastUrl() {
        return genseeWebcastUrl;
    }

    public void setGenseeWebcastUrl(String genseeWebcastUrl) {
        this.genseeWebcastUrl = genseeWebcastUrl;
    }

    public String getApph5Url() {
        return apph5Url;
    }

    public void setApph5Url(String apph5Url) {
        this.apph5Url = apph5Url;
    }
}

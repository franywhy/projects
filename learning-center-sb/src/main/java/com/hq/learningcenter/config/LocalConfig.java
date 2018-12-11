package com.hq.learningcenter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Glenn on 2017/4/17 0017.
 */
@ConfigurationProperties(prefix="local-info")
@Component
public class LocalConfig {

    private String ssoHost;

    private String creditGetmycreditUrl;

    private String liveCallbackInfo;

    private String replayCallbackInfo;

    private String genseeWebcastWeb;

    private String genseeCtx;

    private Long genseeKTimeout;

    private String genseeKWhite;

    private String ccVodUrl;

    private String customerCareUrl;

    private String oldCenter;

    private String ccCommodity;

    private String onlineContractUrl;

    private String onlineContractAppid;

    private String onlineContractAppkey;

    private Long onlineContractAppsignerid;

    private String insuranceDeskey;

    private String notBusinessDomain;

    public String getSsoHost() {
        return ssoHost;
    }

    public void setSsoHost(String ssoHost) {
        this.ssoHost = ssoHost;
    }

    public String getCreditGetmycreditUrl() {
        return creditGetmycreditUrl;
    }

    public void setCreditGetmycreditUrl(String creditGetmycreditUrl) {
        this.creditGetmycreditUrl = creditGetmycreditUrl;
    }

    public String getLiveCallbackInfo() {
        return liveCallbackInfo;
    }

    public void setLiveCallbackInfo(String liveCallbackInfo) {
        this.liveCallbackInfo = liveCallbackInfo;
    }

    public String getReplayCallbackInfo() {
        return replayCallbackInfo;
    }

    public void setReplayCallbackInfo(String replayCallbackInfo) {
        this.replayCallbackInfo = replayCallbackInfo;
    }

    public String getGenseeWebcastWeb() {
        return genseeWebcastWeb;
    }

    public void setGenseeWebcastWeb(String genseeWebcastWeb) {
        this.genseeWebcastWeb = genseeWebcastWeb;
    }

    public String getGenseeCtx() {
        return genseeCtx;
    }

    public void setGenseeCtx(String genseeCtx) {
        this.genseeCtx = genseeCtx;
    }

    public Long getGenseeKTimeout() {
        return genseeKTimeout;
    }

    public void setGenseeKTimeout(Long genseeKTimeout) {
        this.genseeKTimeout = genseeKTimeout;
    }

    public String getGenseeKWhite() {
        return genseeKWhite;
    }

    public void setGenseeKWhite(String genseeKWhite) {
        this.genseeKWhite = genseeKWhite;
    }

    public String getCcVodUrl() {
        return ccVodUrl;
    }

    public void setCcVodUrl(String ccVodUrl) {
        this.ccVodUrl = ccVodUrl;
    }

    public String getCustomerCareUrl() {
        return customerCareUrl;
    }

    public void setCustomerCareUrl(String customerCareUrl) {
        this.customerCareUrl = customerCareUrl;
    }

    public String getOldCenter() {
        return oldCenter;
    }

    public void setOldCenter(String oldCenter) {
        this.oldCenter = oldCenter;
    }

    public String getCcCommodity() {
        return ccCommodity;
    }

    public void setCcCommodity(String ccCommodity) {
        this.ccCommodity = ccCommodity;
    }

    public String getOnlineContractUrl() {
        return onlineContractUrl;
    }

    public void setOnlineContractUrl(String onlineContractUrl) {
        this.onlineContractUrl = onlineContractUrl;
    }

    public String getOnlineContractAppid() {
        return onlineContractAppid;
    }

    public void setOnlineContractAppid(String onlineContractAppid) {
        this.onlineContractAppid = onlineContractAppid;
    }

    public String getOnlineContractAppkey() {
        return onlineContractAppkey;
    }

    public void setOnlineContractAppkey(String onlineContractAppkey) {
        this.onlineContractAppkey = onlineContractAppkey;
    }

    public Long getOnlineContractAppsignerid() {
        return onlineContractAppsignerid;
    }

    public void setOnlineContractAppsignerid(Long onlineContractAppsignerid) {
        this.onlineContractAppsignerid = onlineContractAppsignerid;
    }

    public String getInsuranceDeskey() {
        return insuranceDeskey;
    }

    public void setInsuranceDeskey(String insuranceDeskey) {
        this.insuranceDeskey = insuranceDeskey;
    }

    public String getNotBusinessDomain() {
        return notBusinessDomain;
    }

    public void setNotBusinessDomain(String notBusinessDomain) {
        this.notBusinessDomain = notBusinessDomain;
    }
}

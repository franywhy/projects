package com.hq.learningcenter.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Glenn on 2017/4/24 0024.
 */

@ConfigurationProperties(prefix="local-info")
@Component
public class LocalConfigProperties {

    private String ssoHost;

    private String qrCodeUrl;

    private Long captchaTimeout;

    private Integer cookieTokenTimeout;

    private String cookieTokenName;

    public Integer getCookieTokenTimeout() {
        return cookieTokenTimeout;
    }

    public void setCookieTokenTimeout(Integer cookieTokenTimeout) {
        this.cookieTokenTimeout = cookieTokenTimeout;
    }

    public Long getCaptchaTimeout() {
        return captchaTimeout;
    }

    public void setCaptchaTimeout(Long captchaTimeout) {
        this.captchaTimeout = captchaTimeout;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getSsoHost() {
        return ssoHost;
    }

    public void setSsoHost(String ssoHost) {
        this.ssoHost = ssoHost;
    }

    public String getCookieTokenName() {
        return cookieTokenName;
    }

    public void setCookieTokenName(String cookieTokenName) {
        this.cookieTokenName = cookieTokenName;
    }
}

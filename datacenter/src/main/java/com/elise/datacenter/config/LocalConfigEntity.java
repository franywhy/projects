package com.elise.datacenter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Glenn on 2017/4/17 0017.
 */
@ConfigurationProperties(prefix="local-info")
@Component
public class LocalConfigEntity {

    private String serviceUrl;
    private String ssoHost;

    public String getSsoHost() {
        return ssoHost;
    }

    public void setSsoHost(String ssoHost) {
        this.ssoHost = ssoHost;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

}

package com.bluewhale.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Glenn on 2017/8/9 0024.
 */
@ConfigurationProperties(prefix = "multipart")
public class MultiPartRepository {

    private Boolean enabled = false;
    private String maxFileSize = null;
    private String maxRequestSize = null;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(String maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String getMaxRequestSize() {
        return maxRequestSize;
    }

    public void setMaxRequestSize(String maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }
}

package com.hq.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Created by Glenn on 2017/5/18 0018.
 */

@ConfigurationProperties(prefix="fdfs")
public class FastDFSRepository {

    private Boolean enabled;
    private Integer networkTimeout;
    private Integer connectTimeout;
    List<String> trackerServer;
    private String charset;
    private Integer trackerHttpPort;
    private Boolean antiStealToken;
    private String secretKey;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getNetworkTimeout() {
        return networkTimeout;
    }

    public void setNetworkTimeout(Integer networkTimeout) {
        this.networkTimeout = networkTimeout;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public List<String> getTrackerServer() {
        return trackerServer;
    }

    public void setTrackerServer(List<String> trackerServer) {
        this.trackerServer = trackerServer;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Integer getTrackerHttpPort() {
        return trackerHttpPort;
    }

    public void setTrackerHttpPort(Integer trackerHttpPort) {
        this.trackerHttpPort = trackerHttpPort;
    }

    public Boolean getAntiStealToken() {
        return antiStealToken;
    }

    public void setAntiStealToken(Boolean antiStealToken) {
        this.antiStealToken = antiStealToken;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}

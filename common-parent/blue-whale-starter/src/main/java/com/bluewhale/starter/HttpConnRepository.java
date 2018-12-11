package com.bluewhale.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * Created by Glenn on 2017/8/9 0024.
 */
@ConfigurationProperties(prefix = "http")
public class HttpConnRepository {

    private Integer retryCount;
    private Boolean expectContinueEnable;
    private Pool pool;
    private Timeout timeout;


    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public Timeout getTimeout() {
        return timeout;
    }

    public void setTimeout(Timeout timeout) {
        this.timeout = timeout;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Boolean getExpectContinueEnable() {
        return expectContinueEnable;
    }

    public void setExpectContinueEnable(Boolean expectContinueEnable) {
        this.expectContinueEnable = expectContinueEnable;
    }


    public static class Pool {

        private Integer maxTotal;
        private Integer routeTotal;

        public Integer getMaxTotal() {
            return maxTotal;
        }

        public void setMaxTotal(Integer maxTotal) {
            this.maxTotal = maxTotal;
        }

        public Integer getRouteTotal() {
            return routeTotal;
        }

        public void setRouteTotal(Integer routeTotal) {
            this.routeTotal = routeTotal;
        }
    }

    public static class Timeout {
        private Integer conn;
        private Integer request;
        private Integer socket;

        public Integer getConn() {
            return conn;
        }

        public void setConn(Integer conn) {
            this.conn = conn;
        }

        public Integer getRequest() {
            return request;
        }

        public void setRequest(Integer request) {
            this.request = request;
        }

        public Integer getSocket() {
            return socket;
        }

        public void setSocket(Integer socket) {
            this.socket = socket;
        }
    }

}

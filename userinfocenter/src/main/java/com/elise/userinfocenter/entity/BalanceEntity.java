package com.elise.userinfocenter.entity;

import java.io.Serializable;


/**
 * @author hq
 * @email hq@hq.com
 * @date 2018-02-27 17:34:28
 */
public class BalanceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String mobile;
    private Long userId;
    private Double cash;
    private Double points;
    private Integer dr;
    private Double hqg;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public Integer getDr() {
        return dr;
    }

    public void setDr(Integer dr) {
        this.dr = dr;
    }

    public Double getHqg() {
        return hqg;
    }

    public void setHqg(Double hqg) {
        this.hqg = hqg;
    }
}

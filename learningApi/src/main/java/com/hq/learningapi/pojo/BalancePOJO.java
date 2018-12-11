package com.hq.learningapi.pojo;

/**
 * Created by DL on 2018/9/11.
 */
public class BalancePOJO {

    private Long id;
    //用户名称
    private String username;
    //用户手机
    private String mobile;
    //用户id
    private Long userId;
    //用户金额
    private Double cash;
    //用户积分
    private Double points;

    private Integer dr;
    //用户恒企币
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

    @Override
    public String toString() {
        return "BalancePOJO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", userId=" + userId +
                ", cash=" + cash +
                ", points=" + points +
                ", dr=" + dr +
                ", hqg=" + hqg +
                '}';
    }
}

package io.renren.entity;


import java.math.BigInteger;
import java.util.Date;

public class UserPerformEntity {
    //ID
    private int id;
    //订单ID
    private Long orderId;
    //用户ID
    private Long  userId;
    //用户昵称
    private String userNickname;
    //创建时间
    private Date creationTime;
    //奖学金名称
    private String scholarshipName;
    //奖学金
    private float scholarship;
    //学习评价
    private String perform;
    //添加者ID
    private long creatorId;
    //有效状态
    private int dr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getScholarshipName() {
        return scholarshipName;
    }

    public void setScholarshipName(String scholarshipName) {
        this.scholarshipName = scholarshipName;
    }

    public float getScholarship() {
        return scholarship;
    }

    public void setScholarship(float scholarship) {
        this.scholarship = scholarship;
    }

    public String getPerform() {
        return perform;
    }

    public void setPerform(String perform) {
        this.perform = perform;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public int getDr() {
        return dr;
    }

    public void setDr(int dr) {
        this.dr = dr;
    }

    @Override
    public String toString() {
        return "UserPerformEntity{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", userId=" + userId +
                ", userNickname='" + userNickname + '\'' +
                ", creationTime=" + creationTime +
                ", scholarshipName='" + scholarshipName + '\'' +
                ", scholarship=" + scholarship +
                ", perform='" + perform + '\'' +
                ", creatorId=" + creatorId +
                ", dr=" + dr +
                '}';
    }
}

package io.renren.pojo;


import java.math.BigInteger;
import java.util.Date;

public class UserPerformPOJO {
    //ID
    private int id;
    //商品名称
    private String commodityName;
    //用户ID
    private Long userId;
    //用户昵称
    private String userNickname;
    //创建时间
    private Date creationTime;
    //奖学金名称
    private String scholarshipName;
    //奖学金
    private Float scholarship;
    //学习评价
    private String perform;
    //班主任
    private String teacher;
    //有效状态
    private int dr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    public int getDr() {
        return dr;
    }

    public void setDr(int dr) {
        this.dr = dr;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "UserPerformPOJO{" +
                "id=" + id +
                ", commodityName='" + commodityName + '\'' +
                ", userId=" + userId +
                ", userNickname='" + userNickname + '\'' +
                ", creationTime=" + creationTime +
                ", scholarshipName='" + scholarshipName + '\'' +
                ", scholarship=" + scholarship +
                ", perform='" + perform + '\'' +
                ", dr=" + dr +
                '}';
    }
}

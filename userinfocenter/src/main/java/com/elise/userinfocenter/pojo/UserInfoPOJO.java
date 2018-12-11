package com.elise.userinfocenter.pojo;

/**
 * Created by Glenn on 2017/4/22 0022.
 */
public class UserInfoPOJO {

    private String avatar;
    private String nickName;
    private String email;
    private Integer gender;
    private Integer uid;
    private String mobileNo;

    ////////////////////////
    /*private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }*/
/////////////////////////////////////////
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "UserInfoPOJO{" +
                "avatar='" + avatar + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", uid=" + uid +
                ", mobileNo='" + mobileNo + '\'' +
                '}';
    }
}

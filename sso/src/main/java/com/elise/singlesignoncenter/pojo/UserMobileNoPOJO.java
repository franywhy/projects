package com.elise.singlesignoncenter.pojo;

/**
 * add by shihongjie 2018-01-30
 */
public class UserMobileNoPOJO {
    private Integer userId;
    private String mobileNo;

    public UserMobileNoPOJO() {
    }

    public UserMobileNoPOJO(Integer userId, String mobileNo) {

        this.userId = userId;
        this.mobileNo = mobileNo;
    }

    @Override
    public String toString() {
        return "UserMobileNoPOJO{" +
                "mobileNo='" + mobileNo + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

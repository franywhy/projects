package com.elise.singlesignoncenter.pojo;

/**
 * Created by Glenn on 2017/4/22 0022.
 */

public class UserInfoPOJO {

    private Integer uid;
    private String avatar;
    private String nickName;
    private String email;
    private Integer gender;


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
        StringBuilder sb = new StringBuilder();
        sb.append("\nuid=");
        sb.append(uid);
        sb.append("\navatar=");
        sb.append(avatar);
        sb.append("\nnickName=");
        sb.append(nickName);
        sb.append("\nemail=");
        sb.append(email);
        sb.append("\ngender=");
        sb.append(gender);
        return sb.toString();
    }
}

package com.school.pojo;

import java.io.Serializable;

public class UserInfoPOJO implements Serializable  {

	private static final long serialVersionUID = 1436814577593284974L;

	private Long userId;
	
	private String mobileNo;
	
	private String nickName;
	
	private Integer gender;
	
	private String email;
	
	private String avatar;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

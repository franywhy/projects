package com.school.accountant.vo;

import java.io.Serializable;

/**
 * session中保存的用户信息
 *
 */
public class UserInfo implements Serializable {

	private static final long serialVersionUID = 446742380438325938L;

	/**
	 * 用户ID
	 */
	private String id;

	/**
	 * 手机号
	 */
	private String mobileNo;

	/**
	 * 昵称
	 */
	private String nickname;
	
	/**
	 * 用户头像
	 */
	private String photo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
}

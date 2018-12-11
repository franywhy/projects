package io.renren.test.domain;

import java.io.Serializable;

public class Info implements Serializable{
	private String nickName;
	private String token;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}

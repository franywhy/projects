package com.izhubo.userSystem.mongo.qquser;

import java.math.BigInteger;

import org.springframework.data.annotation.Id;

public class QQUser {

	@Id
	private BigInteger id;
	
	private String openId;
	
	private String username;
	
	private String nickName;
	
	private String tuid;
	
	private String password;
	
	
	private String qd;
	
	private String qqpicUrl;
	
	private String pk;
	
	
	public void setId(BigInteger id) {
		this.id = id;
	}
	public BigInteger getId() {
		return id;
	}
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getTuid() {
		return tuid;
	}
	public void setTuid(String tuid) {
		this.tuid = tuid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getQd() {
		return qd;
	}
	public void setQd(String qd) {
		this.qd = qd;
	}
	public String getQqpicUrl() {
		return qqpicUrl;
	}
	public void setQqpicUrl(String qqpicUrl) {
		this.qqpicUrl = qqpicUrl;
	}
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	
	
	
	
	
}

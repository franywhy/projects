package com.school.pojo;

import java.io.Serializable;

public class UdeskPOJO implements Serializable  {

	private static final long serialVersionUID = 1436814577593284974L;

	private String nick_name;
	
	private String c_phone;
	
	private String c_email;
	
	private String agent_id;
	
	private String web_token;
	
	private String sdk_token;
	
	private String session_key;
	
	private String app_id;
	
	private String app_key;
	
	private String domain;
	
	private String nonce;
	
	private Long timestamp;
	
	private String sign;
	
	private Integer unreadCount;

	public String getSession_key() {
		return session_key;
	}

	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}

	public String getC_phone() {
		return c_phone;
	}

	public void setC_phone(String c_phone) {
		this.c_phone = c_phone;
	}

	public String getC_email() {
		return c_email;
	}

	public void setC_email(String c_email) {
		this.c_email = c_email;
	}

	public String getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(String agent_id) {
		this.agent_id = agent_id;
	}

	public String getWeb_token() {
		return web_token;
	}

	public void setWeb_token(String web_token) {
		this.web_token = web_token;
	}

	public String getSdk_token() {
		return sdk_token;
	}

	public void setSdk_token(String sdk_token) {
		this.sdk_token = sdk_token;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getApp_key() {
		return app_key;
	}

	public void setApp_key(String app_key) {
		this.app_key = app_key;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(Integer unreadCount) {
		this.unreadCount = unreadCount;
	}
	
	
}

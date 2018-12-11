package com.izhubo.userSystem.web;

public enum UsError {
	
//	UsernameHasExisted("30306", "手机已注册，可用会计城账号登录");
	UsernameHasExisted("30306", "手机已注册，如果你忘记了密码，请点击“找回密码”功能"),
	UsernameNotExisted("30307", "此手机号未注册过，请返回注册");
	
	private String message;
	
	private String code;
	
	private UsError(String code, String message){
		this.code = code;
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
}

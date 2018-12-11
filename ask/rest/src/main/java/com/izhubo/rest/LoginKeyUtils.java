package com.izhubo.rest;

public abstract class LoginKeyUtils {
	
	
	
	
	public static final String USERNAME_TO_INFO = "uinfo:";
	
	public static String getUserNameToInfoKey(String username){
		return USERNAME_TO_INFO + username;
	}
	
	public static class LR_APP{
		public static final String NAMESPACE = "q0-";
		public static final String ACCESS_TOKEN_TO_USERNAME_KEY = "access_token_to_username";
		public static final String USERNAME_TO_ACCESS_TOKEN_KEY = "username_to_access_token";
	}
	public static class LR_PC{
		public static final String NAMESPACE_APP = "q1-";
		public static final String ACCESS_TOKEN_TO_USERNAME_KEY = "access_token_to_username_pc";
		public static final String USERNAME_TO_ACCESS_TOKEN_KEY = "username_to_access_token_pc";
	}
	
}

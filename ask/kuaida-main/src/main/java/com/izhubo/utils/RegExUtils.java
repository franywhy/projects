package com.izhubo.utils;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class RegExUtils {
	
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^[0-9]{11}$");  
	
    /**
     * 手机号码校验
     * @Description: 手机号码校验 
     * @date 2015年10月12日 下午3:56:22 
     * @param @param mobile 要校验的手机号码
     * @param @return 正确的手机号码 true 否则false
     */
	public static boolean isMobile(String mobile){
		if(StringUtils.isEmpty(mobile)){
			return false;
		}
		return MOBILE_PATTERN.matcher(mobile).matches();
	}
	
	/**
	 * 校验如果是手机截取最后两位,否则直接返回str
	 * @Description: 校验如果是手机截取最后两位,否则直接返回str
	 * @date 2015年10月13日 上午8:58:55 
	 * @param @param str 要校验的字符串
	 * @param @return 
	 */
	public static String mobileReplace(String str){
		if(!isMobile(str)){
			return str;
		}else{
			int len = str.length();
			return str.substring(len - 4, len);
		}
	}
	
	public static void main(String[] args) {
		String s = "12345678901";
		System.out.println(isMobile(s));
		System.out.println(mobileReplace(s));
	}
	
}

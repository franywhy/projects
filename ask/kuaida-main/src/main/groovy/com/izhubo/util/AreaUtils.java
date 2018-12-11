package com.izhubo.util;

import org.apache.commons.lang3.StringUtils;

public class AreaUtils {
	
	
//	大区的编码字符长度为3
	public static final int ARE_LENGTH = 3;
//	省编码字符长度为5
	public static final int PROVINCE_LENGTH = 5;
//	市编码字符长度为7
	public static final int CITY_LENGTH = 7;
//	公司编码字符长度为9
	public static final int COMPANY_LENGTH = 9;
//	校区编码字符长度为11
	public static final int SCHOOL_LENGTH = 11;

	/**
	 * 从string获取大区编码
	 * @date 2016年3月10日 下午3:32:13 
	 * @param @param string 传入编码
	 * @param @return 大区编码
	 */
	public static String getAre(String string){
		int len = ARE_LENGTH;
		if(StringUtils.isNotBlank(string) && string.length() > len){
			return string.substring(0,len);
		}
		return string;
	}
	
	/**
	 * 从string获取省编码
	 * @date 2016年3月10日 下午3:32:13 
	 * @param @param string 传入编码
	 * @param @return 省编码
	 */
	public static String getProvince(String string){
		int len = PROVINCE_LENGTH;
		if(StringUtils.isNotBlank(string) && string.length() > len){
			return string.substring(0,len);
		}
		return string;
	}
	
	/**
	 * 从string获取城市编码
	 * @date 2016年3月10日 下午3:32:13 
	 * @param @param string 传入编码
	 * @param @return 城市编码
	 */
	public static String getCity(String string){
		int len = CITY_LENGTH;
		if(StringUtils.isNotBlank(string) && string.length() > len){
			return string.substring(0,len);
		}
		return string;
//		int len = CITY_LENGTH;
//		if(StringUtils.isNotBlank(string) && string.length() > len){
//			return string.substring(0,len);
//		}
//		return string;
	}
	
	/**
	 * 从string获取公司编码
	 * @date 2016年3月10日 下午3:32:13 
	 * @param @param string 传入编码
	 * @param @return 公司编码
	 */
	public static String getCompany(String string){
		int len = COMPANY_LENGTH;
		if(StringUtils.isNotBlank(string) && string.length() > len){
			return string.substring(0,len);
		}
		return string;
	}
	
}

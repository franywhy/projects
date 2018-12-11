package com.izhubo.rest.common.util.school;

import org.apache.commons.lang.StringUtils;
/**
 * 校区方法
 *
 */
public class SchoolUtils {

	public static final String DEFAULT_PHONE_ARRAY[] = { "400-070-6667" };

	/**
	 * 校区电话号码
	 * <p>默认是总部校区电话号码,可以存多个电话号码,最终以数组的形式返回
	 * @param phone	数据库中保存的电话号码
	 * @return		校区电话号码数组
	 */
	public static final String[] phonearray(String phone) {
		if (StringUtils.isNotBlank(phone)) {
			return phone.split("/");
		}
		return DEFAULT_PHONE_ARRAY;

	}
	
//	public static void main(String[] args) {
//		show("400-500-600");
//		show("123");
//		show("123/456");
//		show("/999");
//	}
//	
//	public static void show(String str){
//		String[] ar = phonearray(str);
//		if(ar != null && ar.length > 0){
//			for (int i = 0; i < ar.length; i++) {
//				System.out.println("str:" + str + "  i:" + i + "  ar[i]:" + ar[i]);
//			}
//		}
//	}
}

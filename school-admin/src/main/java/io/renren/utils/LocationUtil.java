package io.renren.utils;
/**
 * 判断所在地
 * @author lintf
 *身份证号、手机号校验标准
会校验格式，大陆）长度≤18位，首位非0，最后一位为数字或字母;2.（香港）一个英文字母开头+6个数字+括号及0-9中的任一个数字，或者字母A
；eg: C668668（9）;3.（台湾）长度=10位，首位与第二位为字母+8个数字；4.（澳门）8个拉丁数字组成格式为“X/NNNNNN/Y”或“XNNNNNN(Y)”；该身份证号码针对应用具有唯一性
 
 
 手机号，大陆,首位为1，长度11位纯数字;2香港、澳门,长度为8的纯数字;3台湾,长度为10的纯数字;分别对应1-3类型的正则，在数据库中不具唯一性
 */
public class LocationUtil {
/**
 * 
 *@param mobile
 *@return 0 大陆 1香港澳门 2 台湾
 * @author lintf
 * 2018年6月6日
 */
	public static int getMobileLocation(String mobile) {
		try {
		if(mobile.length()==11&&mobile.matches("^(1)\\d{10}$")) {  //首位为1 后面10位为数字
			return 0;
		} else if (mobile.length()==8&&mobile.matches("[0-9]+")) {//8位纯数字
			return 1;
		} else if (mobile.length()==10&&mobile.matches("[0-9]+")) {//10位纯数字
			return 2;
		}
		}catch(Exception e) {
			
			System.out.println(e.getMessage());
			return 0;
		}
		
		
		return 0;
		
	}
	/**
	 * 
	 *@param idcard
	 *@return 0 大陆 1 香港 2 台湾  3 澳门
	 * @author lintf
	 * 2018年6月6日
	 */
	public static int getIdCardLocation(String idcard) {
		
		if (idcard.length()==18||idcard.length()==15) {
		if(	idcard.matches("^\\d{17}([0-9]|x|X){1}$")||
			
			idcard.matches("^\\d{14}([0-9]|x|X){1}$") ) {
			return 0;
		}
			
			
		}else {
		
		
		  if (idcard.matches("^[a-zA-Z][0-9]{9}$")) { // 台湾
			  return 2;
	           
	        } else if (idcard.matches("^[1|5|7][0-9]{6}\\(?[0-9A-Z]\\)?$")) { // 澳门
	          return 3;
	        } else if (idcard.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) { // 香港
	           return 1; 
	        }  
		}
		return 0;
		
	}
 
	 
}

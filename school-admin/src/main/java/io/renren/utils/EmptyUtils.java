package io.renren.utils;
/**  
 * 类说明   
 *  
 * @author shihongjie
 * @email  shihongjie@hengqijy.com
 * @date 2017年11月29日
 */
public class EmptyUtils {
	
	public static boolean isEmpty(final Long l) {
		return null != l && l != 0;
	}
	
	public static boolean isNotEmpty(final Long l) {
		return !isEmpty(l);
	}
	
	public static boolean isEmpty(final Integer i) {
		return null != i && i != 0;
	}
	
	public static boolean isNotEmpty(final Integer i) {
		return !isEmpty(i);
	}
	
	public static boolean isEmpty(final Double d) {
		return null != d && d != 0;
	}
	
	public static boolean isNotEmpty(final Double d) {
		return !isEmpty(d);
	}
	
}

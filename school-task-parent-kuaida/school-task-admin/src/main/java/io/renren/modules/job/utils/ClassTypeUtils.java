package io.renren.modules.job.utils;

import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * 班型工具类
 * @class io.renren.utils.ClassTypeUtils.java
 * @Description:
 * @author shihongjie
 * @dete 2017年5月12日
 */
public class ClassTypeUtils {
	
	private static final String CLASSTYPE_SY = ",";
	
	public static final String ins(Long... longs){
		StringBuffer sbf = new StringBuffer();
		if(null != longs && longs.length > 0){
			sbf.append(CLASSTYPE_SY);
			for (Long long1 : longs) {
				sbf.append(long1 + CLASSTYPE_SY);
			}
		}
		return sbf.toString();
	}
	public static final String inArr(Long[] longs){
		StringBuffer sbf = new StringBuffer();
		if(null != longs && longs.length > 0){
			sbf.append(CLASSTYPE_SY);
			for (Long long1 : longs) {
				sbf.append(long1 + CLASSTYPE_SY);
			}
		}
		return sbf.toString();
	}
	public static final String inArr(List<Long> longs){
		StringBuffer sbf = new StringBuffer();
		if(null != longs && longs.size() > 0){
			sbf.append(CLASSTYPE_SY);
			for (Long long1 : longs) {
				sbf.append(long1 + CLASSTYPE_SY);
			}
		}
		return sbf.toString();
	}
	
	/**
	 * 权限列表入库前操作
	 * @param ids
	 * @return
	 */
	public static final String in(String ids){
		StringBuffer sbf = new StringBuffer();
		//非空
		if(StringUtils.isNotBlank(ids)){
			//不是以CLASSTYPE_SY开始
			if(!startSY(ids)){
				sbf.append(CLASSTYPE_SY);
			}
			sbf.append(ids);
			//如果不是以CLASSTYPE_SY结束
			if(!lastSY(ids)){
				sbf.append(CLASSTYPE_SY);
			}
		}
		return sbf.toString();
	}
	
	/**
	 * 权限列表出库后的操作
	 * @param ids
	 * @return
	 */
	public static final String out(String ids){
		if(StringUtils.isNotBlank(ids)){
			if(startSY(ids)){
				ids = ids.substring(1, ids.length());
			}
			if(lastSY(ids)){
				ids = ids.substring(0, ids.length() - 1);
			}
		}
		return ids;
	}
	
	private static boolean startSY(String ids){
		if(StringUtils.isNotBlank(ids)){
			return ids.indexOf(CLASSTYPE_SY, 0) == 0;
		}
		return false;
	}
	
	private static boolean lastSY(String ids){
		if(StringUtils.isNotBlank(ids)){
			return ids.lastIndexOf(CLASSTYPE_SY) == ids.length()-1;
		}
		return false;
	}
	
	public static void main(String[] args) {
		String s0 = "11";
		String s1 = "11,12";
		String s2 = ",11,12";
		String s3 = "11,12,";
		String s4 = ",11,12,";
//		ins(longs)
		System.out.println(out(s0));
		System.out.println(out(s1));
		System.out.println(out(s2));
		System.out.println(out(s3));
		System.out.println(out(s4));
//		System.out.println(in(s0));
//		System.out.println(in(s1));
//		System.out.println(in(s2));
//		System.out.println(in(s3));
//		System.out.println(in(s4));
	}
	
}

package com.izhubo.model;

public enum VlevelType {
	
	V0 , V1;
	
	
	/**
	 * 是否显示VIP的ICON
	 * @param vlevel
	 * @return
	 */
	public static boolean vipIcon(Integer vlevel){
		boolean show = false;
		if(V1.ordinal() == vlevel){
			show = true;
		}
		return show;
	}
	/**
	 * 是否显示VIP的ICON
	 * @param vlevel
	 * @return
	 */
	public static boolean vipIcon(Object vlevel){
		if(null != vlevel){
			return vipIcon(Integer.valueOf(vlevel.toString()));
		}
		return false;
	}
}

package com.izhubo.util;

import com.izhubo.model.VlevelType;

public class VlevelUtils {
	
	/**
	 * 是否显示VIP的ICON
	 * @param vlevel
	 * @return
	 */
	private boolean vipIcon(Integer vlevel){
		boolean show = false;
		if(VlevelType.V1.ordinal() == vlevel){
			show = true;
		}
		return show;
	}
	
}

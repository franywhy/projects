package io.renren.utils;

import java.util.UUID;

public class UUIDUtils {
	/**
	 * 获取没有-的UUID
	 * @return
	 */
	public static final String formatter(){
		UUID uuid = UUID.randomUUID();
		StringBuilder sb = new StringBuilder ();
		sb.append(Long.toHexString(uuid.getMostSignificantBits()));
		sb.append(Long.toHexString(uuid.getLeastSignificantBits()));
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(formatter());
	}
	
}

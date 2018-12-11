package com.izhubo.model;

public enum TopicBunsType {
	
//	0.未打开 1.已打开
	
	未打开 , 已打开;
	
	public static TopicBunsType valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("TopicBunsType Invalid ordinal");
        }
        return values()[ordinal];
    }
	
	public static String vname(int ordinal) {
		return valueOf(ordinal).name();
	}
}

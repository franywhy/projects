package com.izhubo.model;

public enum TopicEndType {
	
//	0.学生结束 1.老师结束 2问题超时
	学生结束 , 老师结束 , 问题超时;
	
	public static TopicEndType valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("TopicEndType Invalid ordinal");
        }
        return values()[ordinal];
    }
	
	public static String vname(int ordinal) {
		return valueOf(ordinal).name();
	}
	
}

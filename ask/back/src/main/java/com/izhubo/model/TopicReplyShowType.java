package com.izhubo.model;

public enum TopicReplyShowType {
	
//	0.所有人可以查看 1.老师学员 2.老师 3.学员
	所有人 , 老师学员 , 老师 , 学员;
	
	public static TopicReplyShowType valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("TopicReplyShowType Invalid ordinal");
        }
        return values()[ordinal];
    }
	
	public static String vname(int ordinal) {
		return valueOf(ordinal).name();
	}
	
}

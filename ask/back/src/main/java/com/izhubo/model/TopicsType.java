package com.izhubo.model;

public enum TopicsType {
	
//	0.待抢答 1.抢答失败 2.抢答成功 3.问题已结束 
	待抢答 , 抢答失败 , 抢答成功 , 问题已结束 ;
	
	
	public static TopicsType valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("TopicsType Invalid ordinal");
        }
        return values()[ordinal];
    }
	
	public static String vname(int ordinal) {
		return valueOf(ordinal).name();
	}
}

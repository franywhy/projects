package com.izhubo.model;

public enum TopicChannelType {
	
//	0.会答  1.题库
	
	会答 , 题库;
	
	public static TopicChannelType valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("TopicChannelType Invalid ordinal");
        }
        return values()[ordinal];
    }
	
	public static String vname(int ordinal) {
		return valueOf(ordinal).name();
	}
	
}

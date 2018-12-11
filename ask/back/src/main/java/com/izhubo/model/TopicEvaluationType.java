package com.izhubo.model;

public enum TopicEvaluationType {
	
//	0.未评价 1.差评 2.好评
	未评价 , 不满意 , 满意 , 非常满意;
	
	public static TopicEvaluationType valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("TopicEvaluationType Invalid ordinal");
        }
        return values()[ordinal];
    }
	public static String vname(int ordinal) {
		return valueOf(ordinal).name();
	}
}

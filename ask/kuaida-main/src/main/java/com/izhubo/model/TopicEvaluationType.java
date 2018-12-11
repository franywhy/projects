package com.izhubo.model;

public enum TopicEvaluationType {
	
//	0.未评价 1.差评 2.好评
	未评价 , 不满意 , 满意 , 很满意;
	
	
	/**
	 * 兼容旧版本 非常满意 改成满意
	 * @param type evaluation_type
	 */
	public static Integer oldTypeInt(int type){
		if(TopicEvaluationType.很满意.ordinal() == type){
			return TopicEvaluationType.很满意.ordinal();
		}
		return type;
	}
}

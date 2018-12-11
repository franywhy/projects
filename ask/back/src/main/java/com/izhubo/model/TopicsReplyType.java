package com.izhubo.model;

/**
 * 回复类型
* @ClassName: TopicsReplyType 
* @Description: 回复类型
* @author shihongjie
* @date 2015年7月27日 下午3:58:40 
*
 */
public enum TopicsReplyType {
	
	文字,图片,语音,系统文字,红包;
	
	public static TopicsReplyType valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("TopicsReplyType Invalid ordinal");
        }
        return values()[ordinal];
    }
	
	public static String vname(int ordinal) {
		return valueOf(ordinal).name();
	}
	
}

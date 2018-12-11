package com.izhubo.model;

/**
 * 直播间类型
 * <p>0：在线咨询,1,课程直播间，2：答疑直播间,3：上岗指导直播间,4：大讲堂,5：员工直播间</p>
 * 2015-01-31
 * @author shihongjie
 *
 */
public enum RoomType {
	
//	0：在线咨询,1,课程直播间，2：答疑直播间,3：上岗指导直播间,4：大讲堂,5：员工直播间
	
	在线咨询,课程直播间,答疑直播间,上岗指导直播间,大讲堂,员工直播间;
	
	public static RoomType valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("RoomType Invalid ordinal");
        }
        return values()[ordinal];
    }
	
	public static void main(String[] args) {
		System.out.println(RoomType.valueOf(1));
	}
}

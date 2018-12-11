package com.izhubo.model;

/**
 * 直播间类型
 * <p>0：验证通过,1,未购买课程，2：非VIP会员,3：非公司员工</p>
 * 2015-01-31
 * @author zhaokun
 *
 */
public enum AuthType {
	
//	0：验证通过,1,未购买课程，2：非VIP会员,3：非公司员
	
	验证通过,未购买课程,非VIP会员,非公司员工;
	
	public static AuthType valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("AuthType Invalid ordinal");
        }
        return values()[ordinal];
    }
	
	public static void main(String[] args) {
		System.out.println(RoomType.valueOf(1));
	}
}
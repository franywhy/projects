package com.izhubo.model;

// 0:免费,1:VIP会员才能看,2:需要报名收费 
public enum CostType {
    
	免费,VIP会员才能看,需要报名收费;
	
	public static CostType valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("AuthType Invalid ordinal");
        }
        return values()[ordinal];
    }
	
	public static void main(String[] args) {
		System.out.println(RoomType.valueOf(1));
	}
}

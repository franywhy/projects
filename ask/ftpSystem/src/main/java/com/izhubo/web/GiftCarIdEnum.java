package com.izhubo.web;


/**
 * 礼物和座驾的id
 * @author wubinjie
 *
 */
public enum GiftCarIdEnum {
	
	ExpCard100(128, 164, 232), 
	ExpCard300(129, 165, 233),
	ExpCard500(1, 168, 234),
	ExpCard1500(1, 171, 235),
	Fly(1,130,152),
	Diamond(108,177,239),
	CarTortoise(1,3, 3), //王八座驾
	CarDonkey(1,5, 5),//毛驴
	CarGreen(1,7, 7),//青牛
	CarGoldenPegasus(1,9,9),    //座驾，金色飞马
	CarBraveTroops(1,4, 4),//貔貅
	CarBeast(1,13, 2),//年兽
	CarWolfRider(1,1, 1),//狼骑
	Lollipop20(1,147,103),//棒棒糖
	Luckybean20(1,143,104),//幸运豆
	LvBag(1,153,119),//LV包
	Icecream(1,134,182),//冰淇淋
	;
	
	private int localId;
	private int betaTestId;
	private int productId;
	
	private GiftCarIdEnum(int localId, int betaTestId, int productId){
		this.localId = localId;
		this.betaTestId = betaTestId;
		this.productId = productId;
	}

	public int getId(ServerTypeEnum serverTypeEnum){
		int id;
		switch(serverTypeEnum){
			default:
			case product:
				id = productId;
				break;
			case betaTest:
				id = betaTestId;
				break;
			case testRemote:
				id = localId;
				break;
		}
		
		return id;
	}
	
	
	public static boolean isExpCard(int giftId, ServerTypeEnum serverTypeEnum){
		boolean expCard = false;
		int expCard1500Id = ExpCard1500.getId(serverTypeEnum);
		int expCard500Id = ExpCard500.getId(serverTypeEnum);
		int expCard300Id = ExpCard300.getId(serverTypeEnum);
		int expCard100Id = ExpCard100.getId(serverTypeEnum);
		if(giftId == expCard100Id || giftId == expCard300Id
				|| giftId == expCard500Id 
				|| giftId == expCard1500Id){
			expCard = true;
		}
		
		return expCard;
	}
}

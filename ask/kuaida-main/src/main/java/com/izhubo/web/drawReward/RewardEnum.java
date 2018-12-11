package com.izhubo.web.drawReward;
import com.izhubo.web.GiftCarIdEnum;
import com.izhubo.web.ServerTypeEnum;
import com.izhubo.web.UserVipEnum;



/**
 * 
 * @author wubinjie, 和前台的序号相对，只能增加不能减少
 *
 */
public enum RewardEnum {
	ThankYou(null, null, null, null, 825, 481, 300,"谢谢参与"), 
	Coin50(50, null, null, null, 102, 410, 581,"50金币"),
	LvBag(300, null, null, 1, 10, 5, 15,"LV包一个"), 
	Lollipop20(null, null, null, 20, 20, 20, 17,"棒棒糖20个"),
	Iphone6(null, null, null, 1, 0, 0, 0,"iphone6一部"), 
	Fly(null, null, null, 1, 0, 0, 0,"飞机"), 
	Icecream10(null, null, null, 10, 20, 20, 17,"冰淇淋10个"), 
	VipYellow9(null,UserVipEnum.Yellow, 9, null, 1, 0, 5,"普通VIP9天"),
	Diamond3(null, null, null, 3, 0, 10, 10,"水晶3个"), 
	Diamond5(null, null, null, 5, 0, 10, 15,"水晶5个"), 
	Broadcast5(null, null, null, 5, 2, 2, 5,"喇叭5个"), 
	Luckybean20(null, null, null, 20, 20, 20, 25,"幸运豆20个"), 
	CarGoldenPegasus3(null, null, 3, null, 0, 2, 10,"金色飞马3天"),
	;
	
	
	private Integer coin; //金币数
	private UserVipEnum vipType;  //VIP类型
	private Integer days;   //座驾或VIP的天数
	private Integer num;    //礼物,钻石,世界播的数量
	private int freeRate; //免费抽奖概率, 1000
	private int fillRate; //充值抽奖概率, 1000
	private int chargeRate; //付费的抽奖概率, 1000
	private String PrizeName; //付费的抽奖概率, 1000
	
	private RewardEnum(Integer coin, UserVipEnum vipType, Integer days, Integer num,
			int freeRate, int fillRate, int chargeRate,String PrizeName){
		this.coin = coin;
		this.vipType = vipType;
		this.days = days;
		this.num = num;
		this.freeRate = freeRate;
		this.fillRate = fillRate;
		this.chargeRate = chargeRate;
		this.PrizeName = PrizeName;	
	}
	
	public Integer getGiftId(ServerTypeEnum serverTypeEnum){
		Integer id = null;
		switch(this){
		default:
			id = null;
			break;
//		case ExpCard1500:
//			id = GiftCarIdEnum.ExpCard1500.getId(serverTypeEnum);
//			break;
		case Fly:
			id = GiftCarIdEnum.Fly.getId(serverTypeEnum);
			break;
		case Diamond3:
			id = GiftCarIdEnum.Diamond.getId(serverTypeEnum);
			break;
		case Diamond5:
			id = GiftCarIdEnum.Diamond.getId(serverTypeEnum);
			break;
			
		}
		
		
		
		return id;
	}
	
	public Integer getCarId(ServerTypeEnum serverTypeEnum) {
		
		Integer id = null;
		
		switch(this){
		default:
			id = null;
			break;
		case CarGoldenPegasus3:
			id = GiftCarIdEnum.CarGoldenPegasus.getId(serverTypeEnum);
			break;
			
		}
		
		return id;
	}

	public Integer getCoin() {
		return coin;
	}

	public void setCoin(Integer coin) {
		this.coin = coin;
	}

	public UserVipEnum getVipType() {
		return vipType;
	}

	public void setVipType(UserVipEnum vipType) {
		this.vipType = vipType;
	}



	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public int getFreeRate() {
		return freeRate;
	}

	public void setFreeRate(int freeRate) {
		this.freeRate = freeRate;
	}

	public int getFillRate() {
		return fillRate;
	}

	public void setFillRate(int fillRate) {
		this.fillRate = fillRate;
	}

	public int getChargeRate() {
		return chargeRate;
	}

	public void setChargeRate(int chargeRate) {
		this.chargeRate = chargeRate;
	}
	public String getPrizeName() {
		return PrizeName;
	}

	public void setPrizeName(String PrizeName) {
		this.PrizeName = PrizeName;
	}
	
	public int getRate(DrawTypeEnum drawType){
		int rate = 0;
		switch(drawType){
		case Free:
			rate = this.freeRate;
			break;
		case Fill:
			rate = this.fillRate;
			break;
		case Charge:
			rate = this.chargeRate;
			break;
		}	
		return rate;
	}
}

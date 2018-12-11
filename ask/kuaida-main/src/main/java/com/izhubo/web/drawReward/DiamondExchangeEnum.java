package com.izhubo.web.drawReward;

import com.izhubo.web.GiftCarIdEnum;
import com.izhubo.web.ServerTypeEnum;

/**
 * 
 * @author lizhuolun
 * 
 */
public enum DiamondExchangeEnum {
	CarTortoise15(30, 15), // 王八座驾
	CarDonkey15(50, 15), // 毛驴
	CarGreen15(80, 15), // 青牛
	CarGoldenPegasus15(100, 15), // 金色飞马
	CarBraveTroops15(120, 15), // 貔貅
	CarBeast15(150, 15), // 年兽
	CarWolfRider15(180, 15), // 狼骑
	;

	private Integer DiamondCount; // 水晶数
	private Integer days; // 座驾或VIP的天数

	private DiamondExchangeEnum(Integer DiamondCount, Integer days) {
		this.DiamondCount = DiamondCount;
		this.days = days;
	}

	public Integer getCarId(ServerTypeEnum serverTypeEnum) {

		Integer id = null;

		switch (this) {
		default:
			id = null;
			break;
		case CarTortoise15:
			id = GiftCarIdEnum.CarTortoise.getId(serverTypeEnum);
			break;
		case CarDonkey15:
			id = GiftCarIdEnum.CarDonkey.getId(serverTypeEnum);
			break;
		case CarGreen15:
			id = GiftCarIdEnum.CarGreen.getId(serverTypeEnum);
			break;
		case CarBraveTroops15:
			id = GiftCarIdEnum.CarBraveTroops.getId(serverTypeEnum);
			break;
		case CarBeast15:
			id = GiftCarIdEnum.CarBeast.getId(serverTypeEnum);
			break;
		case CarWolfRider15:
			id = GiftCarIdEnum.CarWolfRider.getId(serverTypeEnum);
			break;
		case CarGoldenPegasus15:
			id = GiftCarIdEnum.CarGoldenPegasus.getId(serverTypeEnum);
			break;
		}

		return id;
	}

	public Integer getDiamondCount() {
		return DiamondCount;
	}

	public void setDiamondCount(Integer DiamondCount) {
		this.DiamondCount = DiamondCount;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}
}

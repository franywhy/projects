package com.izhubo.web.vo;

import com.izhubo.web.vo.WalletInfoVO.WalletInfoVOData;
import com.wordnik.swagger.annotations.ApiModelProperty;


	public class WalletInfoVO  extends BaseResultVO<WalletInfoVOData>{
		
		
		
		public class WalletInfoVOData {
		@ApiModelProperty(value = "_id")
		private int _id;

	
		@ApiModelProperty(value = "钱包余额")
		private double wallet_balance;
		
		@ApiModelProperty(value = "返现的余额")
		private double wallet_total;
		
		@ApiModelProperty(value = "优惠券数量")
		private int coupon_count;

		public void set_id(int _id){
		this._id = _id;
		}
		public int get_id(){
		return this._id;
		}
		public double getWallet_balance() {
			return wallet_balance;
		}
		public void setWallet_balance(double wallet_balance) {
			this.wallet_balance = wallet_balance;
		}
		public double getWallet_total() {
			return wallet_total;
		}
		public void setWallet_total(double wallet_total) {
			this.wallet_total = wallet_total;
		}
		public int getCoupon_count() {
			return coupon_count;
		}
		public void setCoupon_count(int coupon_count) {
			this.coupon_count = coupon_count;
		}
	
		}
}

package com.hqonline.model;

/**
 * 0.红包 , 1.打赏 , 2.申请提现 , 3.提现失败 
 *
 */
public enum UserIncomType {
	
	红包 , 打赏 , 申请提现 , 提现失败 ;
	
	public static final String getName(final Integer type){
		String s = "";
		if(红包.ordinal() == type){
			s = "红包";
		}else if(打赏.ordinal() == type){
			s = "打赏";
		}else if(申请提现.ordinal() == type){
			s = "提现申请";
		}else if(提现失败.ordinal() == type){
			s = "提现失败,返还金额";
		}else if(打赏.ordinal() == type){
			s = "其他";
		}
		return s;
	}
}

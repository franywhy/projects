package com.izhubo.web.vo;

import com.izhubo.web.vo.UserWalletDetailListVO.UserWalletDetailListVOData;
import com.wordnik.swagger.annotations.ApiModelProperty;




public class UserWalletDetailListVO  extends BasePageResultVO<UserWalletDetailListVOData>{
	
	
	class UserWalletDetailListVOData {

		
		

		/**
		 * id
		 */
		@ApiModelProperty(value = "id")
		private java.lang.Integer id;
		
		/**
		 * 金额
		 */
		@ApiModelProperty(value = "金额")
		private java.math.BigDecimal mmoney;
		
		/**
		 * 获取金额的原因
		 */
		@ApiModelProperty(value = "获取金额的原因")
		private java.lang.String mmoneyDetail;
		
		/**
		 * 获取金额的类型，参考java枚举
		 */
		@ApiModelProperty(value = "获取金额的类型，参考java枚举")
		private java.lang.Integer mmoneyType;
		
		/**
		 * user_id
		 */
		@ApiModelProperty(value = "userId")
		private java.lang.Integer userId;
		
		/**
		 * user_nickname
		 */
		@ApiModelProperty(value = "userNickname")
		private java.lang.String userNickname;
		
		/**
		 * create_time
		 */
		private java.sql.Timestamp createTime;
		


		/**
		 * 获取 id 的属性值
		 * @return id :  id 
		 * @author otom3@163.com
		 */
		public java.lang.Integer getId(){
			return this.id;
		}

		/**
		 * 设置 id 的属性值
		 * @param id :  id 
		 * @author otom3@163.com
		 */
		public void setId(java.lang.Integer id){
			this.id	= id;
		}
		
		/**
		 * 获取 金额 的属性值
		 * @return mmoney :  金额 
		 * @author otom3@163.com
		 */
		public java.math.BigDecimal getMmoney(){
			return this.mmoney;
		}

		/**
		 * 设置 金额 的属性值
		 * @param mmoney :  金额 
		 * @author otom3@163.com
		 */
		public void setMmoney(java.math.BigDecimal mmoney){
			this.mmoney	= mmoney;
		}
		
		/**
		 * 获取 获取金额的原因 的属性值
		 * @return mmoneyDetail :  获取金额的原因 
		 * @author otom3@163.com
		 */
		public java.lang.String getMmoneyDetail(){
			return this.mmoneyDetail;
		}

		/**
		 * 设置 获取金额的原因 的属性值
		 * @param mmoneyDetail :  获取金额的原因 
		 * @author otom3@163.com
		 */
		public void setMmoneyDetail(java.lang.String mmoneyDetail){
			this.mmoneyDetail	= mmoneyDetail;
		}
		
		/**
		 * 获取 获取金额的类型，参考java枚举 的属性值
		 * @return mmoneyType :  获取金额的类型，参考java枚举 
		 * @author otom3@163.com
		 */
		public java.lang.Integer getMmoneyType(){
			return this.mmoneyType;
		}

		/**
		 * 设置 获取金额的类型，参考java枚举 的属性值
		 * @param mmoneyType :  获取金额的类型，参考java枚举 
		 * @author otom3@163.com
		 */
		public void setMmoneyType(java.lang.Integer mmoneyType){
			this.mmoneyType	= mmoneyType;
		}
		
		/**
		 * 获取 user_id 的属性值
		 * @return userId :  user_id 
		 * @author otom3@163.com
		 */
		public java.lang.Integer getUserId(){
			return this.userId;
		}

		/**
		 * 设置 user_id 的属性值
		 * @param userId :  user_id 
		 * @author otom3@163.com
		 */
		public void setUserId(java.lang.Integer userId){
			this.userId	= userId;
		}
		
		/**
		 * 获取 user_nickname 的属性值
		 * @return userNickname :  user_nickname 
		 * @author otom3@163.com
		 */
		public java.lang.String getUserNickname(){
			return this.userNickname;
		}

		/**
		 * 设置 user_nickname 的属性值
		 * @param userNickname :  user_nickname 
		 * @author otom3@163.com
		 */
		public void setUserNickname(java.lang.String userNickname){
			this.userNickname	= userNickname;
		}
		
		/**
		 * 获取 create_time 的属性值
		 * @return createTime :  create_time 
		 * @author otom3@163.com
		 */
		public java.sql.Timestamp getCreateTime(){
			return this.createTime;
		}

		/**
		 * 设置 create_time 的属性值
		 * @param createTime :  create_time 
		 * @author otom3@163.com
		 */
		public void setCreateTime(java.sql.Timestamp createTime){
			this.createTime	= createTime;
		}
		
		

	}
}

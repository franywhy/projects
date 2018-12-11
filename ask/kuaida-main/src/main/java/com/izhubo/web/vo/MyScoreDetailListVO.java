package com.izhubo.web.vo;

import com.izhubo.web.vo.MyScoreDetailListVO.MyScoreDetailListVOData;
import com.wordnik.swagger.annotations.ApiModelProperty;




public class MyScoreDetailListVO  extends BasePageResultVO<MyScoreDetailListVOData>{
	
	
	class MyScoreDetailListVOData {

		
		/**
		 * id
		 */
		@ApiModelProperty(value = "id")
		private java.lang.Integer id;
		
		/**
		 * score
		 */
		@ApiModelProperty(value = "积分分值")
		private java.lang.Float score;
		
		/**
		 * score_detail
		 */
		@ApiModelProperty(value = "获取积分的说明")
		private java.lang.String scoreDetail;
		
		/**
		 * score_gain_type
		 */
		@ApiModelProperty(value = "scoreGainType")
		private java.lang.Integer scoreGainType;
		
		/**
		 * score_type
		 */
		@ApiModelProperty(value = "scoreType")
		private java.lang.Integer scoreType;
		
		/**
		 * user_id
		 */
		@ApiModelProperty(value = "用户id")
		private java.lang.Integer userId;
		
		/**
		 * user_nickname
		 */
		@ApiModelProperty(value = "用户昵称")
		private java.lang.String userNickname;
		
		/**
		 * create_time
		 */
		@ApiModelProperty(value = "创建时间")
		private java.sql.Timestamp createTime;

		public java.lang.Integer getId() {
			return id;
		}

		public void setId(java.lang.Integer id) {
			this.id = id;
		}

		public java.lang.Float getScore() {
			return score;
		}

		public void setScore(java.lang.Float score) {
			this.score = score;
		}

		public java.lang.String getScoreDetail() {
			return scoreDetail;
		}

		public void setScoreDetail(java.lang.String scoreDetail) {
			this.scoreDetail = scoreDetail;
		}

		public java.lang.Integer getScoreGainType() {
			return scoreGainType;
		}

		public void setScoreGainType(java.lang.Integer scoreGainType) {
			this.scoreGainType = scoreGainType;
		}

		public java.lang.Integer getScoreType() {
			return scoreType;
		}

		public void setScoreType(java.lang.Integer scoreType) {
			this.scoreType = scoreType;
		}

		public java.lang.Integer getUserId() {
			return userId;
		}

		public void setUserId(java.lang.Integer userId) {
			this.userId = userId;
		}

		public java.lang.String getUserNickname() {
			return userNickname;
		}

		public void setUserNickname(java.lang.String userNickname) {
			this.userNickname = userNickname;
		}

		public java.sql.Timestamp getCreateTime() {
			return createTime;
		}

		public void setCreateTime(java.sql.Timestamp createTime) {
			this.createTime = createTime;
		}
	}
}

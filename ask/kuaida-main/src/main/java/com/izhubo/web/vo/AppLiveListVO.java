package com.izhubo.web.vo;



import com.wordnik.swagger.annotations.ApiModelProperty;


	public class AppLiveListVO  {
		
		@ApiModelProperty(value = "_id")
		private int _id;


		@ApiModelProperty(value = "是否預約 0：還沒預約 1：已經預約")
		private int live_reservation_state;

		@ApiModelProperty(value = "直播间显示状态 0：尚未开始  1 ：即将开始(开始半小时之前)  2：直播中 3：已经结束")
		private int live_state;
		
		@ApiModelProperty(value = "直播间显示状态 字符")
		private int live_state_text;
		
		@ApiModelProperty(value = "直播间标题")
		private String live_title;
		
		@ApiModelProperty(value = "直播间banner地址")
		private String live_banner_url;
		
		@ApiModelProperty(value = "直播间时间描述")
		private String live_time_detail;
		
		@ApiModelProperty(value = "直播开始时间长整型")
		private Long live_start_time;
		
		@ApiModelProperty(value = "直播结束时间长整型")
		private Long live_end_time;
		
		@ApiModelProperty(value = "直播房间号")
		private String live_num;
		
		@ApiModelProperty(value = "直播id")
		private String live_id;
		
		@ApiModelProperty(value = "直播域名")
		private String live_domain;



		public int getLive_reservation_state() {
			return live_reservation_state;
		}

		public void setLive_reservation_state(int live_reservation_state) {
			this.live_reservation_state = live_reservation_state;
		}

		public int getLive_state() {
			return live_state;
		}

		public void setLive_state(int live_state) {
			this.live_state = live_state;
		}


		
	

		public void set_id(int _id) {
			this._id = _id;
		}

		public int get_id() {
			return this._id;
		}

		public String getLive_title() {
			return live_title;
		}

		public void setLive_title(String live_title) {
			this.live_title = live_title;
		}

		public String getLive_time_detail() {
			return live_time_detail;
		}

		public void setLive_time_detail(String live_time_detail) {
			this.live_time_detail = live_time_detail;
		}

		public Long getLive_start_time() {
			return live_start_time;
		}

		public void setLive_start_time(Long live_start_time) {
			this.live_start_time = live_start_time;
		}

		public Long getLive_end_time() {
			return live_end_time;
		}

		public void setLive_end_time(Long live_end_time) {
			this.live_end_time = live_end_time;
		}

		public String getLive_num() {
			return live_num;
		}

		public void setLive_num(String live_num) {
			this.live_num = live_num;
		}

		public String getLive_id() {
			return live_id;
		}

		public void setLive_id(String live_id) {
			this.live_id = live_id;
		}

		public String getLive_domain() {
			return live_domain;
		}

		public void setLive_domain(String live_domain) {
			this.live_domain = live_domain;
		}

		public String getLive_banner_url() {
			return live_banner_url;
		}

		public void setLive_banner_url(String live_banner_url) {
			this.live_banner_url = live_banner_url;
		}

	

		public int getLive_state_text() {
			return live_state_text;
		}

		public void setLive_state_text(int live_state_text) {
			this.live_state_text = live_state_text;
		}
		
		
		
}

package com.izhubo.web.vo;

import com.izhubo.web.vo.DailyRecommandVO.DailyRecommandVOData;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class DailyRecommandVO extends BaseResultVO<DailyRecommandVOData> {

	class DailyRecommandVOData {
		@ApiModelProperty(value = "_id")
		private int _id;

		@ApiModelProperty(value = "是否推薦首頁   0：不显示默认状态 1:显示在首页")
		private int is_recommend;

		@ApiModelProperty(value = "推薦副標題 ")
		private String recommend_info;

		@ApiModelProperty(value = "推薦標題")
		private String recommend_title;

		@ApiModelProperty(value = "推薦的url 視頻，音頻，文章分別都有對應url")
		private String recommend_infourl;

		@ApiModelProperty(value = "直播的開始時間")
		private String recommend_live_endtime;

		@ApiModelProperty(value = "直播的結束時間")
		private String recommend_live_starttime;

		@ApiModelProperty(value = "推薦的標題圖片")
		private String recommend_picurl;

		@ApiModelProperty(value = "推薦的時間")
		private String recommend_time;

		@ApiModelProperty(value = " 推荐类型：0：直播 1：语音 2：文章信息 3 微课")
		private int recommend_type;
		
		@ApiModelProperty(value = "推荐的标签：文章和语音都会有")
		private String recommend_tag;

		@ApiModelProperty(value = "語音類型的播放時長 單位為秒，請自行轉換為對應格式")
		private int recommend_radio_time;

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
		
		
		
	

		@ApiModelProperty(value = "语音老师姓名")
		private String recommend_radio_teacher_name;

		@ApiModelProperty(value = "语音老师头像")
		private String recommend_radio_teacher_pic;

		@ApiModelProperty(value = "语音或者文章的简介")
		private String summary;
		@ApiModelProperty(value = "语音详情")
		private String recommend_html;
		@ApiModelProperty(value = "语音地址")
		private String voiceurl;
		
	

		public void set_id(int _id) {
			this._id = _id;
		}

		public int get_id() {
			return this._id;
		}

		public void setIs_recommend(int is_recommend) {
			this.is_recommend = is_recommend;
		}

		public int getIs_recommend() {
			return this.is_recommend;
		}

		public void setRecommend_info(String recommend_info) {
			this.recommend_info = recommend_info;
		}

		public String getRecommend_info() {
			return this.recommend_info;
		}

		public void setRecommend_title(String recommend_title) {
			this.recommend_title = recommend_title;
		}

		public String getRecommend_title() {
			return this.recommend_title;
		}

		public void setRecommend_infourl(String recommend_infourl) {
			this.recommend_infourl = recommend_infourl;
		}

		public String getRecommend_infourl() {
			return this.recommend_infourl;
		}

		public void setRecommend_live_endtime(String recommend_live_endtime) {
			this.recommend_live_endtime = recommend_live_endtime;
		}

		public String getRecommend_live_endtime() {
			return this.recommend_live_endtime;
		}

		public void setRecommend_live_starttime(String recommend_live_starttime) {
			this.recommend_live_starttime = recommend_live_starttime;
		}

		public String getRecommend_live_starttime() {
			return this.recommend_live_starttime;
		}

		public void setRecommend_picurl(String recommend_picurl) {
			this.recommend_picurl = recommend_picurl;
		}

		public String getRecommend_picurl() {
			return this.recommend_picurl;
		}

		public void setRecommend_time(String recommend_time) {
			this.recommend_time = recommend_time;
		}

		public String getRecommend_time() {
			return this.recommend_time;
		}

		public void setRecommend_type(int recommend_type) {
			this.recommend_type = recommend_type;
		}

		public int getRecommend_type() {
			return this.recommend_type;
		}

	

		public int getRecommend_radio_time() {
			return recommend_radio_time;
		}

		public void setRecommend_radio_time(int recommend_radio_time) {
			this.recommend_radio_time = recommend_radio_time;
		}

		public int getIs_live() {
			return live_state;
		}

		public void setIs_live(int is_live) {
			this.live_state = is_live;
		}

	

		public String getSummary() {
			return summary;
		}

		public void setSummary(String summary) {
			this.summary = summary;
		}

		public String getRecommend_radio_teacher_pic() {
			return recommend_radio_teacher_pic;
		}

		public void setRecommend_radio_teacher_pic(
				String recommend_radio_teacher_pic) {
			this.recommend_radio_teacher_pic = recommend_radio_teacher_pic;
		}

		public String getRecommend_radio_teacher_name() {
			return recommend_radio_teacher_name;
		}

		public void setRecommend_radio_teacher_name(
				String recommend_radio_teacher_name) {
			this.recommend_radio_teacher_name = recommend_radio_teacher_name;
		}

		public String getRecommend_html() {
			return recommend_html;
		}

		public void setRecommend_html(String recommend_html) {
			this.recommend_html = recommend_html;
		}

		public String getVoiceurl() {
			return voiceurl;
		}

		public void setVoiceurl(String voiceurl) {
			this.voiceurl = voiceurl;
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

		public String getRecommend_tag() {
			return recommend_tag;
		}

		public void setRecommend_tag(String recommend_tag) {
			this.recommend_tag = recommend_tag;
		}

		public int getLive_state_text() {
			return live_state_text;
		}

		public void setLive_state_text(int live_state_text) {
			this.live_state_text = live_state_text;
		}
		
		
	}
}

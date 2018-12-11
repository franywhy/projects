package com.izhubo.web.vo;

import com.izhubo.web.vo.DailyRecommandListVO.DailyRecommandListVOVOData;
import com.wordnik.swagger.annotations.ApiModelProperty;


	public class DailyRecommandListVO  extends BasePageResultVO<DailyRecommandListVOVOData>{
		
		
		
		class DailyRecommandListVOVOData {
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

		@ApiModelProperty(value = " 推荐类型：0：直播 1：语音 2：文章信息")
		private int recommend_type;

		
		@ApiModelProperty(value = "語音類型的播放時長 單位為秒，請自行轉換為對應格式")
		private int recommend_radio_time;
		

		
		@ApiModelProperty(value = "是否預約 0：還沒預約 1：已經預約")
		private int is_reservation;

		public void set_id(int _id){
		this._id = _id;
		}
		public int get_id(){
		return this._id;
		}
		public void setIs_recommend(int is_recommend){
		this.is_recommend = is_recommend;
		}
		public int getIs_recommend(){
		return this.is_recommend;
		}
		public void setRecommend_info(String recommend_info){
		this.recommend_info = recommend_info;
		}
		public String getRecommend_info(){
		return this.recommend_info;
		}
		public void setRecommend_title(String recommend_title){
		this.recommend_title = recommend_title;
		}
		public String getRecommend_title(){
		return this.recommend_title;
		}
		public void setRecommend_infourl(String recommend_infourl){
		this.recommend_infourl = recommend_infourl;
		}
		public String getRecommend_infourl(){
		return this.recommend_infourl;
		}
		public void setRecommend_live_endtime(String recommend_live_endtime){
		this.recommend_live_endtime = recommend_live_endtime;
		}
		public String getRecommend_live_endtime(){
		return this.recommend_live_endtime;
		}
		public void setRecommend_live_starttime(String recommend_live_starttime){
		this.recommend_live_starttime = recommend_live_starttime;
		}
		public String getRecommend_live_starttime(){
		return this.recommend_live_starttime;
		}
		public void setRecommend_picurl(String recommend_picurl){
		this.recommend_picurl = recommend_picurl;
		}
		public String getRecommend_picurl(){
		return this.recommend_picurl;
		}
		public void setRecommend_time(String recommend_time){
		this.recommend_time = recommend_time;
		}
		public String getRecommend_time(){
		return this.recommend_time;
		}
		public void setRecommend_type(int recommend_type){
		this.recommend_type = recommend_type;
		}
		public int getRecommend_type(){
		return this.recommend_type;
		}
		public int getIs_reservation() {
			return is_reservation;
		}
		public void setIs_reservation(int is_reservation) {
			this.is_reservation = is_reservation;
		}
		public int getRecommend_radio_time() {
			return recommend_radio_time;
		}
		public void setRecommend_radio_time(int recommend_radio_time) {
			this.recommend_radio_time = recommend_radio_time;
		}
		}
}

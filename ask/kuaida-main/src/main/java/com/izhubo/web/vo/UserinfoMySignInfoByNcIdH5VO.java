package com.izhubo.web.vo;

import java.util.List;

import com.izhubo.web.vo.UserinfoMySignInfoByNcIdH5VO.UserinfoMySignInfoByNcIdH5VOData;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class UserinfoMySignInfoByNcIdH5VO extends
		BaseResultVO<UserinfoMySignInfoByNcIdH5VOData> {

	class Item {
		@ApiModelProperty(value = "直播课程名称")
		private String name;

		@ApiModelProperty(value = "直播老师名称")
		private String teacher_name;

		@ApiModelProperty(value = "直播地址(为空隐藏跳转按钮)>http://113.108.202.180:1708/pc_live_param")
		private String live_url;

		@ApiModelProperty(value = "直播时间>2016-07-16 00:10")
		private String time;

		@ApiModelProperty(value = "直播状态>未开始")
		private String state;

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}


		public String getTeacher_name() {
			return teacher_name;
		}

		public void setTeacher_name(String teacher_name) {
			this.teacher_name = teacher_name;
		}

		public void setLive_url(String live_url) {
			this.live_url = live_url;
		}

		public String getLive_url() {
			return this.live_url;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getTime() {
			return this.time;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getState() {
			return this.state;
		}

	}

	class LiveList {
		@ApiModelProperty(value = "直播列表")
		private List<Item> item;

		@ApiModelProperty(value = "直播系列名称>考前冲刺基础系列")
		private String name;

		public void setItem(List<Item> item) {
			this.item = item;
		}

		public List<Item> getItem() {
			return this.item;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

	}

	class PlanList {
		@ApiModelProperty(value = "课程列表")
		private String teacher_name;

		@ApiModelProperty(value = "题库地址(为空隐藏跳转按钮)>http://113.108.202.180:1708/pc_live_param")
		private String tiku_url;

		@ApiModelProperty(value = "名称")
		private String name;

		@ApiModelProperty(value = "实训地址(为空隐藏跳转按钮)>http://113.108.202.180:1708/pc_live_param")
		private String shixun_url;

		@ApiModelProperty(value = "直播地址(为空隐藏跳转按钮)>http://113.108.202.180:1708/pc_live_param")
		private String live_url;

		@ApiModelProperty(value = "状态>未开始")
		private String state;

		@ApiModelProperty(value = "时间>2016-07-16 00:10")
		private String time;

		public void setTeacher_name(String teacher_name) {
			this.teacher_name = teacher_name;
		}

		public String getTeacher_name() {
			return this.teacher_name;
		}

		public void setTiku_url(String tiku_url) {
			this.tiku_url = tiku_url;
		}

		public String getTiku_url() {
			return this.tiku_url;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public void setShixun_url(String shixun_url) {
			this.shixun_url = shixun_url;
		}

		public String getShixun_url() {
			return this.shixun_url;
		}

		public void setLive_url(String live_url) {
			this.live_url = live_url;
		}

		public String getLive_url() {
			return this.live_url;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getState() {
			return this.state;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getTime() {
			return this.time;
		}

	}

	class DownList {
		@ApiModelProperty(value = "线下课程名称")
		private String course_name;

		@ApiModelProperty(value = "线下课程列表")
		private List<PlanList> planList;

		public void setCourse_name(String course_name) {
			this.course_name = course_name;
		}

		public String getCourse_name() {
			return this.course_name;
		}

		public void setPlanList(List<PlanList> planList) {
			this.planList = planList;
		}

		public List<PlanList> getPlanList() {
			return this.planList;
		}

	}

	class OnlineList {
		@ApiModelProperty(value = "线上课程名称")
		private String course_name;

		@ApiModelProperty(value = "线上课程列表")
		private List<PlanList> planList;

		public void setCourse_name(String course_name) {
			this.course_name = course_name;
		}

		public String getCourse_name() {
			return this.course_name;
		}

		public void setPlanList(List<PlanList> planList) {
			this.planList = planList;
		}

		public List<PlanList> getPlanList() {
			return this.planList;
		}

	}

	class UserinfoMySignInfoByNcIdH5VOData {
		private String commodity_id;
		@ApiModelProperty(value = "报名表id")
		private String nc_sign_id;

		@ApiModelProperty(value = "线上课程列表")
		private List<OnlineList> onlineList;

		@ApiModelProperty(value = "线上课数量")
		private int crouse_online_num;

		@ApiModelProperty(value = "考前冲刺列表")
		private List<LiveList> liveList;

		@ApiModelProperty(value = "进度>80.80")
		private int progress;

		@ApiModelProperty(value = "课程总数>50")
		private int crouse_num;

		@ApiModelProperty(value = "线下课程列表")
		private List<DownList> downList;

		@ApiModelProperty(value = "商品名称>零起步会计培训方案")
		private String commodity_name;

		@ApiModelProperty(value = "线下课程数量>30")
		private int crouse_down_num;

		@ApiModelProperty(value = "考前冲刺数量>50")
		private int liveclass_num;
		
		public int getLiveclass_num() {
			return liveclass_num;
		}

		public void setLiveclass_num(int liveclass_num) {
			this.liveclass_num = liveclass_num;
		}

		public void setCommodity_id(String commodity_id) {
			this.commodity_id = commodity_id;
		}

		public String getCommodity_id() {
			return this.commodity_id;
		}

		public void setOnlineList(List<OnlineList> onlineList) {
			this.onlineList = onlineList;
		}

		public List<OnlineList> getOnlineList() {
			return this.onlineList;
		}

		public void setCrouse_online_num(int crouse_online_num) {
			this.crouse_online_num = crouse_online_num;
		}

		public String getNc_sign_id() {
			return nc_sign_id;
		}

		public void setNc_sign_id(String nc_sign_id) {
			this.nc_sign_id = nc_sign_id;
		}

		public int getCrouse_online_num() {
			return this.crouse_online_num;
		}

		public void setLiveList(List<LiveList> liveList) {
			this.liveList = liveList;
		}

		public List<LiveList> getLiveList() {
			return this.liveList;
		}

		public void setProgress(int progress) {
			this.progress = progress;
		}

		public int getProgress() {
			return this.progress;
		}

		public void setCrouse_num(int crouse_num) {
			this.crouse_num = crouse_num;
		}

		public int getCrouse_num() {
			return this.crouse_num;
		}

		public void setDownList(List<DownList> downList) {
			this.downList = downList;
		}

		public List<DownList> getDownList() {
			return this.downList;
		}

		public void setCommodity_name(String commodity_name) {
			this.commodity_name = commodity_name;
		}

		public String getCommodity_name() {
			return this.commodity_name;
		}

		public void setCrouse_down_num(int crouse_down_num) {
			this.crouse_down_num = crouse_down_num;
		}

		public int getCrouse_down_num() {
			return this.crouse_down_num;
		}

	}

}

package com.izhubo.web.vo;

import java.util.List;

import com.izhubo.web.vo.AHCommoditysCommodityInfoVO.AHCommoditysCommodityInfoVOData;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class AHCommoditysCommodityInfoVO extends
		BaseResultVO<AHCommoditysCommodityInfoVOData> {

	class AHCommoditysCommodityInfoVOData {
		
		@ApiModelProperty(value = "id")
		private String _id;

		@ApiModelProperty(value = "名称")
		private String name;

		@ApiModelProperty(value = "图片")
		private String app_photo;

		@ApiModelProperty(value = "实际价格")
		private int price;

		@ApiModelProperty(value = "简介-html文本")
		private String content_app;

		@ApiModelProperty(value = "商品类型(0.未分类 1.套餐商品 2.经营商品 3.管理会计 4.会计考证 5.会计学历)-暂时无业务处理")
		private int type;

		@ApiModelProperty(value = "月供价格")
		private int monthprice;

		@ApiModelProperty(value = "适用对象")
		private String reader;
		
		@ApiModelProperty(value = "包含课次")
		private String classtime;
		
		@ApiModelProperty(value = "学习周期")
		private String learntime;
		
		@ApiModelProperty(value = "上课方式")
		private String classmode;

		@ApiModelProperty(value = "视频地址 http://asdfasdf")
		private String try_video;

		@ApiModelProperty(value = "课程大纲集合")
		private List<Commodity_type_list> commodity_type_list;

		public void setTry_video(String try_video) {
			this.try_video = try_video;
		}

		public String getTry_video() {
			return this.try_video;
		}

		public void set_id(String _id) {
			this._id = _id;
		}

		public String get_id() {
			return this._id;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

	
		public String getApp_photo() {
			return app_photo;
		}

		public void setApp_photo(String app_photo) {
			this.app_photo = app_photo;
		}

		public void setPrice(int price) {
			this.price = price;
		}

		public int getPrice() {
			return this.price;
		}

		public void setContent_app(String content_app) {
			this.content_app = content_app;
		}

		public String getContent_app() {
			return this.content_app;
		}

		public void setType(int type) {
			this.type = type;
		}

		public int getType() {
			return this.type;
		}

		public void setMonthprice(int monthprice) {
			this.monthprice = monthprice;
		}

		public int getMonthprice() {
			return this.monthprice;
		}

		public void setReader(String reader) {
			this.reader = reader;
		}

		public String getReader() {
			return this.reader;
		}

		public void setClasstime(String classtime) {
			this.classtime = classtime;
		}

		public String getClasstime() {
			return this.classtime;
		}

		public void setLearntime(String learntime) {
			this.learntime = learntime;
		}

		public String getLearntime() {
			return this.learntime;
		}

		public void setClassmode(String classmode) {
			this.classmode = classmode;
		}

		public String getClassmode() {
			return this.classmode;
		}

		public void setCommodity_type_list(
				List<Commodity_type_list> commodity_type_list) {
			this.commodity_type_list = commodity_type_list;
		}

		public List<Commodity_type_list> getCommodity_type_list() {
			return this.commodity_type_list;
		}

	}

	class Course_list {
		@ApiModelProperty(value = "课程id")
		private String commodity_courses_id;

		@ApiModelProperty(value = "排序 暂无业务作用")
		private int sort;

		@ApiModelProperty(value = "课程名称")
		private String show_name;

		@ApiModelProperty(value = "NCID")
		private String nc_course_id;

		@ApiModelProperty(value = "是否是线上 0.否 1.是")
		private int is_online;
		
		@ApiModelProperty(value = "面授/直播")
		private String line_name;

		public void setCommodity_courses_id(String commodity_courses_id) {
			this.commodity_courses_id = commodity_courses_id;
		}

		public String getCommodity_courses_id() {
			return this.commodity_courses_id;
		}

		public void setSort(int sort) {
			this.sort = sort;
		}

		public int getSort() {
			return this.sort;
		}
		

		public String getLine_name() {
			return line_name;
		}

		public void setLine_name(String line_name) {
			this.line_name = line_name;
		}

		public void setShow_name(String show_name) {
			this.show_name = show_name;
		}

		public String getShow_name() {
			return this.show_name;
		}

		public void setNc_course_id(String nc_course_id) {
			this.nc_course_id = nc_course_id;
		}

		public String getNc_course_id() {
			return this.nc_course_id;
		}

		public void setIs_online(int is_online) {
			this.is_online = is_online;
		}

		public int getIs_online() {
			return this.is_online;
		}

	}

	class Commodity_type_list {
		@ApiModelProperty(value = "分组名称")
		private String name;

		@ApiModelProperty(value = "排序-暂无业务作用")
		private int sort;

		@ApiModelProperty(value = "课程集合")
		private List<Course_list> course_list;

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public void setSort(int sort) {
			this.sort = sort;
		}

		public int getSort() {
			return this.sort;
		}

		public void setCourse_list(List<Course_list> course_list) {
			this.course_list = course_list;
		}

		public List<Course_list> getCourse_list() {
			return this.course_list;
		}

	}

}

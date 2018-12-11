package com.izhubo.web.vo;

import java.util.List;

import com.izhubo.web.vo.CommodityCommodityPageInfo.CommodityCommodityPageInfoData;
import com.wordnik.swagger.annotations.ApiModelProperty;


public class CommodityCommodityPageInfo extends BaseResultVO<CommodityCommodityPageInfoData> {

	
	class CommodityCommodityPageInfoData {
		@ApiModelProperty(value = "商品id")
		private String _id;
		
		@ApiModelProperty(value = "code")
		private String code;
		
		@ApiModelProperty(value = "名称")
		private String name;
		
		@ApiModelProperty(value = "商品名简称:入门级,实物级")
		private String short_name;

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

		@ApiModelProperty(value = "图片地址")
		private String photo;
		
		@ApiModelProperty(value = "icon图片地址")
		private String icon_url;

		@ApiModelProperty(value = "是否热门标志-默认选中第一热门标志")
		private int is_hot;
		
		

		@ApiModelProperty(value = "课程大纲")
		private List<Commodity_type_list> commodity_type_list ;

		@ApiModelProperty(value = "省市校区价格")
		private List<School_price> school_price ;
		
		
		@ApiModelProperty(value = "试听视频的地址")
		private String try_video;
		
		public String getTry_video() {
			return try_video;
		}

		public void setTry_video(String try_video) {
			this.try_video = try_video;
		}
		
		
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
		
	
		
		public String getShort_name() {
			return short_name;
		}

		public void setShort_name(String short_name) {
			this.short_name = short_name;
		}

		public void setPhoto(String photo){
			this.photo = photo;
		}
		
		public String getPhoto(){
			return this.photo;
		}
		
		
		public void set_id(String _id){
			this._id = _id;
		}
		public String get_id(){
			return this._id;
		}
		public void setName(String name){
			this.name = name;
		}
		public String getName(){
			return this.name;
		}
		public void setMonthprice(int monthprice){
			this.monthprice = monthprice;
		}
		public int getMonthprice(){
			return this.monthprice;
		}
		public void setReader(String reader){
			this.reader = reader;
		}
		public String getReader(){
			return this.reader;
		}
		public void setClasstime(String classtime){
			this.classtime = classtime;
		}
		public String getClasstime(){
			return this.classtime;
		}
		public void setLearntime(String learntime){
			this.learntime = learntime;
		}
		public String getLearntime(){
			return this.learntime;
		}
		public void setClassmode(String classmode){
			this.classmode = classmode;
		}
		public String getClassmode(){
			return this.classmode;
		}
		public void setIcon_url(String icon_url){
			this.icon_url = icon_url;
		}
		public String getIcon_url(){
			return this.icon_url;
		}
		public void setIs_hot(int is_hot){
			this.is_hot = is_hot;
		}
		public int getIs_hot(){
			return this.is_hot;
		}
		public void setCommodity_type_list(List<Commodity_type_list> commodity_type_list){
			this.commodity_type_list = commodity_type_list;
		}
		public List<Commodity_type_list> getCommodity_type_list(){
			return this.commodity_type_list;
		}
		public void setSchool_price(List<School_price> school_price){
			this.school_price = school_price;
		}
		public List<School_price> getSchool_price(){
			return this.school_price;
		}
	}
	
	class Course_list {
		@ApiModelProperty(value = "id")
		private String commodity_courses_id;

		@ApiModelProperty(value = "排序")
		private int sort;

		@ApiModelProperty(value = "商品名称")
		private String show_name;

		@ApiModelProperty(value = "NCid")
		private String nc_course_id;

		@ApiModelProperty(value = "是否是线上 0.否 1.是")
		private int is_online;

		@ApiModelProperty(value = "试听地址 无则不显示按钮")
		private String try_video;

		@ApiModelProperty(value = "题库地址 无则不显示按钮")
		private String tiku_url;

		@ApiModelProperty(value = "实训 无则不显示按钮")
		private String shixu_url;
		
	
		public void setCommodity_courses_id(String commodity_courses_id){
			this.commodity_courses_id = commodity_courses_id;
		}
		public String getCommodity_courses_id(){
			return this.commodity_courses_id;
		}
		public void setSort(int sort){
			this.sort = sort;
		}
		public int getSort(){
			return this.sort;
		}
		public void setShow_name(String show_name){
			this.show_name = show_name;
		}
		public String getShow_name(){
			return this.show_name;
		}
		public void setNc_course_id(String nc_course_id){
			this.nc_course_id = nc_course_id;
		}
		public String getNc_course_id(){
			return this.nc_course_id;
		}
		public void setIs_online(int is_online){
			this.is_online = is_online;
		}
		public int getIs_online(){
			return this.is_online;
		}
		public void setTry_video(String try_video){
			this.try_video = try_video;
		}
		public String getTry_video(){
			return this.try_video;
		}
		public void setTiku_url(String tiku_url){
			this.tiku_url = tiku_url;
		}
		public String getTiku_url(){
			return this.tiku_url;
		}
		public void setShixu_url(String shixu_url){
			this.shixu_url = shixu_url;
		}
		public String getShixu_url(){
			return this.shixu_url;
		}
	}
	class Commodity_type_list {
		@ApiModelProperty(value = "分类名称")
		private String name;

		@ApiModelProperty(value = "排序-无业务作用")
		private int sort;

		@ApiModelProperty(value = "课程列表")
		private List<Course_list> course_list ;

		public void setName(String name){
			this.name = name;
		}
		public String getName(){
			return this.name;
		}
		public void setSort(int sort){
			this.sort = sort;
		}
		public int getSort(){
			return this.sort;
		}
		public void setCourse_list(List<Course_list> course_list){
			this.course_list = course_list;
		}
		public List<Course_list> getCourse_list(){
			return this.course_list;
		}
	}
	class School {
		@ApiModelProperty(value = "id")
		private String _id;

		@ApiModelProperty(value = "校区编码")
		private String school_code;

		@ApiModelProperty(value = "课程id")
		private String commodity_id;

		@ApiModelProperty(value = "校区价格")
		private int normal_price;

		@ApiModelProperty(value = "校区名称")
		private String school_name;

		public void set_id(String _id){
			this._id = _id;
		}
		public String get_id(){
			return this._id;
		}
		public void setSchool_code(String school_code){
			this.school_code = school_code;
		}
		public String getSchool_code(){
			return this.school_code;
		}
		public void setCommodity_id(String commodity_id){
			this.commodity_id = commodity_id;
		}
		public String getCommodity_id(){
			return this.commodity_id;
		}
		public void setNormal_price(int normal_price){
			this.normal_price = normal_price;
		}
		public int getNormal_price(){
			return this.normal_price;
		}
		public void setSchool_name(String school_name){
			this.school_name = school_name;
		}
		public String getSchool_name(){
			return this.school_name;
		}
	}
	class City {
		@ApiModelProperty(value = "城市编码")
		private String code;

		@ApiModelProperty(value = "校区集合")
		private List<School> school ;

		@ApiModelProperty(value = "城市名称")
		private String name;

		public void setCode(String code){
			this.code = code;
		}
		public String getCode(){
			return this.code;
		}
		public void setSchool(List<School> school){
			this.school = school;
		}
		public List<School> getSchool(){
			return this.school;
		}
		public void setName(String name){
			this.name = name;
		}
		public String getName(){
			return this.name;
		}
	}
	class School_price {
		@ApiModelProperty(value = "省编码")
		private String code;

		@ApiModelProperty(value = "城市集合")
		private List<City> city ;

		@ApiModelProperty(value = "省名称")
		private String name;

		public void setCode(String code){
			this.code = code;
		}
		public String getCode(){
			return this.code;
		}
		public void setCity(List<City> city){
			this.city = city;
		}
		public List<City> getCity(){
			return this.city;
		}
		public void setName(String name){
			this.name = name;
		}
		public String getName(){
			return this.name;
		}
	}
	
}

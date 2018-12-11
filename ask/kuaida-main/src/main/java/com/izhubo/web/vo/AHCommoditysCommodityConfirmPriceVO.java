package com.izhubo.web.vo;

import java.util.List;

import com.izhubo.web.vo.AHCommoditysCommodityConfirmPriceVO.AHCommoditysCommodityConfirmPriceVOData;
import com.wordnik.swagger.annotations.ApiModelProperty;
/**
 * 购买详情页1
 * @author Administrator
 *
 */
public class AHCommoditysCommodityConfirmPriceVO extends
		BaseResultVO<AHCommoditysCommodityConfirmPriceVOData> {

	class AHCommoditysCommodityConfirmPriceVOData {
		
		@ApiModelProperty(value = "商品类型 1.套餐商品 2.非套餐商品")
		private int ctype;
		
		@ApiModelProperty(value = "商品集合")
		private List<CommodityList> commodityList;

		public void setCtype(int ctype) {
			this.ctype = ctype;
		}

		public int getCtype() {
			return this.ctype;
		}

		public void setCommodityList(List<CommodityList> commodityList) {
			this.commodityList = commodityList;
		}

		public List<CommodityList> getCommodityList() {
			return this.commodityList;
		}

	}

	class School {
		@ApiModelProperty(value = "校区id")
		private String _id;

		@ApiModelProperty(value = "校区編碼")
		private String school_code;

		@ApiModelProperty(value = "商品id")
		private String commodity_id;

		@ApiModelProperty(value = "价格")
		private int normal_price;

		@ApiModelProperty(value = "校区名称")
		private String school_name;

		public void set_id(String _id) {
			this._id = _id;
		}

		public String get_id() {
			return this._id;
		}

		public void setSchool_code(String school_code) {
			this.school_code = school_code;
		}

		public String getSchool_code() {
			return this.school_code;
		}

		public void setCommodity_id(String commodity_id) {
			this.commodity_id = commodity_id;
		}

		public String getCommodity_id() {
			return this.commodity_id;
		}

		public void setNormal_price(int normal_price) {
			this.normal_price = normal_price;
		}

		public int getNormal_price() {
			return this.normal_price;
		}

		public void setSchool_name(String school_name) {
			this.school_name = school_name;
		}

		public String getSchool_name() {
			return this.school_name;
		}

	}

	class City {
		@ApiModelProperty(value = "市编码")
		private String code;

		@ApiModelProperty(value = "校区集合")
		private List<School> school;

		@ApiModelProperty(value = "市名称")
		private String name;

		public void setCode(String code) {
			this.code = code;
		}

		public String getCode() {
			return this.code;
		}

		public void setSchool(List<School> school) {
			this.school = school;
		}

		public List<School> getSchool() {
			return this.school;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

	}

	class School_price {
		@ApiModelProperty(value = "省编码")
		private String code;

		@ApiModelProperty(value = "市集合")
		private List<City> city;

		@ApiModelProperty(value = "省名称")
		private String name;

		public void setCode(String code) {
			this.code = code;
		}

		public String getCode() {
			return this.code;
		}

		public void setCity(List<City> city) {
			this.city = city;
		}

		public List<City> getCity() {
			return this.city;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

	}

	class CommodityList {
		@ApiModelProperty(value = "商品id")
		private String _id;

		@ApiModelProperty(value = "商品名称")
		private String name;
		
		@ApiModelProperty(value = "商品简称")
		private String short_name;

		@ApiModelProperty(value = "图片地址")
		private String app_photo;

		@ApiModelProperty(value = "价格")
		private int price;

		@ApiModelProperty(value = "类型")
		private int type;

		@ApiModelProperty(value = "月供")
		private int monthprice;

		@ApiModelProperty(value = "适用对象")
		private String reader;

		@ApiModelProperty(value = "包含课次")
		private String classtime;

		@ApiModelProperty(value = "学习周期")
		private String learntime;

		@ApiModelProperty(value = "上课方式")
		private String classmode;

		@ApiModelProperty(value = "价格集合-省")
		private List<School_price> school_price;
		
		

		public String getShort_name() {
			return short_name;
		}

		public void setShort_name(String short_name) {
			this.short_name = short_name;
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

		public void setSchool_price(List<School_price> school_price) {
			this.school_price = school_price;
		}

		public List<School_price> getSchool_price() {
			return this.school_price;
		}

	}

}

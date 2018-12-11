package com.izhubo.web.vo;

import java.util.List;

import com.izhubo.web.vo.AHCommoditysCommodityPriceListVO.AHCommoditysCommodityPriceListVOData;
import com.wordnik.swagger.annotations.ApiModelProperty;
/**
 * 获取商品价格列表
 * @author Administrator
 *
 */
public class AHCommoditysCommodityPriceListVO extends
		BaseResultVO<AHCommoditysCommodityPriceListVOData> {

	class AHCommoditysCommodityPriceListVOData {
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

	
}

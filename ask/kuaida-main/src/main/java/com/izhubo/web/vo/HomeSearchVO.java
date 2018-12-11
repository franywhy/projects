package com.izhubo.web.vo;

import com.izhubo.web.vo.HomeSearchVO.HomeSearchVOData;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class HomeSearchVO extends BasePageResultVO<HomeSearchVOData> {
	

	class HomeSearchVOData {
		@ApiModelProperty(value = "_id")
		private String _id;

		@ApiModelProperty(value = "商品名")
		private String name;

		@ApiModelProperty(value = "商品图片地址")
		private String photo;

		@ApiModelProperty(value = "商品价格")
		private double price;

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

		public void setPhoto(String photo) {
			this.photo = photo;
		}

		public String getPhoto() {
			return this.photo;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public double getPrice() {
			return this.price;
		}

	}

}

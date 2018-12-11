package com.izhubo.web.vo;

import java.util.List;

import com.izhubo.web.vo.AHHomeCommodityListVO.AHHomeCommodityListVOData;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class AHHomeCommodityListVO extends BaseResultVO<AHHomeCommodityListVOData> {

	class AHHomeCommodityListVOData {
		
		@ApiModelProperty(value = "单项商品集合")
		private List<SingleListHome> singleListHome;
		
		@ApiModelProperty(value = "套餐商品集合")
		private List<PackageListHome> packageListHome;

		public void setSingleListHome(List<SingleListHome> singleListHome) {
			this.singleListHome = singleListHome;
		}

		public List<SingleListHome> getSingleListHome() {
			return this.singleListHome;
		}

		public void setPackageListHome(List<PackageListHome> packageListHome) {
			this.packageListHome = packageListHome;
		}

		public List<PackageListHome> getPackageListHome() {
			return this.packageListHome;
		}

	}

	class SingleListHome {
		
		@ApiModelProperty(value = "_id")
		private String _id;

		@ApiModelProperty(value = "商品名称")
		private String name;
		
		@ApiModelProperty(value = "商品图片地址")
		private String photo;

		@ApiModelProperty(value = "商品价格")
		private double price;
		
		@ApiModelProperty(value = "小图地址")
		private String thumbnail;
		
		@ApiModelProperty(value = "url")
		private String url;
		
		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
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

		public String getThumbnail() {
			return thumbnail;
		}

		public void setThumbnail(String thumbnail) {
			this.thumbnail = thumbnail;
		}
		
		

	}

	class PackageListHome {
		
		@ApiModelProperty(value = "_id")
		private String _id;
		
		@ApiModelProperty(value = "商品名称")
		private String name;
		
		@ApiModelProperty(value = "跳转地址")
		private String url;

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

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
		
		
	}

}

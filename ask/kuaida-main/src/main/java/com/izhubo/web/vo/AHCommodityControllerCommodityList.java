package com.izhubo.web.vo;

import java.util.List;

import com.izhubo.web.vo.AHCommodityControllerCommodityList.AHCommodityControllerCommodityListData;
import com.izhubo.web.vo.CommodityListVO.BannerList;
import com.izhubo.web.vo.CommodityListVO.Item;
import com.izhubo.web.vo.CommodityListVO.Tab0;
import com.izhubo.web.vo.CommodityListVO.Tab1;
import com.izhubo.web.vo.CommodityListVO.Tab2;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class AHCommodityControllerCommodityList extends
		BaseResultVO<AHCommodityControllerCommodityListData> {

	public class Tab1 {

		@ApiModelProperty(value = "Tab1集合")
		private List<Item> item;

		@ApiModelProperty(value = "Tab1名称>会计考证")
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

	class Tab2 {

		@ApiModelProperty(value = "Tab2集合")
		private List<Item> item;

		@ApiModelProperty(value = "Tab2名称>经营会计")
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

	class Item {

		@ApiModelProperty(value = "商品id")
		private String _id;

		@ApiModelProperty(value = "图片地址")
		private String app_thumbnail;

		@ApiModelProperty(value = "试题地址")
		private String try_video_app;

		@ApiModelProperty(value = "价格")
		private String price;

		@ApiModelProperty(value = "原价")
		private String original_price;

		@ApiModelProperty(value = "商品名称")
		private String name;

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getOriginal_price() {
			return original_price;
		}

		public void setOriginal_price(String original_price) {
			this.original_price = original_price;
		}

		public void set_id(String _id) {
			this._id = _id;
		}

		public String get_id() {
			return this._id;
		}

		public String getApp_thumbnail() {
			return app_thumbnail;
		}

		public void setApp_thumbnail(String app_thumbnail) {
			this.app_thumbnail = app_thumbnail;
		}

		public String getTry_video_app() {
			return try_video_app;
		}

		public void setTry_video_app(String try_video_app) {
			this.try_video_app = try_video_app;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

	}

	class Tab0 {
		@ApiModelProperty(value = "Tab0集合")
		private List<Item> item;
		@ApiModelProperty(value = "Tab0名称>会计上岗")
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

	class BannerList {

		@ApiModelProperty(value = "id")
		private String _id;

		@ApiModelProperty(value = "banner名称 鼠标悬停名称")
		private String name;

		@ApiModelProperty(value = "大图片地址")
		private String app_pic;

		@ApiModelProperty(value = "点击跳转地址")
		private String app_url;

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

		public String getApp_pic() {
			return app_pic;
		}

		public void setApp_pic(String app_pic) {
			this.app_pic = app_pic;
		}

		public String getApp_url() {
			return app_url;
		}

		public void setApp_url(String app_url) {
			this.app_url = app_url;
		}

	}

	class AHCommodityControllerCommodityListData {

		@ApiModelProperty(value = "顶部banner集合")
		private List<BannerList> bannerList;

		@ApiModelProperty(value = "tab1集合>会计考证")
		private Tab1 tab1;

		@ApiModelProperty(value = "tab2集合>经营会计")
		private Tab2 tab2;

		@ApiModelProperty(value = "tab0集合>会计上岗")
		private Tab0 tab0;

		public void setBannerList(List<BannerList> bannerList) {
			this.bannerList = bannerList;
		}

		public List<BannerList> getBannerList() {
			return this.bannerList;
		}

		public void setTab1(Tab1 tab1) {
			this.tab1 = tab1;
		}

		public Tab1 getTab1() {
			return this.tab1;
		}

		public void setTab2(Tab2 tab2) {
			this.tab2 = tab2;
		}

		public Tab2 getTab2() {
			return this.tab2;
		}

		public void setTab0(Tab0 tab0) {
			this.tab0 = tab0;
		}

		public Tab0 getTab0() {
			return this.tab0;
		}

	}

}

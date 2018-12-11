package com.izhubo.web.vo;

import java.util.List;

import com.izhubo.web.vo.CommodityListVO.CommodityListVOData;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class CommodityListVO extends BaseResultVO<CommodityListVOData> {

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
		private String thumbnail;

		@ApiModelProperty(value = "试题地址")
		private String try_video;

		@ApiModelProperty(value = "价格")
		private String price;

		@ApiModelProperty(value = "原价")
		private String original_price;

		@ApiModelProperty(value = "商品名称")
		private String name;
		
		@ApiModelProperty(value = "推荐标志 0:正常 1:推荐 ")
		private Integer is_hot;
		
		@ApiModelProperty(value = "类型: 0.未分类 ,1.会计上岗 , 2.会计考证 , 3.会计学历 , 4.经营会计 5.会计上岗_猎才计划")
		private Integer type;
		
		
		
		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

		public Integer getIs_hot() {
			return is_hot;
		}

		public void setIs_hot(Integer is_hot) {
			this.is_hot = is_hot;
		}

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

		
		public String getThumbnail() {
			return thumbnail;
		}

		public void setThumbnail(String thumbnail) {
			this.thumbnail = thumbnail;
		}

		public void setTry_video(String try_video) {
			this.try_video = try_video;
		}

		public String getTry_video() {
			return this.try_video;
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
		private String pc_pic;

		@ApiModelProperty(value = "小图片地址")
		private String pc_icon;

		@ApiModelProperty(value = "点击跳转地址")
		private String pc_url;

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

		public void setPc_pic(String pc_pic) {
			this.pc_pic = pc_pic;
		}

		public String getPc_pic() {
			return this.pc_pic;
		}

		public void setPc_icon(String pc_icon) {
			this.pc_icon = pc_icon;
		}

		public String getPc_icon() {
			return this.pc_icon;
		}

		public void setPc_url(String pc_url) {
			this.pc_url = pc_url;
		}

		public String getPc_url() {
			return this.pc_url;
		}

	}

	class CommodityListVOData {

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

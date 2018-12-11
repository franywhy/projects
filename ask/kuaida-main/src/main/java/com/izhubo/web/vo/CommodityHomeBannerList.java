package com.izhubo.web.vo;

import com.izhubo.web.vo.CommodityHomeBannerList.CommodityHomeBannerListData;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 首页推荐列表
 * @author Administrator
 *
 */
public class CommodityHomeBannerList extends
		BaseResultVO<CommodityHomeBannerListData> {

	class CommodityHomeBannerListData {
		
		@ApiModelProperty(value = "id")
		private String _id;
		
		@ApiModelProperty(value = "跳转url")
		private String url;

		@ApiModelProperty(value = "小图")
		private String icon;

		@ApiModelProperty(value = "大图")
		private String pic;

		@ApiModelProperty(value = "标题")
		private String title;

		public void set_id(String _id) {
			this._id = _id;
		}

		public String get_id() {
			return this._id;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getUrl() {
			return this.url;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		public String getIcon() {
			return this.icon;
		}

		public void setPic(String pic) {
			this.pic = pic;
		}

		public String getPic() {
			return this.pic;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getTitle() {
			return this.title;
		}

	}

}

package com.izhubo.web.vo;

import com.izhubo.web.vo.UserinfoMySignListH5VO.UserinfoMySignListH5VOData;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class UserinfoMySignListH5VO extends BaseResultVO<UserinfoMySignListH5VOData> {

	class UserinfoMySignListH5VOData {
		@ApiModelProperty(value = "报名表id")
		private String nc_sign_id;
		private String commodity_id;

		@ApiModelProperty(value = "线上课数量")
		private int crouse_online_num;

		@ApiModelProperty(value = "进度>80.80")
		private int progress;

		@ApiModelProperty(value = "课程总数>50")
		private int crouse_num;

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

		public void setCrouse_online_num(int crouse_online_num) {
			this.crouse_online_num = crouse_online_num;
		}

		public int getCrouse_online_num() {
			return this.crouse_online_num;
		}

		public void setProgress(int progress) {
			this.progress = progress;
		}

		
		public String getNc_sign_id() {
			return nc_sign_id;
		}

		public void setNc_sign_id(String nc_sign_id) {
			this.nc_sign_id = nc_sign_id;
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

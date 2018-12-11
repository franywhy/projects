package com.izhubo.web.vo;

import com.izhubo.web.vo.UserinfoTipListVO.UserinfoTipListVOData;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class UserinfoTipListVO extends BaseResultVO<UserinfoTipListVOData> {

	class UserinfoTipListVOData {

		@ApiModelProperty(value = "id")
		private int _id;

		@ApiModelProperty(value = "标签名称")
		private String tip_name;

		@ApiModelProperty(value = "选中状态-用户是否关注过该标签")
		private boolean is_select;

		public void set_id(int _id) {
			this._id = _id;
		}

		public int get_id() {
			return this._id;
		}

		public void setTip_name(String tip_name) {
			this.tip_name = tip_name;
		}

		public String getTip_name() {
			return this.tip_name;
		}

		public void setIs_select(boolean is_select) {
			this.is_select = is_select;
		}

		public boolean getIs_select() {
			return this.is_select;
		}

	}

}

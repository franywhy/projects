package com.izhubo.web.vo;

import com.izhubo.web.vo.UserInfoTopicInputTipsV200.UserInfoTopicInputTipsV200Data;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class UserInfoTopicInputTipsV200 extends BaseResultVO<UserInfoTopicInputTipsV200Data> {
	class UserInfoTopicInputTipsV200Data {
		@ApiModelProperty(value = "id")
		private int _id;
		@ApiModelProperty(value = "标签名称")
		private String tip_name;

		public int get_id() {
			return _id;
		}

		public void set_id(int _id) {
			this._id = _id;
		}

		public String getTip_name() {
			return tip_name;
		}

		public void setTip_name(String tip_name) {
			this.tip_name = tip_name;
		}

	}
}

package com.izhubo.web.vo;

import com.izhubo.web.vo.TeacherListByScoreV200.TeacherListByScoreV200Data;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class TeacherListByScoreV200 extends
		BaseResultVO<TeacherListByScoreV200Data> {

	class TeacherListByScoreV200Data {
		@ApiModelProperty(value = "教师id")
		private int teach_id;

		@ApiModelProperty(value = "教师昵称")
		private String nick_name;

		@ApiModelProperty(value = "vip标志")
		private boolean vip_icon;

		@ApiModelProperty(value = "头像")
		private String pic;

		@ApiModelProperty(value = "满意度")
		private String satisfaction;

		public void setTeach_id(int teach_id) {
			this.teach_id = teach_id;
		}

		public int getTeach_id() {
			return this.teach_id;
		}

		public void setNick_name(String nick_name) {
			this.nick_name = nick_name;
		}

		public String getNick_name() {
			return this.nick_name;
		}

		public void setVip_icon(boolean vip_icon) {
			this.vip_icon = vip_icon;
		}

		public boolean getVip_icon() {
			return this.vip_icon;
		}

		public void setPic(String pic) {
			this.pic = pic;
		}

		public String getPic() {
			return this.pic;
		}

		public void setSatisfaction(String satisfaction) {
			this.satisfaction = satisfaction;
		}

		public String getSatisfaction() {
			return this.satisfaction;
		}

	}

}

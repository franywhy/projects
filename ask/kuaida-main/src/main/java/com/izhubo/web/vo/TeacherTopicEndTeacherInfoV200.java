package com.izhubo.web.vo;

import java.util.List;

import com.izhubo.web.vo.TeacherTopicEndTeacherInfoV200.TeacherTopicEndTeacherInfoV200Data;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class TeacherTopicEndTeacherInfoV200 extends BaseResultVO<TeacherTopicEndTeacherInfoV200Data> {
	class TeacherTopicEndTeacherInfoV200Data {
		@ApiModelProperty(value = "不满意集合")
		private List<Evaluation> evaluation;
		@ApiModelProperty(value = "教师信息")
		private Teacher teacher;

		public void setEvaluation(List<Evaluation> evaluation) {
			this.evaluation = evaluation;
		}

		public List<Evaluation> getEvaluation() {
			return this.evaluation;
		}

		public void setTeacher(Teacher teacher) {
			this.teacher = teacher;
		}

		public Teacher getTeacher() {
			return this.teacher;
		}
	}

	class Evaluation {
		@ApiModelProperty(value = "不满意文字内容")
		private String name;

		@ApiModelProperty(value = "id")
		private int id;

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getId() {
			return this.id;
		}

	}

	class Teacher {
		@ApiModelProperty(value = "教师id")
		private int _id;
		@ApiModelProperty(value = "教师昵称")
		private String nick_name;

		@ApiModelProperty(value = "教师头像")
		private String pic;

		@ApiModelProperty(value = "被关注数量")
		private int target_num;

		@ApiModelProperty(value = "老师id")
		private int teach_id;

		@ApiModelProperty(value = "满意度")
		private String satisfaction;

		@ApiModelProperty(value = "抢答数量")
		private int qiangda_num;

		@ApiModelProperty(value = "是否已关注")
		private boolean isAttention;

		public void set_id(int _id) {
			this._id = _id;
		}

		public int get_id() {
			return this._id;
		}

		public void setNick_name(String nick_name) {
			this.nick_name = nick_name;
		}

		public String getNick_name() {
			return this.nick_name;
		}

		public void setPic(String pic) {
			this.pic = pic;
		}

		public String getPic() {
			return this.pic;
		}

		public void setTarget_num(int target_num) {
			this.target_num = target_num;
		}

		public int getTarget_num() {
			return this.target_num;
		}

		public void setTeach_id(int teach_id) {
			this.teach_id = teach_id;
		}

		public int getTeach_id() {
			return this.teach_id;
		}

		public void setSatisfaction(String satisfaction) {
			this.satisfaction = satisfaction;
		}

		public String getSatisfaction() {
			return this.satisfaction;
		}

		public void setQiangda_num(int qiangda_num) {
			this.qiangda_num = qiangda_num;
		}

		public int getQiangda_num() {
			return this.qiangda_num;
		}

		public void setIsAttention(boolean isAttention) {
			this.isAttention = isAttention;
		}

		public boolean getIsAttention() {
			return this.isAttention;
		}

	}
}

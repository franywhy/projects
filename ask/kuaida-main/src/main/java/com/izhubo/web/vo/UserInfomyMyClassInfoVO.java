package com.izhubo.web.vo;

import com.izhubo.web.vo.UserInfomyMyClassInfoVO.UserInfomyMyClassInfoVOData;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 我的班级-班级详情
 * @author Administrator
 *
 */
public class UserInfomyMyClassInfoVO extends BaseResultVO<UserInfomyMyClassInfoVOData> {

	class UserInfomyMyClassInfoVOData{
		
		@ApiModelProperty(value = "排课计划id")
		private String _id;
		
		@ApiModelProperty(value = "课次")
		private String class_time;
		
		@ApiModelProperty(value = "考勤老师昵称")
		private String nc_out_teacher_name;
		
		@ApiModelProperty(value = "上课时间")
		private String open_time;
		
		@ApiModelProperty(value = "教室")
		private String classroom;
		
		@ApiModelProperty(value = "班级名称")
		private String name;
		
		@ApiModelProperty(value = "校区名称")
		private String school_name;
		
		@ApiModelProperty(value = "课程名称")
		private String course_name;
		
		@ApiModelProperty(value = "商品图片地址")
		private String pic;
		
		@ApiModelProperty(value = "状态")
		private String status;
		
		@ApiModelProperty(value = "学员昵称列表")
		private String[] studetList;

		
		
		public String getNc_out_teacher_name() {
			return nc_out_teacher_name;
		}

		public void setNc_out_teacher_name(String nc_out_teacher_name) {
			this.nc_out_teacher_name = nc_out_teacher_name;
		}

		public String getSchool_name() {
			return school_name;
		}

		public void setSchool_name(String school_name) {
			this.school_name = school_name;
		}

		public String getCourse_name() {
			return course_name;
		}

		public void setCourse_name(String course_name) {
			this.course_name = course_name;
		}

		public String getPic() {
			return pic;
		}

		public void setPic(String pic) {
			this.pic = pic;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String[] getStudetList() {
			return studetList;
		}

		public void setStudetList(String[] studetList) {
			this.studetList = studetList;
		}

		public String get_id() {
			return _id;
		}

		public void set_id(String _id) {
			this._id = _id;
		}

		public String getClass_time() {
			return class_time;
		}

		public void setClass_time(String class_time) {
			this.class_time = class_time;
		}

		public String getOpen_time() {
			return open_time;
		}

		public void setOpen_time(String open_time) {
			this.open_time = open_time;
		}

		public String getClassroom() {
			return classroom;
		}

		public void setClassroom(String classroom) {
			this.classroom = classroom;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		
		
	}

}

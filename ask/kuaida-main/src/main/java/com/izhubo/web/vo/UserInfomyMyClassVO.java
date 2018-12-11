package com.izhubo.web.vo;

import com.izhubo.web.vo.UserInfomyMyClassVO.UserInfomyMyClassVOData;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 我的班级-班级列表
 * @author Administrator
 *
 */
public class UserInfomyMyClassVO extends BaseResultVO<UserInfomyMyClassVOData> {

	class UserInfomyMyClassVOData{
		
		@ApiModelProperty(value = "排课计划id")
		private String _id;
		
		@ApiModelProperty(value = "课次")
		private String class_time;
		
		@ApiModelProperty(value = "授课老师")
		private String nc_out_teacher_name;
		
		@ApiModelProperty(value = "上课时间")
		private String open_time;
		
		@ApiModelProperty(value = "教室")
		private String classroom;
		
		@ApiModelProperty(value = "班级名称")
		private String name;

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

		public String getNc_out_teacher_name() {
			return nc_out_teacher_name;
		}

		public void setNc_teacher_name(String nc_out_teacher_name) {
			this.nc_out_teacher_name = nc_out_teacher_name;
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

package io.renren.mongo.sync.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "classplan_live")
public class ClassplanLiveEntity {

	@Override
	public String toString() {
		return "ClassplanLiveEntity [_id=" + _id + ", name=" + name + ", classplan_id=" + classplan_id + ", live_room_id=" + live_room_id
				+ ", period_id=" + period_id + ", live_id=" + live_id + ", live_start_time=" + live_start_time + ", live_end_time=" + live_end_time
				+ ", live_teacher_id=" + live_teacher_id + ", live_res_url=" + live_res_url + ", course_id=" + course_id + ", course_files="
				+ course_files + ", dr=" + dr + "]";
	}
	// id
	private String _id;
	// 名称
	private String name;
	// 备注
	private String classplan_id;
	// 时间说明
	private String live_room_id;
	// 课程子表直播课PK
	private String period_id;
	// 直播间id
	private String live_id;
	// 开始时间
	private String live_start_time;
	// 结束时间
	private String live_end_time;
	// 教师id
	private String live_teacher_id;
	private String live_res_url;
	// 课程id
	private String course_id;
	// 文件上传
	private String course_files;
	// dr
	private int dr;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassplan_id() {
		return classplan_id;
	}
	public void setClassplan_id(String classplan_id) {
		this.classplan_id = classplan_id;
	}
	public String getLive_room_id() {
		return live_room_id;
	}
	public void setLive_room_id(String live_room_id) {
		this.live_room_id = live_room_id;
	}
	public String getPeriod_id() {
		return period_id;
	}
	public void setPeriod_id(String period_id) {
		this.period_id = period_id;
	}
	public String getLive_id() {
		return live_id;
	}
	public void setLive_id(String live_id) {
		this.live_id = live_id;
	}
	public String getLive_start_time() {
		return live_start_time;
	}
	public void setLive_start_time(String live_start_time) {
		this.live_start_time = live_start_time;
	}
	public String getLive_end_time() {
		return live_end_time;
	}
	public void setLive_end_time(String live_end_time) {
		this.live_end_time = live_end_time;
	}
	public String getLive_teacher_id() {
		return live_teacher_id;
	}
	public void setLive_teacher_id(String live_teacher_id) {
		this.live_teacher_id = live_teacher_id;
	}
	public String getLive_res_url() {
		return live_res_url;
	}
	public void setLive_res_url(String live_res_url) {
		this.live_res_url = live_res_url;
	}
	public String getCourse_id() {
		return course_id;
	}
	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}
	public String getCourse_files() {
		return course_files;
	}
	public void setCourse_files(String course_files) {
		this.course_files = course_files;
	}
	public int getDr() {
		return dr;
	}
	public void setDr(int dr) {
		this.dr = dr;
	}

}

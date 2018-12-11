package io.renren.mongo.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 考勤
 * @author 俊斌
 *
 */
@Document(collection="attendances")
public class AttendanceEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * nc_id
	 */
	private String nc_id;
	/**
	 * 子表id
	 */
	private String ncattendancestudentid;
	/**
	 * 考勤时间long
	 */
	private long attendance_time_long;
	/**
	 * 考勤时间
	 */
	private String attendance_time;
	/**
	 *  考勤时点
	 */
	private String attendance_sessions;
	/**
	 * 考勤人数
	 */
	private Integer attendance_number;
	/**
	 * 课时选择
	 */
	private String class_select;
	/**
	 * 课程内容
	 */
	private String content;
	/**
	 * 排课计划ncid
	 */
	private String nc_class_plan;
	/**
	 * 排课计划子表ncid
	 */
	private String nc_plan_course;
	/**
	 * 考勤单号
	 */
	private String nc_code;
	/**
	 * 课程科目ncid
	 */
	private String nc_course_id;
	/**
	 * 课程科目
	 */
	private String nc_course_name;
	/**
	 * 校区编号
	 */
	private String nc_school_code;
	/**
	 * 校区主键
	 */
	private String nc_school_pk;
	/**
	 * 校区昵称
	 */
	private String nc_school_name;
	/**
	 * 教师ncid
	 */
	private String nc_teacher_id;
	/**
	 * 教师昵称
	 */
	private String nc_teacher_name;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 教师
	 */
	private String room;
	/**
	 * 课次
	 */
	private Double sessions;
	/**
	 * 是否删除标示
	 */
	private Integer dr;
	
	public String getNcattendancestudentid() {
		return ncattendancestudentid;
	}
	public void setNcattendancestudentid(String ncattendancestudentid) {
		this.ncattendancestudentid = ncattendancestudentid;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public Double getSessions() {
		return sessions;
	}
	public void setSessions(Double sessions) {
		this.sessions = sessions;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public String getNc_id() {
		return nc_id;
	}
	public void setNc_id(String nc_id) {
		this.nc_id = nc_id;
	}
	
	public String getAttendance_time() {
		return attendance_time;
	}
	public void setAttendance_time(String attendance_time) {
		this.attendance_time = attendance_time;
	}
	public String getAttendance_sessions() {
		return attendance_sessions;
	}
	public void setAttendance_sessions(String attendance_sessions) {
		this.attendance_sessions = attendance_sessions;
	}
	public Integer getAttendance_number() {
		return attendance_number;
	}
	public void setAttendance_number(Integer attendance_number) {
		this.attendance_number = attendance_number;
	}
	public String getClass_select() {
		return class_select;
	}
	public void setClass_select(String class_select) {
		this.class_select = class_select;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getNc_class_plan() {
		return nc_class_plan;
	}
	public void setNc_class_plan(String nc_class_plan) {
		this.nc_class_plan = nc_class_plan;
	}
	public String getNc_plan_course() {
		return nc_plan_course;
	}
	public void setNc_plan_course(String nc_plan_course) {
		this.nc_plan_course = nc_plan_course;
	}
	public String getNc_code() {
		return nc_code;
	}
	public void setNc_code(String nc_code) {
		this.nc_code = nc_code;
	}
	public String getNc_course_id() {
		return nc_course_id;
	}
	public void setNc_course_id(String nc_course_id) {
		this.nc_course_id = nc_course_id;
	}
	public String getNc_course_name() {
		return nc_course_name;
	}
	public void setNc_course_name(String nc_course_name) {
		this.nc_course_name = nc_course_name;
	}
	public String getNc_school_code() {
		return nc_school_code;
	}
	public void setNc_school_code(String nc_school_code) {
		this.nc_school_code = nc_school_code;
	}
	public String getNc_school_pk() {
		return nc_school_pk;
	}
	public void setNc_school_pk(String nc_school_pk) {
		this.nc_school_pk = nc_school_pk;
	}
	public String getNc_school_name() {
		return nc_school_name;
	}
	public void setNc_school_name(String nc_school_name) {
		this.nc_school_name = nc_school_name;
	}
	public String getNc_teacher_id() {
		return nc_teacher_id;
	}
	public void setNc_teacher_id(String nc_teacher_id) {
		this.nc_teacher_id = nc_teacher_id;
	}
	public String getNc_teacher_name() {
		return nc_teacher_name;
	}
	public void setNc_teacher_name(String nc_teacher_name) {
		this.nc_teacher_name = nc_teacher_name;
	}
	public long getAttendance_time_long() {
		return attendance_time_long;
	}
	public void setAttendance_time_long(long attendance_time_long) {
		this.attendance_time_long = attendance_time_long;
	}
	
	private String start_chapter;
	private String end_chapter;
	private String start_section;
	private String end_section;

	public String getStart_chapter() {
		return start_chapter;
	}
	public void setStart_chapter(String start_chapter) {
		this.start_chapter = start_chapter;
	}
	public String getEnd_chapter() {
		return end_chapter;
	}
	public void setEnd_chapter(String end_chapter) {
		this.end_chapter = end_chapter;
	}
	public String getStart_section() {
		return start_section;
	}
	public void setStart_section(String start_section) {
		this.start_section = start_section;
	}
	public String getEnd_section() {
		return end_section;
	}
	public void setEnd_section(String end_section) {
		this.end_section = end_section;
	}
	
}

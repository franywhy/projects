package com.mysqldb.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 学分标准类
 */
@Entity
@Table(name = "credit_standard")
@org.hibernate.annotations.Table(appliesTo = "credit_standard", comment = "credit_standard")
public class CreditStandard implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Integer id;
	
	/**科目主键 */
	private String nc_id;
	/** 科目 */
	private String subject_name;
	/**
	 * 科目编码
	 */
	private String course_code;
	/** 学分标准合计 */
	private Integer total_credits;
	/** 出勤分数 */
	private Integer attendance_score;
	/** 作业分数 */
	private Integer activity_fraction;
	/** 结业考核分数 */
	private Integer graduation_examination_score;
	/** 备注 */
	private String remarks;
	
	/** 科目类型 */
	private String subject_type;
	/** 考试类型  
	 * cy_kj
	 * cy_cjfg
	 * cy_dsh
	 * 
	 * zj_cg
	 * zj_cw
	 * 
	 * 
	 * */
	private String exam_type;
	
	
	
	@Id
	@GeneratedValue
	@Column(name = "ID",columnDefinition = "INT")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取科目主键 
	 * @return
	 */
	@Column(name = "nc_id",columnDefinition = "VARCHAR")
	public String getNc_id() {
		return nc_id;
	}
	public void setNc_id(String nc_id) {
		this.nc_id = nc_id;
	}
	/**
	 * 获取科目名称
	 * @return
	 */
	@Column(name = "subject_name",columnDefinition = "VARCHAR")
	public String getSubject_name() {
		return subject_name;
	}
	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}
	/**
	 * 获取学分标准合计
	 * @return
	 */
	@Column(name = "total_credits",columnDefinition = "int")
	public Integer getTotal_credits() {
		return total_credits;
	}
	public void setTotal_credits(Integer total_credits) {
		this.total_credits = total_credits;
	}
	/**
	 * 获取出勤分数
	 * @return
	 */
	@Column(name = "attendance_score",columnDefinition = "int")
	public Integer getAttendance_score() {
		return attendance_score;
	}
	public void setAttendance_score(Integer attendance_score) {
		this.attendance_score = attendance_score;
	}
	/**
	 * 获取作业分数
	 * @return
	 */
	@Column(name = "activity_fraction",columnDefinition = "int")
	public Integer getActivity_fraction() {
		return activity_fraction;
	}
	public void setActivity_fraction(Integer activity_fraction) {
		this.activity_fraction = activity_fraction;
	}
	/**
	 * 获取结业考核分数
	 * @return
	 */
	@Column(name = "graduation_examination_score",columnDefinition = "int")
	public Integer getGraduation_examination_score() {
		return graduation_examination_score;
	}
	public void setGraduation_examination_score(Integer graduation_examination_score) {
		this.graduation_examination_score = graduation_examination_score;
	}
	@Column(name = "remarks",columnDefinition = "VARCHAR")
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * 获取科目编码
	 * @return
	 */
	@Column(name = "course_code",columnDefinition = "VARCHAR")
	public String getCourse_code() {
		return course_code;
	}
	/**
	 * 设置科目编码
	 */
	public void setCourse_code(String course_code) {
		this.course_code = course_code;
	}
	/**
	 * 获取科目类型
	 * @return
	 */
	@Column(name = "subject_type",columnDefinition = "VARCHAR")
	public String getSubject_type() {
		return subject_type;
	}
	/**
	 * 设置科目类型
	 */
	public void setSubject_type(String subject_type) {
		this.subject_type = subject_type;
	}
	
	/**
	 * 获取考试类型
	 * @return
	 */
	@Column(name = "exam_type",columnDefinition = "VARCHAR")
	public String getExam_type() {
		return exam_type;
	}
	/**
	 * 设置考试类型
	 */
	public void setExam_type(String exam_type) {
		this.exam_type = exam_type;
	}
	
	
	
}

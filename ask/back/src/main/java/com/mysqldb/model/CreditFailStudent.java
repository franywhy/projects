package com.mysqldb.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 不及格学员信息的实体类
 * @author 严志城
 *@time 2017年3月21日21:49:08
 */
@Entity
@Table(name = "credit_fail_student")
@org.hibernate.annotations.Table(appliesTo = "credit_fail_student", comment = "credit_fail_student")
public class CreditFailStudent {
	/**
	 * id
	 */
	private Integer id;
	
	
	/**
	 * 学员NCID
	 */
	private String ncUserId;
	/**
	 * 科目id
	 */
	private String ncSubjectId;
	/**
	 * 科目编码
	 */
	private String ncSubjectCode;
	

	/**
	 * 时间戳
	 */
	private Date ts;

	/**
	 * 考试类型
	 */
	private Integer examType = 0;
	

	/**
	 * 获取 id 的属性值
     *
	 * @return id :  id 
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID",columnDefinition = "INT")
	public Integer getId(){
		return this.id;
	}

	/**
	 * 设置 id 的属性值
	 *		
	 * @param id :  id 
	 */
	public void setId(Integer id){
		this.id	= id;
	}
	
	
	/**
	 * 获取  学员主键
	 */
	@Column(name = "nc_user_id",columnDefinition = "VARCHAR")
	public String getNcUserId() {
		return ncUserId;
	}

	/**
	 * 设置 学员主键
	 */
	public void setNcUserId(String ncUserId) {
		this.ncUserId = ncUserId;
	}

	/**
	 * 获取  科目主键
	 */
	@Column(name = "nc_subject_id",columnDefinition = "VARCHAR")
	public String getNcSubjectId() {
		return ncSubjectId;
	}

	/**
	 * 设置 科目主键
	 */
	public void setNcSubjectId(String ncSubjectId) {
		this.ncSubjectId = ncSubjectId;
	}

	/**
	 * 获取  科目编码
	 */
	@Column(name = "nc_subject_code",columnDefinition = "VARCHAR")
	public String getNcSubjectCode() {
		return ncSubjectCode;
	}

	/**
	 * 设置 科目编码
	 */
	public void setNcSubjectCode(String ncSubjectCode) {
		this.ncSubjectCode = ncSubjectCode;
	}
	
	/**
	 * 获取 时间戳
	 */
	@Column(name = "TS",insertable = false,updatable=false,columnDefinition = "DATETIME")
	public Date getTs(){
		return this.ts;
	}

	/**
	 * 设置 时间戳
	 */
	public void setTs(Date ts){
		this.ts	= ts;
	}
	
	@Column(name = "exam_type",columnDefinition = "INT")
	public Integer getExamType() {
		return examType;
	}

	public void setExamType(Integer examType) {
		this.examType = examType;
	}



	
	
	
}

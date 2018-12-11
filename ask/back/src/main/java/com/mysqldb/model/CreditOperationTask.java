package com.mysqldb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 学分运算类
 * @author yanzhicheng 
 * @Time 2017年2月23日22:32:16
 */
@Entity
@Table(name = "credit_operation_task")
@org.hibernate.annotations.Table(appliesTo = "credit_operation_task", comment = "credit_operation_task")
public class CreditOperationTask implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	/**
	 * id 
	 */
	public static final String ID="id";
	
	/**
	 * url
	 */
	public static final String URL="url";
	
	/**
	 * 名称 
	 */
	public static final String NAME="name";
	
	
	
	/**
	 * id
	 */
	private Integer id;
	
	/**
	 * url
	 */
	private String url;
	
	/**
	 * 名称 
	 */
	private String name;
	
	
	/**
	 * 几号
	 */
	private Integer executeDate;
	
	
	/**
	 * 出勤开始日期
	 */
	private Integer attendanceBeginDate;
	
	/**
	 * 出勤结束日期
	 */
	private Integer attendanceEndDate;
	
	
	
	/**
	 * 作业开始日期
	 */
	private Integer workBeginDate;
	
	/**
	 * 作业结束日期
	 */
	private Integer workEndDate;
	
	
	
	/**
	 * 考试开始日期
	 */
	private Integer examBeginDate;
	
	/**
	 * 考试结束日期
	 */
	private Integer examEndDate;
	
	
	/**
	 * 最后查询日期
	 */
	private String lastQueryDate;
	
	
	@Column(name = "last_query_date",columnDefinition = "VARCHAR")
	public String getLastQueryDate() {
		return lastQueryDate;
	}

	public void setLastQueryDate(String lastQueryDate) {
		this.lastQueryDate = lastQueryDate;
	}

	@Column(name = "attendance_begin_date",columnDefinition = "INT")
	public Integer getAttendanceBeginDate() {
		return attendanceBeginDate;
	}

	public void setAttendanceBeginDate(Integer attendanceBeginDate) {
		this.attendanceBeginDate = attendanceBeginDate;
	}
	@Column(name = "attendance_end_date",columnDefinition = "INT")
	public Integer getAttendanceEndDate() {
		return attendanceEndDate;
	}

	public void setAttendanceEndDate(Integer attendanceEndDate) {
		this.attendanceEndDate = attendanceEndDate;
	}
	@Column(name = "work_begin_date",columnDefinition = "INT")
	public Integer getWorkBeginDate() {
		return workBeginDate;
	}

	public void setWorkBeginDate(Integer workBeginDate) {
		this.workBeginDate = workBeginDate;
	}
	@Column(name = "work_end_date",columnDefinition = "INT")
	public Integer getWorkEndDate() {
		return workEndDate;
	}

	public void setWorkEndDate(Integer workEndDate) {
		this.workEndDate = workEndDate;
	}
	@Column(name = "exam_begin_date",columnDefinition = "INT")
	public Integer getExamBeginDate() {
		return examBeginDate;
	}

	public void setExamBeginDate(Integer examBeginDate) {
		this.examBeginDate = examBeginDate;
	}
	@Column(name = "exam_end_date",columnDefinition = "INT")
	public Integer getExamEndDate() {
		return examEndDate;
	}

	public void setExamEndDate(Integer examEndDate) {
		this.examEndDate = examEndDate;
	}

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
	 * 获取 url
     *
	 * @return url : url
	 */
	@Column(name = "URL",columnDefinition = "VARCHAR")
	public String getUrl(){
		return this.url;
	}

	/**
	 * 设置 url
	 *		
	 * @param  url :  url
	 */
	public void setUrl(String url){
		this.url	= url;
	}
	
	
	
	/**
	 * 获取 名称
     *
	 * @return name :  名称 
	 */
	@Column(name = "NAME",columnDefinition = "VARCHAR")
	public String getName(){
		return this.name;
	}

	/**
	 * 设置 名称 
	 *		
	 * @param  name :  名称 
	 */
	public void setName(String name){
		this.name	= name;
	}
	
	/**
	 * 获取date
	 */
	@Column(name = "execute_date",columnDefinition = "int")
	public Integer getExecuteDate(){
		return this.executeDate;
	}

	/**
	 * 设置date
	 */
	public void setExecuteDate(Integer executeDate){
		this.executeDate	= executeDate;
	}
	

}

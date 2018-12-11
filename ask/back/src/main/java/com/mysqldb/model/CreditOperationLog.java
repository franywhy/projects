package com.mysqldb.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 学分运算日志类
 * @author yanzhicheng 
 * @Time 2017年2月23日22:32:16
 */
@Entity
@Table(name = "credit_operation_log")
@org.hibernate.annotations.Table(appliesTo = "credit_operation_log", comment = "credit_operation_log")
public class CreditOperationLog implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	/**
	 * id 
	 */
	public static final String ID="id";
	
	/**
	 * 运行结果
	 */
	public static final String LOGSTR="logstr";
	
	/**
	 * 名称 
	 */
	public static final String TASKNAME="taskname";
	/**
	 * 时间段-起始时间 
	 */
	public static final String STARTTIME="startTime";
	
	/**
	 * 时间段-结束时间 
	 */
	public static final String ENDTIME="endTime";
	
	/**
	 * 时间戳 
	 */
	public static final String TS="ts";
	
	
	/**
	 * id
	 */
	private Integer id;
	
	/**
	 * 运行结果
	 */
	private String logstr;
	
	/**
	 * 名称 
	 */
	private String taskname;
	
	
	/**
	 * 时间段-起始时间
	 */
	private Date startTime;
	
	/**
	 * 时间段-结束时间
	 */
	private Date endTime;
	
	
	/**
	 * 时间戳
	 */
	private Date ts;
	/**
	 * 每个log的ID主键 
	 */
	private String   LOGID="logid";
	
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
	 * 获取 运行结果
     *
	 * @return logstr :  运行结果 
	 */
	@Column(name = "LOGSTR",columnDefinition = "VARCHAR")
	public String getLogstr(){
		return this.logstr;
	}

	/**
	 * 设置 运行结果
	 *		
	 * @param  logstr :  运行结果
	 */
	public void setLogstr(String logstr){
		this.logstr	= logstr;
	}
	
	
	/**
	 * 每个补丁的唯一主键
     *
	 * @return LOGID :   
	 */
	@Column(name = "logid",columnDefinition = "VARCHAR")
	public String getLogid(){
		return this.LOGID;
	}

	/**
	 * 每个补丁的唯一主键
	 *		
	 * @param  LOGID :   
	 */
	public void setLogid(String logid){
		this.LOGID	= logid;
	}
	
	
	
	
	
	/**
	 * 获取 名称
     *
	 * @return taskname :  名称 
	 */
	@Column(name = "TASK_NAME",columnDefinition = "VARCHAR")
	public String getTaskname(){
		return this.taskname;
	}

	/**
	 * 设置 名称 
	 *		
	 * @param  taskname :  名称 
	 */
	public void setTaskname(String taskname){
		this.taskname	= taskname;
	}
	
	
	/**
	 * 获取 时间段-起始时间 的属性值
     *
	 * @return startTime :  时间段-起始时间 
	 */
	@Column(name = "START_TIME",columnDefinition = "DATETIME")
	public Date getStartTime(){
		return this.startTime;
	}

	/**
	 * 设置 时间段-起始时间 的属性值
	 *		
	 * @param startTime :  时间段-起始时间 
	 */
	public void setStartTime(Date startTime){
		this.startTime	= startTime;
	}
	/**
	 * 获取 时间段-结束时间 的属性值
     *
	 * @return endTime :  时间段-结束时间 
	 */
	@Column(name = "END_TIME",columnDefinition = "DATETIME")
	public Date getEndTime(){
		return this.endTime;
	}

	/**
	 * 设置 时间段-结束时间 的属性值
	 *		
	 * @param endTime :  时间段-结束时间 
	 */
	public void setEndTime(Date endTime){
		this.endTime	= endTime;
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
	

}

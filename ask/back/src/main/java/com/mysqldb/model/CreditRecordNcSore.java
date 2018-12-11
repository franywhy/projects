package com.mysqldb.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.izhubo.credit.vo.NCscoreSyncVO;

/**
 * NC每天变更的成绩单
 * @author lintf 
 * @Time 2017年9月24日13:46:18
 */
@Entity
@Table(name = "credit_record_ncscore")
@org.hibernate.annotations.Table(appliesTo = "credit_record_ncscore", comment = "credit_record_ncscore")
public class CreditRecordNcSore {
	/**
	 * id
	 */
	private Integer id;
	 
	/**
	 * 时间戳
	 */
	private Date ts;
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
	 * 表否删除
	 */
	private int dR;
	/**
	 * 同步时间
	 */
	private Date syTs;
	/**
	 * 单据的表头主键
	 */
	private String hId;
	/**
	 * 单据的表体主键 
	 */
	private String bId;
	/**
	 * 单据的分数 通过 是100 没有通过是0分 现在只有这两个状态  
	 */
	private int passScore;
	/**
	 * 学员主键
	 */
	private String studentId;
	/**
	 * 对应的科目 
	 */
	private String subjectId;
	/**
	 * 考试的类型
	 */
	private String examType;
	/**
	 * 科目的类型
	 */
	private String subjectType;
	
	@Column(name = "DR",columnDefinition = "INT")
	public int getdR() {
		return dR;
	}

	public void setdR(int dR) {
		this.dR = dR;
	}
	@Column(name = "syTs",columnDefinition = "DATETIME")
	public Date getSyTs() {
		return syTs;
	}

	public void setSyTs(Date syTs) {
		this.syTs = syTs;
	}
	@Column(name = "hId",columnDefinition = "VARCHAR")
	public String gethId() {
		return hId;
	}

	public void sethId(String hId) {
		this.hId = hId;
	}
	@Column(name = "bId",columnDefinition = "VARCHAR")
	public String getbId() {
		return bId;
	}

	public void setbId(String bId) {
		this.bId = bId;
	}
	@Column(name = "passScore",columnDefinition = "INT")
	public int getPassScore() {
		return passScore;
	}

	public void setPassScore(int passScore) {
		this.passScore = passScore;
	}
	@Column(name = "studentId",columnDefinition = "VARCHAR")
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	@Column(name = "subjectId",columnDefinition = "VARCHAR")
	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	@Column(name = "examType",columnDefinition = "VARCHAR")
	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}
	@Column(name = "subjectType",columnDefinition = "VARCHAR")
	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	
	 /**
	  * 从每天同步的NC单据源构建
	  * @param sourcevo
	  */
	public CreditRecordNcSore(NCscoreSyncVO sourcevo){
		this.bId=sourcevo.getbId();
		this.hId=sourcevo.gethId();
		this.dR=sourcevo.getdR();
		this.subjectId =sourcevo.getSubjectId(); 
		this.subjectType =sourcevo.getSubjectType();
		this.studentId=sourcevo.getStudentId();
		this.passScore=sourcevo.getPassScore();
		this.examType=sourcevo.getExamType();
		this.syTs=sourcevo.getSyTs();
		 
		
	}
	public CreditRecordNcSore(){
		
		
	}
 
 

	
	
	
	

	
	 
	
	
}

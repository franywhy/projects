package com.mysqldb.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.google.gson.annotations.SerializedName;
import com.izhubo.credit.vo.TiKuScoreVO;

/**
 * 题库中的作业和考试的成绩单
 * 
 * @author lintf
 * @see  修改本类时记得要修改SYNC项目中的com.mysqldb.model.CreditRecordTiKuScore 
 **/
@Entity
@Table(name = "credit_record_tikuscore")
@org.hibernate.annotations.Table(appliesTo = "credit_record_tikuscore", comment = "credit_record_tikuscore")
public class CreditRecordTiKuScore {
 
	 
	 
	private Integer id; 
 
	private String classNCCode;  
	 
	private String courseCode;  
 
	private String ArrSubject; 
	 
	private String studentNCCode;// 商机编号
	 
	 
	private String studentName;
	 
	 
	private Double standarRate;
	/**
	 * 初次登记的成绩
	 */
	private Double firstRate;
	 
	private Double ThisRate;// 本次取得的成绩
	 
	 
	private String BillType;// 这个是单据类型 W为作业E为考试
	 
	 
	private String BillKey;// 这个是商机主键_科目_单据类型
	

	private List<String> BillKeyList;// 这个是商机主键_科目_单据类型 列表
	
 
	// 最后更新时间
	private Date LastTime;
	 
	// 作业和考试时间
	private Date ExanTime; 
	// 科目类型
	private String SubjectType;
	//这个是从题库接口中取得的创建时间
	private String CreateTime;
	private Integer dr;

	@Column(name = "SubjectType",columnDefinition = "VARCHAR")
	public String getSubjectType() {
		return SubjectType;
	}
	@Column(name = "CreateTime",columnDefinition = "VARCHAR")
	public String getCreateTime() {
		return CreateTime;
	}
 

	@Column(name = "classNCCode",columnDefinition = "VARCHAR")
	public String getClassNCCode() {
		return classNCCode;
	}

	@Column(name = "courseCode",columnDefinition = "VARCHAR")
	public String getCourseCode() {
		return courseCode;
	}

	@Column(name = "ArrSubject",columnDefinition = "VARCHAR")
	public String getArrSubject() {
		return ArrSubject;
	}

	@Column(name = "studentNCCode",columnDefinition = "VARCHAR")
	public String getStudentNCCode() {
		return studentNCCode;
	}

	@Column(name = "studentName",columnDefinition = "VARCHAR")
	public String getStudentName() {
		return studentName;
	}

	@Column(name = "standarRate",columnDefinition = "DOUBLE")
	public Double getStandarRate() {
		return standarRate;
	}
	/**
	 * 第一次生成时的分数
	 * @return
	 */
	@Column(name = "firstRate",columnDefinition = "DOUBLE")
	public Double getFirstRate() {
		return firstRate;
	}
	
	
	@Column(name = "BillType",columnDefinition = "VARCHAR")
	public String getBillType() {
		return BillType;
	}

	@Column(name = "BillKey",columnDefinition = "VARCHAR")
	public String getBillKey() {
		return BillKey;
	}

	@Column(name = "LastTime",columnDefinition = "DATETIME")
	public Date getLastTime() {
		return LastTime;
	}

	@Column(name = "ExanTime",columnDefinition = "DATETIME")
	public Date getExanTime() {
		return ExanTime;
	}

	public void setSubjectType(String SubjectType) {
		this.SubjectType = SubjectType;
	}
	public void setClassNCCode(String classNCCode) {
		this.classNCCode = classNCCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public void setArrSubject(String arrSubject) {
		ArrSubject = arrSubject;
	}

	public void setStudentNCCode(String studentNCCode) {
		this.studentNCCode = studentNCCode;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public void setStandarRate(Double standarRate) {
		this.standarRate = standarRate;
	}

	/**
	 * 第一次生成时的分数
	 * @return
	 */
	public void setFirstRate(Double firstRate) {
		this.firstRate = firstRate;
	}
	
	public void setBillType(String billType) {
		BillType = billType;
	}

	public void setBillKey(String billKey) {
		BillKey = billKey;
	}

	public void setLastTime(Date lastTime) {
		LastTime = lastTime;
	}

	public void setExanTime(Date exanTime) {
		ExanTime = exanTime;
	}
	@Id
	@GeneratedValue
 
	@Column(name = "id",columnDefinition = "INT")
	public Integer getId() {
		return id;
	}
	@Column(name = "dr",columnDefinition = "INT")
	public Integer getDr() {
		return dr;
	}

	@Column(name = "ThisRate",columnDefinition = "Double")
	public Double getThisRate() {
		return ThisRate;
	}

	//@Column(name = "BillKeyList",columnDefinition = "VARCHAR")
	@Transient 
	public List<String> getBillKeyList() {
		return BillKeyList;
	}

	public void setId(int id) {
		this.id = id;
	}
	public void setDr(int dr) {
		this.dr = dr;
	}
	public void setThisRate(Double thisRate) {
		ThisRate = thisRate;
	}
	public void setCreateTime(String CreateTime) {
		this.CreateTime = CreateTime;
	}

	
	public void setBillKeyList(List<String> billKeyList) {
		BillKeyList = billKeyList;
	}
public CreditRecordTiKuScore(){}
public CreditRecordTiKuScore(TiKuScoreVO tvo){
	 
	this.classNCCode=tvo.getClassNCCode();
	
	this.courseCode=tvo.getCourseCode();
	
	this.ArrSubject=tvo.getSubjectType();
	
	this.studentNCCode=tvo.getStudentNCCode();
	
	this.studentName=tvo.getStudentName();
	
	this.standarRate=tvo.getStandarRate();
	this.CreateTime=tvo.getCreateTime();
	
	this.ThisRate=tvo.getThisRate();
	
	this.BillType=tvo.getBillType();
	
	this.BillKey=tvo.getBillKey();
	
	this.BillKeyList=tvo.getBillKeyList();
	
	this.LastTime=tvo.getLastTime();
	
	this.ExanTime=tvo.getExanTime();
	this.SubjectType=tvo.getSubjectType();
	
    this.firstRate=tvo.getStandarRate(); //直接取得当
    this.dr=0;
	
}
 
}

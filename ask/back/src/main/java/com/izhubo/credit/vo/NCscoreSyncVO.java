package com.izhubo.credit.vo;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**
 * 每天得出NC中每天的成绩单信息VO
 * </p>包括 从业成绩单(cy)、初级成绩单(cj)、中级成绩单(zj)、学分单(xf) 括号中的为examtype
 * </p>  
 * @author lintf 
 *
 */
public class NCscoreSyncVO {
	/**
	 * 表否删除
	 */
	@SerializedName("dR") private int dR;
	/**
	 * 同步时间
	 */
	@SerializedName("syTs") private Date syTs;
	/**
	 * 单据的表头主键
	 */
	@SerializedName("hId") private String hId;
	/**
	 * 单据的表体主键 
	 */
	@SerializedName("bId") private String bId;
	/**
	 * 单据的分数 通过 是100 没有通过是0分 现在只有这两个状态  
	 */
	@SerializedName("passScore") private int passScore;
	/**
	 * 学员主键
	 */
	@SerializedName("studentId") private String studentId;
	/**
	 * 对应的科目 
	 */
	@SerializedName("subjectId") private String subjectId;
	/**
	 * 考试的类型
	 */
	@SerializedName("examType") private String examType;
	/**
	 * 科目类型
	 */
	@SerializedName("subjectType") private String subjectType;
	public int getdR() {
		return dR;
	}
	public void setdR(int dR) {
		this.dR = dR;
	}
	public Date getSyTs() {
		return syTs;
	}
	public void setSyTs(Date syTs) {
		this.syTs = syTs;
	}
	public String gethId() {
		return hId;
	}
	public void sethId(String hId) {
		this.hId = hId;
	}
	public String getbId() {
		return bId;
	}
	public void setbId(String bId) {
		this.bId = bId;
	}
	public int getPassScore() {
		return passScore;
	}
	public void setPassScore(int passScore) {
		this.passScore = passScore;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	/**
	 * 学分是相应科目的对应科目</p>
	 * 从业是三科目的 取会计基础(1001A71000000000C9IL)</p>
	 * 初级是两科目的取初级会计实务(1001A71000000000C9IU) </p>
	 * 中级是三科的 分别取
	 * 
	 * @return
	 */
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	/**
	 *  从业成绩单(cy)、初级成绩单(cj)、中级成绩单(zj)、学分单(xf)
	 * @return
	 */
	public String getExamType() {
		return examType;
	}
	/**
	 *  从业成绩单(cy)、初级成绩单(cj)、中级成绩单(zj)、学分单(xf)
	 * @param examType
	 */
	public void setExamType(String examType) {
		this.examType = examType;
	}
	public String getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	
	
	
}

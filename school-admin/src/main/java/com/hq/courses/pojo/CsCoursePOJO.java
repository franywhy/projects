package com.hq.courses.pojo;

import java.io.Serializable;
import java.util.Date;


/**
 * 课程
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-07 11:46:18
 */
public class CsCoursePOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//自增id
	private Long courseId;
	//课程名称
	private String courseName;
	//课程编码
	private String courseNo;
	//产品线pk1.自考;2.会计学历;3学来学网;5.会计猎才;6.会计培训
	private Long productId;
	private String productName;
	//部门ID
	private Long deptId;
	private String deptName;
	//更新时间戳
	private Date updateTime;
	//创建时间戳
	private Date createTime;
	//备注
	private String remark;
	//NCID
	private String ncId;
	//蓝鲸ID
	private String ljId;
	//题库ID
	private String tkId;
	//启用状态:0.作废;1.正常
	private Integer status;
	//章数量
	private Integer chapterNum;
	//节数量
	private Integer sectionNum;

	
	public Integer getChapterNum() {
		return chapterNum;
	}
	public void setChapterNum(Integer chapterNum) {
		this.chapterNum = chapterNum;
	}
	public Integer getSectionNum() {
		return sectionNum;
	}
	public void setSectionNum(Integer sectionNum) {
		this.sectionNum = sectionNum;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getNcId() {
		return ncId;
	}
	public void setNcId(String ncId) {
		this.ncId = ncId;
	}
	public String getLjId() {
		return ljId;
	}
	public void setLjId(String ljId) {
		this.ljId = ljId;
	}
	public String getTkId() {
		return tkId;
	}
	public void setTkId(String tkId) {
		this.tkId = tkId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 设置：自增id
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	/**
	 * 获取：自增id
	 */
	public Long getCourseId() {
		return courseId;
	}
	/**
	 * 设置：课程名称
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	/**
	 * 获取：课程名称
	 */
	public String getCourseName() {
		return courseName;
	}
	/**
	 * 设置：课程编码
	 */
	public void setCourseNo(String courseNo) {
		this.courseNo = courseNo;
	}
	/**
	 * 获取：课程编码
	 */
	public String getCourseNo() {
		return courseNo;
	}
	/**
	 * 设置：产品线pk1.自考;2.会计学历;3学来学网;5.会计猎才;6.会计培训
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	/**
	 * 获取：产品线pk1.自考;2.会计学历;3学来学网;5.会计猎才;6.会计培训
	 */
	public Long getProductId() {
		return productId;
	}
	/**
	 * 设置：更新时间戳
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间戳
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：创建时间戳
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间戳
	 */
	public Date getCreateTime() {
		return createTime;
	}
	@Override
	public String toString() {
		return "CsCoursePOJO [courseId=" + courseId + ", courseName=" + courseName + ", courseNo=" + courseNo
				+ ", productId=" + productId + ", productName=" + productName + ", deptId=" + deptId + ", deptName="
				+ deptName + ", updateTime=" + updateTime + ", createTime=" + createTime + ", remark=" + remark
				+ ", ncId=" + ncId + ", ljId=" + ljId + ", tkId=" + tkId + ", status=" + status + ", chapterNum="
				+ chapterNum + ", sectionNum=" + sectionNum + "]";
	}
}

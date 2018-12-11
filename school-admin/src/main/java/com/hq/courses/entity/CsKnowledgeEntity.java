package com.hq.courses.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 知识点档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:36
 */
public class CsKnowledgeEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long knowledgeId;
	//名称
	private String knowledgeName;
	//编码
	private String knowledgeNo;
	//考点:0.正常;1.考点
	private Integer keyPoint;
	//难度PK，请参考静态变量表
	private Integer levelId;
	//启用状态:0.作废;1.正常
	private Integer status;
	//节PK
	private Long sectionId;
	//章PK
	private Long chapterId;
	//课程PK
	private Long courseId;
	//更新时间戳
	private Date updateTime;
	//创建时间戳
	private Date createTime;
	//删除标记 0.正常; 1.删除
	private Integer dr;

	/**
	 * 设置：主键
	 */
	public void setKnowledgeId(Long knowledgeId) {
		this.knowledgeId = knowledgeId;
	}
	/**
	 * 获取：主键
	 */
	public Long getKnowledgeId() {
		return knowledgeId;
	}
	/**
	 * 设置：名称
	 */
	public void setKnowledgeName(String knowledgeName) {
		this.knowledgeName = knowledgeName;
	}
	/**
	 * 获取：名称
	 */
	public String getKnowledgeName() {
		return knowledgeName;
	}
	/**
	 * 设置：编码
	 */
	public void setKnowledgeNo(String knowledgeNo) {
		this.knowledgeNo = knowledgeNo;
	}
	/**
	 * 获取：编码
	 */
	public String getKnowledgeNo() {
		return knowledgeNo;
	}
	/**
	 * 设置：重点:1.正常;2.重点
	 */
	public void setKeyPoint(Integer keyPoint) {
		this.keyPoint = keyPoint;
	}
	/**
	 * 获取：重点:1.正常;2.重点
	 */
	public Integer getKeyPoint() {
		return keyPoint;
	}
	/**
	 * 设置：难度PK，请参考静态变量表
	 */
	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}
	/**
	 * 获取：难度PK，请参考静态变量表
	 */
	public Integer getLevelId() {
		return levelId;
	}
	/**
	 * 设置：启用状态:0.作废;1.正常
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：启用状态:0.作废;1.正常
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：节PK
	 */
	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}
	/**
	 * 获取：节PK
	 */
	public Long getSectionId() {
		return sectionId;
	}
	/**
	 * 设置：章PK
	 */
	public void setChapterId(Long chapterId) {
		this.chapterId = chapterId;
	}
	/**
	 * 获取：章PK
	 */
	public Long getChapterId() {
		return chapterId;
	}
	/**
	 * 设置：课程PK
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	/**
	 * 获取：课程PK
	 */
	public Long getCourseId() {
		return courseId;
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
	/**
	 * 设置：删除标记 0.正常; 1.删除
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：删除标记 0.正常; 1.删除
	 */
	public Integer getDr() {
		return dr;
	}
	@Override
	public String toString() {
		return "AdlKnowledgeEntity [knowledgeId=" + knowledgeId + ", knowledgeName=" + knowledgeName + ", knowledgeNo="
				+ knowledgeNo + ", keyPoint=" + keyPoint + ", levelId=" + levelId + ", status=" + status
				+ ", sectionId=" + sectionId + ", chapterId=" + chapterId + ", courseId=" + courseId + ", updateTime="
				+ updateTime + ", createTime=" + createTime + ", dr=" + dr + "]";
	}
	
}

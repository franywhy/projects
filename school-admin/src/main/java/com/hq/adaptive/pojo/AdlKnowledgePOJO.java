package com.hq.adaptive.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 知识点档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:36
 */
public class AdlKnowledgePOJO implements Serializable {
	private static final long serialVersionUID = 1L;

	// 主键
	private Long knowledgeId;
	// 名称
	private String knowledgeName;
	// 编码
	private String knowledgeNo;
	private String keyPointName;
	private Integer keyPoint;
	private String levelName;
	// 启用状态:0.作废;1.正常
	private Integer status;
	// 节PK
	private Long sectionId;
	private String sectionName;
	private String sectionNo;
	private String questiontypeName;
	// 章PK
	private Long chapterId;
	// 课程PK
	private Long courseId;


	/**
	 * 包含知识点集合
	 */
	private List<AdlKnowledgeContainPOJO> childList;
	
	/** 知识点视频 */
	private AdlKnowledgeVideoPOJO adlKnowledgeVideo;
	
	/** 知识点资料*/
	private AdlKnowledgeFilePOJO adlKnowledgeFile;


	public AdlKnowledgeVideoPOJO getAdlKnowledgeVideo() {
		return adlKnowledgeVideo;
	}

	public void setAdlKnowledgeVideo(AdlKnowledgeVideoPOJO adlKnowledgeVideo) {
		this.adlKnowledgeVideo = adlKnowledgeVideo;
	}

	public AdlKnowledgeFilePOJO getAdlKnowledgeFile() {
		return adlKnowledgeFile;
	}

	public void setAdlKnowledgeFile(AdlKnowledgeFilePOJO adlKnowledgeFile) {
		this.adlKnowledgeFile = adlKnowledgeFile;
	}


	public String getKeyPointName() {
		return keyPointName;
	}

	@Override
	public String toString() {
		return "AdlKnowledgePOJO{" +
				"knowledgeId=" + knowledgeId +
				", knowledgeName='" + knowledgeName + '\'' +
				", knowledgeNo='" + knowledgeNo + '\'' +
				", keyPointName='" + keyPointName + '\'' +
				", keyPoint=" + keyPoint +
				", levelName='" + levelName + '\'' +
				", status=" + status +
				", sectionId=" + sectionId +
				", sectionName='" + sectionName + '\'' +
				", sectionNo='" + sectionNo + '\'' +
				", questiontypeName='" + questiontypeName + '\'' +
				", chapterId=" + chapterId +
				", courseId=" + courseId +
				", childList=" + childList +
				", adlKnowledgeVideo=" + adlKnowledgeVideo +
				", adlKnowledgeFile=" + adlKnowledgeFile +
				'}';
	}

	public Integer getKeyPoint() {
		return keyPoint;
	}

	public void setKeyPoint(Integer keyPoint) {
		this.keyPoint = keyPoint;
	}

	public void setKeyPointName(String keyPointName) {

		this.keyPointName = keyPointName;
	}

	public List<AdlKnowledgeContainPOJO> getChildList() {
		return childList;
	}

	public void setChildList(List<AdlKnowledgeContainPOJO> childList) {
		this.childList = childList;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSectionNo() {
		return sectionNo;
	}

	public void setSectionNo(String sectionNo) {
		this.sectionNo = sectionNo;
	}


	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

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

	public String getQuestiontypeName() {
		return questiontypeName;
	}

	public void setQuestiontypeName(String questiontypeName) {
		this.questiontypeName = questiontypeName;
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
}

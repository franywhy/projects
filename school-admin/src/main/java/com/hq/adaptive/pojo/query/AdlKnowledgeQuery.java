package com.hq.adaptive.pojo.query;

/**
 * 知识点档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:36
 */
public class AdlKnowledgeQuery extends BaseQuery {
	/** 节ID */
	private Long sectionId;
	/** 章ID */
	private Long chapterId;
	/** 课程ID */
	private Long courseId;
	//名称
	private String knowledgeName;
	//编码
	private String knowledgeNo;
	//重点:0.正常;1.重点
	private Integer keyPoint;
	//题型PK,请参考静态变量表
	private String questionName;
	//难度PK，请参考静态变量表
	private String levelName;

	public String getKnowledgeName() {
		return knowledgeName;
	}

	public void setKnowledgeName(String knowledgeName) {
		this.knowledgeName = knowledgeName;
	}

	public String getKnowledgeNo() {
		return knowledgeNo;
	}

	public void setKnowledgeNo(String knowledgeNo) {
		this.knowledgeNo = knowledgeNo;
	}

	public Long getChapterId() {
		return chapterId;
	}

	public void setChapterId(Long chapterId) {
		this.chapterId = chapterId;
	}

	@Override
	public String toString() {
		return "AdlKnowledgeQuery{" +
				"sectionId=" + sectionId +
				", chapterId=" + chapterId +
				", courseId=" + courseId +
				", knowledgeName='" + knowledgeName + '\'' +
				", knowledgeNo='" + knowledgeNo + '\'' +
				", keyPoint='" + keyPoint + '\'' +
				", questionName='" + questionName + '\'' +
				", levelName='" + levelName + '\'' +
				'}';
	}

	public Integer getKeyPoint() {
		return keyPoint;
	}

	public void setKeyPoint(Integer keyPoint) {
		this.keyPoint = keyPoint;
	}

	public String getQuestionName() {
		return questionName;
	}

	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

//	public Long getChapterId() {
//		return chapterId;
//	}
//
//	public void setChapterId(Long chapterId) {
//		this.chapterId = chapterId;
//	}

	public Long getSectionId() {
		return sectionId;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

}

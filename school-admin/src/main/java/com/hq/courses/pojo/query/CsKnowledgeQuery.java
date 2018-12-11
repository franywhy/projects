package com.hq.courses.pojo.query;

/**
 * 知识点档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:36
 */
public class CsKnowledgeQuery extends BaseQuery {
	/** 节ID */
	private Long sectionId;
	/** 章ID */
	private Long chapterId;
	/** 课程ID */
	private Long courseId;
	/**
	 * 知识点id
	 */
	private Long knowledgeId;

	//名称
	private String knowledgeName;
	//编码
	private String knowledgeNo;
	//重点:0.正常;1.重点
	private Integer keyPoint;
	//题型PK,请参考静态变量表
	private Integer questionType;
	//难度PK，请参考静态变量表
	private Integer levelId;

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

	public Integer getKeyPoint() {
		return keyPoint;
	}

	public void setKeyPoint(Integer keyPoint) {
		this.keyPoint = keyPoint;
	}

	public Integer getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Integer questionType) {
		this.questionType = questionType;
	}

	public Integer getLevelId() {
		return levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Long getChapterId() {
		return chapterId;
	}

	public void setChapterId(Long chapterId) {
		this.chapterId = chapterId;
	}

	public Long getSectionId() {
		return sectionId;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

	public Long getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(Long knowledgeId) {
		this.knowledgeId = knowledgeId;
	}

	@Override
	public String toString() {
		return "CsKnowledgeQuery{" +
				"sectionId=" + sectionId +
				", chapterId=" + chapterId +
				", courseId=" + courseId +
				", knowledgeId=" + knowledgeId +
				", knowledgeName='" + knowledgeName + '\'' +
				", knowledgeNo='" + knowledgeNo + '\'' +
				", keyPoint=" + keyPoint +
				", questionType=" + questionType +
				", levelId=" + levelId +
				'}';
	}
}

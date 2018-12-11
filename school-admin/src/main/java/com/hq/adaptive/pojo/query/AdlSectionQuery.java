package com.hq.adaptive.pojo.query;

/**
 * 类说明
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017年11月28日
 */
public class AdlSectionQuery extends BaseQuery {
	/** 课程ID */
	private Long courseId;
	/** 章ID */
	private Long chapterId;

	private String sectionName;
	private String sectionNo;

	@Override
	public String toString() {
		return "AdlSectionQuery{" +
				"courseId=" + courseId +
				", chapterId=" + chapterId +
				", sectionName='" + sectionName + '\'' +
				", sectionNo='" + sectionNo + '\'' +
				'}';
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

	public Long getChapterId() {
		return chapterId;
	}

	public void setChapterId(Long chapterId) {
		this.chapterId = chapterId;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

}

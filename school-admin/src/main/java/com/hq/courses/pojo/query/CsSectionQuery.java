package com.hq.courses.pojo.query;

/**
 * 类说明
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017年11月28日
 */
public class CsSectionQuery extends BaseQuery {
	/** 课程ID */
	private Long courseId;
	/** 章ID */
	private Long chapterId;

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

	@Override
	public String toString() {
		return "AdlSectionQuery [courseId=" + courseId + ", chapterId=" + chapterId + "]";
	}

}

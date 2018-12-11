package com.hq.adaptive.pojo.query;

/**
 * 类说明
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017年11月28日
 */
public class AdlChapterQuery extends BaseQuery {

	private Long courseId;

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	@Override
	public String toString() {
		return "AdlChapterQuery [courseId=" + courseId + "]";
	}

}

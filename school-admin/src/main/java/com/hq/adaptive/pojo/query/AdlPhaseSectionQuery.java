package com.hq.adaptive.pojo.query;

/**
 * 评测阶段包含节关系表
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:37
 */
public class AdlPhaseSectionQuery extends BaseQuery {

	// 阶段PK
	private Long phaseId;
	
	//课程PK
	private Long courseId;

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Long getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}

	@Override
	public String toString() {
		return "AdlPhaseSectionQuery [phaseId=" + phaseId + ", courseId=" + courseId + "]";
	}

}

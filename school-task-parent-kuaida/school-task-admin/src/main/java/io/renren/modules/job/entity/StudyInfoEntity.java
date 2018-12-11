package io.renren.modules.job.entity;

import java.math.BigDecimal;
import java.util.Date;

public class StudyInfoEntity {
	
	private Long id;
	private Long userId;
	private Long courseId;
	private Long fullDur;
	private Long watchDur;
	private Long videoDur;
	private Long liveDur;
	private Date creationTime;
	private Date modifyTime;
	private BigDecimal studyPersents;
	private Integer dr;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	public Long getFullDur() {
		return fullDur;
	}
	public void setFullDur(Long fullDur) {
		this.fullDur = fullDur;
	}
	public Long getWatchDur() {
		return watchDur;
	}
	public void setWatchDur(Long watchDur) {
		this.watchDur = watchDur;
	}
	public Long getVideoDur() {
		return videoDur;
	}
	public void setVideoDur(Long videoDur) {
		this.videoDur = videoDur;
	}
	public Long getLiveDur() {
		return liveDur;
	}
	public void setLiveDur(Long liveDur) {
		this.liveDur = liveDur;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public BigDecimal getStudyPersents() {
		return studyPersents;
	}
	public void setStudyPersents(BigDecimal studyPersents) {
		this.studyPersents = studyPersents;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	 
	
}

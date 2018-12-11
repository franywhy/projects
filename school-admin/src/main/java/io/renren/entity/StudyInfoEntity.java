package io.renren.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class StudyInfoEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	//主键ID
	private Long id;
	//学员id
	private Long userId;
	//课程id
	private Long courseId;
	//应出勤时长(毫秒)
	private Long fullDur;
	//直播和录播总时长(毫秒)
	private Long watchDur;
	//录播总时长(毫秒)
	private Long videoDur;
	//直播总时长(毫秒)
	private Long liveDur;
	//听课完成率
	private BigDecimal studyPersents;
	//创建时间
	private Date creationTime;
	//修改时间
	private Date modifyTime;
	//dr
	private int dr;
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
	public BigDecimal getStudyPersents() {
		return studyPersents;
	}
	public void setStudyPersents(BigDecimal studyPersents) {
		this.studyPersents = studyPersents;
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
	public int getDr() {
		return dr;
	}
	public void setDr(int dr) {
		this.dr = dr;
	}
	@Override
	public String toString() {
		return "StudyInfoEntity [id=" + id + ", userId=" + userId + ", courseId=" + courseId + ", fullDur=" + fullDur
				+ ", watchDur=" + watchDur + ", videoDur=" + videoDur + ", liveDur=" + liveDur + ", studyPersents="
				+ studyPersents + ", creationTime=" + creationTime + ", modifyTime=" + modifyTime + ", dr=" + dr + "]";
	}
}

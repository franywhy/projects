package com.hq.learningapi.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class CourseOlivePOJO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long oliveId;

	private String oliveTitle;

	private Date oliveStartTime;

	private Date oliveEndTime;

	private String olivePic;

	private Long liveRoomId;

	private String replayUrl;

	private String teacherName;

	private String teacherAvatar;

	private String teacherIntroduction;

	private String suitable;

	private String content;

	public Long getOliveId() {
		return oliveId;
	}

	public void setOliveId(Long oliveId) {
		this.oliveId = oliveId;
	}

	public String getOliveTitle() {
		return oliveTitle;
	}

	public void setOliveTitle(String oliveTitle) {
		this.oliveTitle = oliveTitle;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
	public Date getOliveStartTime() {
		return oliveStartTime;
	}

	public void setOliveStartTime(Date oliveStartTime) {
		this.oliveStartTime = oliveStartTime;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
	public Date getOliveEndTime() {
		return oliveEndTime;
	}

	public void setOliveEndTime(Date oliveEndTime) {
		this.oliveEndTime = oliveEndTime;
	}

	public String getOlivePic() {
		return olivePic;
	}

	public void setOlivePic(String olivePic) {
		this.olivePic = olivePic;
	}

	public Long getLiveRoomId() {
		return liveRoomId;
	}

	public void setLiveRoomId(Long liveRoomId) {
		this.liveRoomId = liveRoomId;
	}

	public String getReplayUrl() {
		return replayUrl;
	}

	public void setReplayUrl(String replayUrl) {
		this.replayUrl = replayUrl;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTeacherAvatar() {
		return teacherAvatar;
	}

	public void setTeacherAvatar(String teacherAvatar) {
		this.teacherAvatar = teacherAvatar;
	}

	public String getTeacherIntroduction() {
		return teacherIntroduction;
	}

	public void setTeacherIntroduction(String teacherIntroduction) {
		this.teacherIntroduction = teacherIntroduction;
	}

	public String getSuitable() {
		return suitable;
	}

	public void setSuitable(String suitable) {
		this.suitable = suitable;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}

package io.renren.pojo.examschedule;

public class ExamScheduleCreatePOJO {
	// 考试时段名称
	private String scheduleName;
	// 考试年月
	private String scheduleDate;
	// 备注
	private String comments;

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}

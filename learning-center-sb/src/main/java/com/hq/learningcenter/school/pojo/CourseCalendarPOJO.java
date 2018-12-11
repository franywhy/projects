package com.hq.learningcenter.school.pojo;


public class CourseCalendarPOJO {

	private Long userplanId;
	
	private String date;
	
	/*月类型: 0:当月 -1:上个月 1:下个月*/
	private Integer monType;
	
	private Integer isAttend;
	
	private Integer status;

	public Long getUserplanId() {
		return userplanId;
	}

	public void setUserplanId(Long userplanId) {
		this.userplanId = userplanId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getIsAttend() {
		return isAttend;
	}

	public void setIsAttend(Integer isAttend) {
		this.isAttend = isAttend;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getMonType() {
		return monType;
	}

	public void setMonType(Integer monType) {
		this.monType = monType;
	}
}

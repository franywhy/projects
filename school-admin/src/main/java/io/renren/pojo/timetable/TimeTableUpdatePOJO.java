package io.renren.pojo.timetable;

import java.util.Date;
import java.util.List;

public class TimeTableUpdatePOJO {

	private Long number;

	private String name;

	private String comments;

	private String createPerson;

	private String updatePerson;

	private Date createTime;

	private Date updateTime;

	private List<TimeTableDetailPOJO> details;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public String getUpdatePerson() {
		return updatePerson;
	}

	public void setUpdatePerson(String updatePerson) {
		this.updatePerson = updatePerson;
	}

	public List<TimeTableDetailPOJO> getDetails() {
		return details;
	}

	public void setDetails(List<TimeTableDetailPOJO> details) {
		this.details = details;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}

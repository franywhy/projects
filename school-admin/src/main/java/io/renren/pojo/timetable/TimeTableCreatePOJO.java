package io.renren.pojo.timetable;

public class TimeTableCreatePOJO {
	// 名称
	private String name;
	// 备注
	private String comments;
	
	private String createPerson;
	//FastJson实现由bug,暂时按照String处理
	private /*List<TimeTableDetailPOJO>*/String details;

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

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}
	
	
}

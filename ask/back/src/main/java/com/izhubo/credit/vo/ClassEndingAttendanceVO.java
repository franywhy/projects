package com.izhubo.credit.vo;

/**
 * 结课学员考勤数据
 * @author 严志城
 * @Time 2017年3月6日16:37:53
 */
public class ClassEndingAttendanceVO{
	
	/**
	 * 出勤率
	 */
	private Integer attendanceRate;
	
	/**
	 * 科目id
	 */
	private String subjectilesId;
	/**
	 * 学员id
	 */
	private String studentId;
	
	
	

	public String getSubjectilesId() {
		return subjectilesId;
	}


	public void setSubjectilesId(String subjectilesId) {
		this.subjectilesId = subjectilesId;
	}


	public String getStudentId() {
		return studentId;
	}


	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public Integer getAttendanceRate() {
		return attendanceRate;
	}


	public void setAttendanceRate(Integer attendanceRate) {
		this.attendanceRate = attendanceRate;
	}

	
	
	
}

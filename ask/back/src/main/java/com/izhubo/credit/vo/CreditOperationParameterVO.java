package com.izhubo.credit.vo;
/**
 * 直接运算参数vo
 * @author Administrator
 *
 */
public class CreditOperationParameterVO {
	/**
	 * 出勤开始日期
	 */
	private String attendanceBeginDate;
	
	/**
	 * 出勤结束日期
	 */
	private String attendanceEndDate;
	
	
	
	/**
	 * 作业实操开始日期
	 */
	private String pworkBeginDate;
	
	/**
	 * 作业实操结束日期
	 */
	private String pworkEndDate;
	
	
	/**
	 * 作业理论开始日期
	 */
	private String tworkBeginDate;
	
	/**
	 * 作业理论结束日期
	 */
	private String tworkEndDate;
	
	/**
	 * 考试开始日期
	 */
	private String examBeginDate;
	
	/**
	 * 考试结束日期
	 */
	private String examEndDate;
	
	/**
	 * 报名表开始日期
	 */
	private String regBeginDate;
	
	/**
	 * 报名表结束日期
	 */
	private String regEndDate;
	
	/**
	 * 是否计算重复考
	 */
	private boolean retake;
	/**
	 * 是否计算初级职称
	 */
	private boolean junior;
	/**
	 * 是否计算中级职称
	 */
	private boolean medium;
	/**
	 * 是否计算会计证
	 */
	private boolean accounting;
	
	/**
	 * 是否同步老师信息
	 */
	private boolean teacher;
	
	/**
	 * 是否同步报名表
	 */
	private boolean syncreg;
	/**
	 * 是否运算结课考勤
	 */
	private boolean syncatt;
	/**
	 * 是否运算NC成绩单
	 */
	private boolean syncexam;
	/**
	 * 是否运算题库
	 */
	private boolean synctk;
	private String tikuMonth;

	public String getAttendanceBeginDate() {
		return attendanceBeginDate;
	}

	public void setAttendanceBeginDate(String attendanceBeginDate) {
		this.attendanceBeginDate = attendanceBeginDate;
	}

	public String getAttendanceEndDate() {
		return attendanceEndDate;
	}

	public void setAttendanceEndDate(String attendanceEndDate) {
		this.attendanceEndDate = attendanceEndDate;
	}



	public String getExamBeginDate() {
		return examBeginDate;
	}

	public void setExamBeginDate(String examBeginDate) {
		this.examBeginDate = examBeginDate;
	}

	public String getExamEndDate() {
		return examEndDate;
	}

	public void setExamEndDate(String examEndDate) {
		this.examEndDate = examEndDate;
	}

	public String getPworkBeginDate() {
		return pworkBeginDate;
	}

	public void setPworkBeginDate(String pworkBeginDate) {
		this.pworkBeginDate = pworkBeginDate;
	}

	public String getPworkEndDate() {
		return pworkEndDate;
	}

	public void setPworkEndDate(String pworkEndDate) {
		this.pworkEndDate = pworkEndDate;
	}

	public String getTworkBeginDate() {
		return tworkBeginDate;
	}

	public void setTworkBeginDate(String tworkBeginDate) {
		this.tworkBeginDate = tworkBeginDate;
	}

	public String getTworkEndDate() {
		return tworkEndDate;
	}

	public void setTworkEndDate(String tworkEndDate) {
		this.tworkEndDate = tworkEndDate;
	}

	public String getRegBeginDate() {
		return regBeginDate;
	}

	public void setRegBeginDate(String regBeginDate) {
		this.regBeginDate = regBeginDate;
	}

	public String getRegEndDate() {
		return regEndDate;
	}

	public void setRegEndDate(String regEndDate) {
		this.regEndDate = regEndDate;
	}

	public boolean getRetake() {
		return retake;
	}

	public void setRetake(boolean retake) {
		this.retake = retake;
	}

	public boolean getJunior() {
		return junior;
	}

	public void setJunior(boolean junior) {
		this.junior = junior;
	}

	public boolean getMedium() {
		return medium;
	}

	public void setMedium(boolean medium) {
		this.medium = medium;
	}

	public boolean getAccounting() {
		return accounting;
	}

	public void setAccounting(boolean accounting) {
		this.accounting = accounting;
	}

	public boolean getTeacher() {
		return teacher;
	}

	public void setTeacher(boolean teacher) {
		this.teacher = teacher;
	}

	public boolean isSyncreg() {
		return syncreg;
	}

	public void setSyncreg(boolean syncreg) {
		this.syncreg = syncreg;
	}

	public boolean isSyncatt() {
		return syncatt;
	}

	public void setSyncatt(boolean syncatt) {
		this.syncatt = syncatt;
	}

	public boolean isSyncexam() {
		return syncexam;
	}

	public void setSyncexam(boolean syncexam) {
		this.syncexam = syncexam;
	}

	public boolean isSynctk() {
		return synctk;
	}

	public void setSynctk(boolean synctk) {
		this.synctk = synctk;
	}

	public String getTikuMonth() {
		return tikuMonth;
	}

	public void setTikuMonth(String tikuMonth) {
		this.tikuMonth = tikuMonth;
	}
	
}

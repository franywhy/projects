package com.izhubo.credit.vo;
/**
 * 初级职称成绩分数
 * @author 严志城
 * @Time 2017年3月6日16:37:53
 */
public class JuniorLevelAccountantScoreVO {
	private Double swscore;
	private Double jjscore;
	private String ispass;
	/**
	 * 学员id
	 */
	private String studentId;
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	/**
	 * 获取初级会计 实务
	 * @return
	 */
	public Double getSwscore() {
		return swscore;
	}
	/**
	 * 设置初级会计 实务
	 * @param swscore
	 */
	public void setSwscore(Double swscore) {
		this.swscore = swscore;
	}
	/**
	 * 获取经济法基础
	 * @return
	 */
	public Double getJjscore() {
		return jjscore;
	}
	/**
	 * 设置经济法基础
	 * @param jjscore
	 */
	public void setJjscore(Double jjscore) {
		this.jjscore = jjscore;
	}
	/**
	 * 获取是否通过
	 * @return
	 */
	public String getIspass() {
		return ispass;
	}
	/**
	 * 设置是否通过
	 * @param ispass
	 */
	public void setIspass(String ispass) {
		this.ispass = ispass;
	}
	
	
}

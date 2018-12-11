package com.izhubo.credit.vo;
/**
 * 会计证成绩分数 
 * @author 严志城
 */
public class AccountingCertificateScoreVO {
	private Double jichu;
	private Double caijing;
	private Double diansuan;
	private String ispass;
	
	/**
	 * 学员id
	 */
	private String studentId;
	
	/**
	 * 获取学员主键
	 * @return
	 */
	public String getStudentId() {
		return studentId;
	}


	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	/**
	 * 获取会计基础分
	 * @return
	 */
	public Double getJichu() {
		return jichu;
	}
	/**
	 * 设置会计基础分
	 * @param jichu
	 */
	public void setJichu(Double jichu) {
		this.jichu = jichu;
	}
	/**
	 * 获取财经法规和职业道德
	 * @return
	 */
	public Double getCaijing() {
		return caijing;
	}
	/**
	 * 设置财经法规和职业道德
	 * @param caijing
	 */
	public void setCaijing(Double caijing) {
		this.caijing = caijing;
	}
	/**
	 * 获取初级电算化分
	 * @return
	 */
	public Double getDiansuan() {
		return diansuan;
	}
	/**
	 * 设置初级电算化分
	 * @param diansuan
	 */
	public void setDiansuan(Double diansuan) {
		this.diansuan = diansuan;
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

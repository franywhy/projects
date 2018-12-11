package com.izhubo.credit.vo;
/**
 * 中级职称成绩分数
 * @author 严志城
 * @Time 2017年3月6日16:37:53
 */
public class MediumLevelAccountantScoreVO {
	private String ispass;
	private String zhongjishiwu;
	private String zhongjicaiwuguanli;
	private String jingjifa;
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
	 * 获取中级实务
	 * @return
	 */
	public String getZhongjishiwu() {
		return zhongjishiwu;
	}
	/**
	 * 设置中级实务
	 * @param zhongjishiwu
	 */
	public void setZhongjishiwu(String zhongjishiwu) {
		this.zhongjishiwu = zhongjishiwu;
	}
	/**
	 *获取 中级财务管理
	 * @return
	 */
	public String getZhongjicaiwuguanli() {
		return zhongjicaiwuguanli;
	}
	/**
	 * 设置中级财务管理
	 * @param zhongjicaiwuguanli
	 */
	public void setZhongjicaiwuguanli(String zhongjicaiwuguanli) {
		this.zhongjicaiwuguanli = zhongjicaiwuguanli;
	}
	/**
	 * 获取中级经济法
	 * @return
	 */
	public String getJingjifa() {
		return jingjifa;
	}
	/**
	 * 设置中级经济法
	 * @param jingjifa
	 */
	public void setJingjifa(String jingjifa) {
		this.jingjifa = jingjifa;
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

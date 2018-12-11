package com.izhubo.credit.vo;

/**
 * 排课计划
 * @author 严志城
 * @time 2017年3月21日19:29:02
 */
public class ArrangedPlanVO {
	private String pk_arrangedplanhid;
	private String subjectilesid;
	private String subjecttype;
	public String getSubjecttype() {
		return subjecttype;
	}
	public void setSubjecttype(String subjecttype) {
		this.subjecttype = subjecttype;
	}
	public String getPk_arrangedplanhid() {
		return pk_arrangedplanhid;
	}
	public void setPk_arrangedplanhid(String pk_arrangedplanhid) {
		this.pk_arrangedplanhid = pk_arrangedplanhid;
	}
	public String getSubjectilesid() {
		return subjectilesid;
	}
	public void setSubjectilesid(String subjectilesid) {
		this.subjectilesid = subjectilesid;
	}
}

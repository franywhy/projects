package com.hq.courses.pojo;

/**
 * 类说明
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017年12月1日
 */
public class CsConfigPOJO {

	private Long id;
	// 名称
	private String cname;
	// 值
	private String cvalue;
	// 备注
	private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getCvalue() {
		return cvalue;
	}

	public void setCvalue(String cvalue) {
		this.cvalue = cvalue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "AdlConfigPOJO [id=" + id + ", cname=" + cname + ", cvalue=" + cvalue + ", remark=" + remark + "]";
	}

}

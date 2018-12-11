package io.renren.pojo.area;

import java.util.Date;

public class AreaDisplayPOJO {
	// 地区ID
	private Long areaId;
	// 地区名称
	private String areaName;
	// 是否停用
	private Integer status;
	// 创建时间
	private Date createTime;
	// 修改时间
	private Date modifyTime;
	// 创建人
	private String createPerson;
	// 修改人
	private String modifyPerson;
	//NC_ID
	private String ncId;
	//NC_CODE
	private String ncCode;
	

	public String getNcId() {
		return ncId;
	}

	public void setNcId(String ncId) {
		this.ncId = ncId;
	}

	public String getNcCode() {
		return ncCode;
	}

	public void setNcCode(String ncCode) {
		this.ncCode = ncCode;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public String getModifyPerson() {
		return modifyPerson;
	}

	public void setModifyPerson(String modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
}

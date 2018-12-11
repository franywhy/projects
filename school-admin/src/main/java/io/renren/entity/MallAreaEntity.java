package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 省份档案业务表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 16:58:35
 */
public class MallAreaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//地区ID
	private Long areaId;
	//地区名称
	private String areaName;
	//是否停用
	private Integer status;
	//创建时间
	private Date createTime;
	//修改时间
	private Date modifyTime;
	//创建人
	private Long createPerson;
	//修改人
	private Long modifyPerson;
	//业务类型id
	private String schoolId;
	//NC_ID
	private String ncId;
	//NC_CODE
	private String ncCode;

	/**
	 * 设置：地区ID
	 */
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	/**
	 * 获取：地区ID
	 */
	public Long getAreaId() {
		return areaId;
	}
	/**
	 * 设置：地区名称
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	/**
	 * 获取：地区名称
	 */
	public String getAreaName() {
		return areaName;
	}
	/**
	 * 设置：是否停用
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：是否停用
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：修改时间
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getModifyTime() {
		return modifyTime;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}
	/**
	 * 获取：创建人
	 */
	public Long getCreatePerson() {
		return createPerson;
	}
	/**
	 * 设置：修改人
	 */
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	/**
	 * 获取：修改人
	 */
	public Long getModifyPerson() {
		return modifyPerson;
	}
	/**
	 * 设置：业务类型id
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：业务类型id
	 */
	public String getSchoolId() {
		return schoolId;
	}
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
	
	
}

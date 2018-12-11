package io.renren.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;



/**
 * 部门管理
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-25 14:21:58
 */
public class SysDeptEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//部门id
	private Long deptId;
	//上级部门ID，一级部门为0
	private Long parentId;
	//部门名称
	private String name;
	//排序
	private Integer orderNum;
	//NC_ID
	private String ncId;
	//nc_code
	private String ncCode;
	//NC上级部门ID
	private String ncParentId;
	//是否删除  -1：已删除  0：正常
	private Integer dr;
	/**
	 * ztree属性
	 */
	private Boolean open;

	private List<?> list;
	
	/**
	 * 设置：
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：
	 */
	public Long getDeptId() {
		return deptId;
	}
	/**
	 * 设置：上级部门ID，一级部门为0
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：上级部门ID，一级部门为0
	 */
	public Long getParentId() {
		return parentId;
	}
	/**
	 * 设置：部门名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：部门名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：排序
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 获取：排序
	 */
	public Integer getOrderNum() {
		return orderNum;
	}
	/**
	 * 设置：NC_ID
	 */
	public void setNcId(String ncId) {
		this.ncId = ncId;
	}
	/**
	 * 获取：NC_ID
	 */
	public String getNcId() {
		return ncId;
	}
	/**
	 * 设置：NC上级部门ID
	 */
	public void setNcParentId(String ncParentId) {
		this.ncParentId = ncParentId;
	}
	/**
	 * 获取：NC上级部门ID
	 */
	public String getNcParentId() {
		return ncParentId;
	}
	/**
	 * 设置：是否删除  -1：已删除  0：正常
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：是否删除  -1：已删除  0：正常
	 */
	public Integer getDr() {
		return dr;
	}
	public Boolean getOpen() {
		return open;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	}
	public List<?> getList() {
		return list;
	}
	public void setList(List<?> list) {
		this.list = list;
	}
	public String getNcCode() {
		return ncCode;
	}
	public void setNcCode(String ncCode) {
		this.ncCode = ncCode;
	}
	
}

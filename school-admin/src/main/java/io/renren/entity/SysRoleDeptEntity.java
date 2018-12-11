package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 角色与组织对应关系
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-03 17:46:54
 */
public class SysRoleDeptEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//角色ID
	private Long roleId;
	//组织ID
	private Long deptId;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：角色ID
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	/**
	 * 获取：角色ID
	 */
	public Long getRoleId() {
		return roleId;
	}
	/**
	 * 设置：组织ID
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：组织ID
	 */
	public Long getDeptId() {
		return deptId;
	}
}

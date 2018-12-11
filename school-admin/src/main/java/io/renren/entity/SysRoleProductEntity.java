package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 角色与产品对应关系
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-03 16:42:59
 */
public class SysRoleProductEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//角色ID
	private Long roleId;
	//产品ID
	private Long productId;

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
	 * 设置：产品ID
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	/**
	 * 获取：产品ID
	 */
	public Long getProductId() {
		return productId;
	}
}

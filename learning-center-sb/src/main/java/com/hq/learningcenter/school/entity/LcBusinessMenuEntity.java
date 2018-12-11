package com.hq.learningcenter.school.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 业务-菜单关联表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-08 19:27:03
 */
public class LcBusinessMenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long lcBusinessMenuId;
	//业务id
	private String businessId;
	//学习中心-菜单id
	private String menuId;
	//json格式的相关参数
	private String jsonsetting;
	//备注
	private String remark;
	//创建人
	private Long creator;
	//创建时间
	private Date createTime;
	//最后修改人
	private Long modifier;
	//最后修改时间
	private Date modifiedTime;

	/**
	 * 设置：主键
	 */
	public void setLcBusinessMenuId(Long lcBusinessMenuId) {
		this.lcBusinessMenuId = lcBusinessMenuId;
	}
	/**
	 * 获取：主键
	 */
	public Long getLcBusinessMenuId() {
		return lcBusinessMenuId;
	}
	/**
	 * 设置：业务id
	 */
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	/**
	 * 获取：业务id
	 */
	public String getBusinessId() {
		return businessId;
	}
	/**
	 * 设置：学习中心-菜单id
	 */
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	/**
	 * 获取：学习中心-菜单id
	 */
	public String getMenuId() {
		return menuId;
	}
	/**
	 * 设置：json格式的相关参数
	 */
	public void setJsonsetting(String jsonsetting) {
		this.jsonsetting = jsonsetting;
	}
	/**
	 * 获取：json格式的相关参数
	 */
	public String getJsonsetting() {
		return jsonsetting;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * 获取：创建人
	 */
	public Long getCreator() {
		return creator;
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
	 * 设置：最后修改人
	 */
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	/**
	 * 获取：最后修改人
	 */
	public Long getModifier() {
		return modifier;
	}
	/**
	 * 设置：最后修改时间
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	/**
	 * 获取：最后修改时间
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}
}

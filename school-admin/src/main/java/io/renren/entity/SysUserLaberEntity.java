package io.renren.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 标签管理员表
 * 
 * @author vince
 * @date 2018-05-24 17:01:37
 */
public class SysUserLaberEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//系统管理员
	private Long sysUserId;
	//标签id
	private String laberIds;
	//标签名称
	private String laberNames;
	//创建时间
	private Date createTime;
	//更新时间
	private Date modifyTime;
	//创建人id
	private Long creator;
	//修改人id
	private Long modifier;
	//软删除0：正常 1：删除
	private Integer dr;
	//权限状态(1正常2冻结)
	private Integer status;
	//备注
	private String remark;

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
	 * 设置：系统管理员
	 */
	public void setSysUserId(Long sysUserId) {
		this.sysUserId = sysUserId;
	}
	/**
	 * 获取：系统管理员
	 */
	public Long getSysUserId() {
		return sysUserId;
	}
	/**
	 * 设置：标签id
	 */
	public void setLaberIds(String laberIds) {
		this.laberIds = laberIds;
	}
	/**
	 * 获取：标签id
	 */
	public String getLaberIds() {
		return laberIds;
	}
	/**
	 * 设置：标签名称
	 */
	public void setLaberNames(String laberNames) {
		this.laberNames = laberNames;
	}
	/**
	 * 获取：标签名称
	 */
	public String getLaberNames() {
		return laberNames;
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
	 * 设置：更新时间
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getModifyTime() {
		return modifyTime;
	}
	/**
	 * 设置：创建人id
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * 获取：创建人id
	 */
	public Long getCreator() {
		return creator;
	}
	/**
	 * 设置：修改人id
	 */
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	/**
	 * 获取：修改人id
	 */
	public Long getModifier() {
		return modifier;
	}
	/**
	 * 设置：软删除0：正常 1：删除
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：软删除0：正常 1：删除
	 */
	public Integer getDr() {
		return dr;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}

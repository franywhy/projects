package com.hq.courses.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 静态变量表
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:36
 */
public class CsConfigEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	private Long id;
	//类型
	private String ckey;
	//名称
	private String cname;
	//值
	private String cvalue;
	//排序默认0;升序
	private Integer orderNum;
	//状态 0：隐藏 1：显示
	private Integer status;
	//备注
	private String remark;
	//更新时间戳
	private Date updateTime;
	//创建时间戳
	private Date createTime;

	/**
	 * 设置：id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：类型
	 */
	public void setCkey(String ckey) {
		this.ckey = ckey;
	}
	/**
	 * 获取：类型
	 */
	public String getCkey() {
		return ckey;
	}
	/**
	 * 设置：名称
	 */
	public void setCname(String cname) {
		this.cname = cname;
	}
	/**
	 * 获取：名称
	 */
	public String getCname() {
		return cname;
	}
	/**
	 * 设置：值
	 */
	public void setCvalue(String cvalue) {
		this.cvalue = cvalue;
	}
	/**
	 * 获取：值
	 */
	public String getCvalue() {
		return cvalue;
	}
	/**
	 * 设置：排序默认0;升序
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 获取：排序默认0;升序
	 */
	public Integer getOrderNum() {
		return orderNum;
	}
	/**
	 * 设置：状态 0：隐藏 1：显示
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态 0：隐藏 1：显示
	 */
	public Integer getStatus() {
		return status;
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
	 * 设置：更新时间戳
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间戳
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：创建时间戳
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间戳
	 */
	public Date getCreateTime() {
		return createTime;
	}
}

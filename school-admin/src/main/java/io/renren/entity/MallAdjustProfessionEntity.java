package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 特殊情况-转专业
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-08-11 15:16:05
 */
public class MallAdjustProfessionEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//学员ID
	private Long userId;
	//订单ID
	private Long orderId;
	//原来专业
	private Long lastProfessionId;
	//创建时间
	private Date createTime;
	//是否删除
	private Integer dr;
	//是否存档相关申请,0:否,1:是
	private Integer applystatus;

	/**
	 * 设置：主键
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：学员ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：学员ID
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：订单ID
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	/**
	 * 获取：订单ID
	 */
	public Long getOrderId() {
		return orderId;
	}
	/**
	 * 设置：原来专业
	 */
	public void setLastProfessionId(Long lastProfessionId) {
		this.lastProfessionId = lastProfessionId;
	}
	/**
	 * 获取：原来专业
	 */
	public Long getLastProfessionId() {
		return lastProfessionId;
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
	 * 设置：是否删除
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：是否删除
	 */
	public Integer getDr() {
		return dr;
	}
	/**
	 * 设置：是否存档相关申请,0:否,1:是
	 */
	public void setApplystatus(Integer applystatus) {
		this.applystatus = applystatus;
	}
	/**
	 * 获取：是否存档相关申请,0:否,1:是
	 */
	public Integer getApplystatus() {
		return applystatus;
	}
}

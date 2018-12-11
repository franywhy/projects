package io.renren.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-03-09 11:28:21
 */
public class ClassToTkLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//
	private Date createtime;
	//用户手机
	private String userMobile;
	//用户id
	private Long userId;
	//商品id
	private Long goodId;
	//推送内容
	private String pushJson;
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
	 * 设置：
	 */
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	/**
	 * 获取：
	 */
	public Date getCreatetime() {
		return createtime;
	}
	/**
	 * 设置：用户手机
	 */
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	/**
	 * 获取：用户手机
	 */
	public String getUserMobile() {
		return userMobile;
	}
	/**
	 * 设置：用户id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户id
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：商品id
	 */
	public void setGoodId(Long goodId) {
		this.goodId = goodId;
	}
	/**
	 * 获取：商品id
	 */
	public Long getGoodId() {
		return goodId;
	}
	/**
	 * 设置：推送内容
	 */
	public void setPushJson(String pushJson) {
		this.pushJson = pushJson;
	}
	/**
	 * 获取：推送内容
	 */
	public String getPushJson() {
		return pushJson;
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
}

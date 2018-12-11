package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 商机记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-28 15:14:18
 */
public class MallBussinessOppEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Integer id;
	//用户ID
	private Long userId;
	//用户昵称
	private String nickName;
	//手机
	private String userMobile;
	//创建时间
	private Date createTime;
	//平台ID
	private String schoolId;

	/**
	 * 设置：主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：用户ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户ID
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：用户昵称
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	/**
	 * 获取：用户昵称
	 */
	public String getNickName() {
		return nickName;
	}
	/**
	 * 设置：手机
	 */
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	/**
	 * 获取：手机
	 */
	public String getUserMobile() {
		return userMobile;
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
	 * 设置：平台ID
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：平台ID
	 */
	public String getSchoolId() {
		return schoolId;
	}
}

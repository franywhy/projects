package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;


/**
 * 标签管理员表
 * 
 * @author vince
 * @date 2018-05-29 17:01:37
 */
public class SysUserLaberPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//创建时间
	private Date createTime;
	//系统管理员ID
	private Long sysUserId;
	//系统管理员名称
	private String nickName;
	//系统管理员手机号码
	private String mobile;
	//标签id
	private String laberIds;
	//标签名称
	private String laberNames;
	//权限状态(1正常2冻结)
	private Integer status;
	//创建人名称
	private String creationName;
	//更新时间
	private Date modifyTime;
	//备注
	private String remark;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getSysUserId() {
		return sysUserId;
	}
	public void setSysUserId(Long sysUserId) {
		this.sysUserId = sysUserId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getLaberIds() {
		return laberIds;
	}
	public void setLaberIds(String laberIds) {
		this.laberIds = laberIds;
	}
	public String getLaberNames() {
		return laberNames;
	}
	public void setLaberNames(String laberNames) {
		this.laberNames = laberNames;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCreationName() {
		return creationName;
	}
	public void setCreationName(String creationName) {
		this.creationName = creationName;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}

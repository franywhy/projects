package com.hq.learningcenter.school.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-27 10:26:24
 */
public class UsersEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//ID
	private Long userId;
	//平台ID
	private String schoolId ;
	//是否删除   0.未删除  1.删除   用于软删除
	private Integer dr;
	//手机号码(登录账号)
	private String mobile;
	//用户昵称
	private String nickName;
	//头像地址
	private String pic;
	//状态  0：禁用   1：正常
	private Integer status = 1;
	//性别0女，1男，2保密
	private Integer sex;
	//登录密码
	private String password;
	//创建用户
	private Long creator;
	//创建时间
	private Date creationTime;
	//修改用户
	private Long modifier;
	//修改时间
	private Date modifiedTime;
	//最近登录时间
	private Date lastLoginTime;
	//最近登录IP
	private String lastLoginIp;
	//来源 0.正常注册;1.后台注册;2.NC导入
	private Integer channel;
	//备注
	private String remake;
	//性别名称
	private String sexName;
	//邮箱
	private String email;
	//平台ID
	private Long deptId;
	//nc_id
	private String ncId;
	
	
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getCreator() {
		return creator;
	}
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Long getModifier() {
		return modifier;
	}
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getLastLoginIp() {
		return lastLoginIp;
	}
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	public Integer getChannel() {
		return channel;
	}
	public void setChannel(Integer channel) {
		this.channel = channel;
	}
	public String getRemake() {
		return remake;
	}
	public void setRemake(String remake) {
		this.remake = remake;
	}
	public String getSexName() {
		return sexName;
	}
	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
	
	public String getNcId() {
		return ncId;
	}
	public void setNcId(String ncId) {
		this.ncId = ncId;
	}
	@Override
	public String toString() {
		return "UsersEntity [userId=" + userId + ", schoolId=" + schoolId + ", dr=" + dr + ", mobile=" + mobile
				+ ", nickName=" + nickName + ", pic=" + pic + ", status=" + status + ", sex=" + sex + ", password="
				+ password + ", creator=" + creator + ", creationTime=" + creationTime + ", modifier=" + modifier
				+ ", modifiedTime=" + modifiedTime + ", lastLoginTime=" + lastLoginTime + ", lastLoginIp=" + lastLoginIp
				+ ", channel=" + channel + ", remake=" + remake + ", sexName=" + sexName + ", email=" + email
				+ ", deptId=" + deptId + ", ncId=" + ncId + "]";
	}

}

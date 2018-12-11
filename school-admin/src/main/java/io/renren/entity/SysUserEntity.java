package io.renren.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;



/**
 * 系统用户
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-10 09:30:36
 */
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long userId;
	//账号
	private String username;
	//密码
	private String password;
	//邮箱
	private String email;
	//手机号
	private String mobile;
	//微信号
	private String wxCode;
	//状态  0：禁用   1：正常
	private Integer status;
	//创建时间
	private Date createTime;
	//班主任  0：不是  1：是
	private Integer classTeacher;
	//教学老师  0：不是 1：是
	private Integer teacher;
	//平台ID
	private String schoolId;
	//昵称
	private String nickName;
	//身份证号码
	private String idCode;
	//
	private String mId;
	//部门ID
	private Long deptId;
	//部门名称
	private String deptName;
	//当前时间戳
	private Date ts;
	//客服id
		private int ownerId;
	/**
	 * 角色ID列表
	 */
	private List<Long> roleIdList;
	
	
	
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public List<Long> getRoleIdList() {
		return roleIdList;
	}
	public void setRoleIdList(List<Long> roleIdList) {
		this.roleIdList = roleIdList;
	}
	/**
	 * 设置：
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：账号
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取：账号
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 设置：密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 获取：密码
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 设置：邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 获取：邮箱
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 设置：手机号
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：手机号
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * 设置：微信号
	 */
	public void setWxCode(String wxCode) {
		this.wxCode = wxCode;
	}
	/**
	 * 获取：微信号
	 */
	public String getWxCode() {
		return wxCode;
	}
	/**
	 * 设置：状态  0：禁用   1：正常
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态  0：禁用   1：正常
	 */
	public Integer getStatus() {
		return status;
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
	 * 设置：班主任  0：不是  1：是
	 */
	public void setClassTeacher(Integer classTeacher) {
		this.classTeacher = classTeacher;
	}
	/**
	 * 获取：班主任  0：不是  1：是
	 */
	public Integer getClassTeacher() {
		return classTeacher;
	}
	/**
	 * 设置：教学老师  0：不是 1：是
	 */
	public void setTeacher(Integer teacher) {
		this.teacher = teacher;
	}
	/**
	 * 获取：教学老师  0：不是 1：是
	 */
	public Integer getTeacher() {
		return teacher;
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
	/**
	 * 设置：昵称
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	/**
	 * 获取：昵称
	 */
	public String getNickName() {
		return nickName;
	}
	/**
	 * 设置：身份证号码
	 */
	public void setIdCode(String idCode) {
		this.idCode = idCode;
	}
	/**
	 * 获取：身份证号码
	 */
	public String getIdCode() {
		return idCode;
	}
	/**
	 * 设置：
	 */
	public void setMId(String mId) {
		this.mId = mId;
	}
	/**
	 * 获取：
	 */
	public String getMId() {
		return mId;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	
}

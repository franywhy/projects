package io.renren.entity;

import java.io.Serializable;
import java.util.Date;

 public class SysVersionEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//自增主键
	private Long id;
	//版本类型:ios android
	private String clientType;
	//内部版本号
	private Integer versionCode;
	//外部版本号
	private String versionName;
	//0代表推荐升级，1代表强制升级
	private Integer updateStrategy;
	//下载地址
	private String downloadUrl;
	//更新内容细节
	private String updateDetail;
	//MD5值
	private String md5;
	//1代表当前版本，0代表过期版本
	private Integer isActive;
	//1代表需要灰度升级，0代表不需要灰度升级
	private Integer isGreyUpdate;
	//灰度用户列表
	private String updateUserList;
	//业务线标识
	private String schoolId;
	//记录创建者
	private Long createPerson;
	//记录创建者名
	private String createPersonName;
    //	状态
	private String status;
	//最小版本编码
     private Integer minVersionCode;


     public Integer getMinVersionCode() {
         return minVersionCode;
     }

     public void setMinVersionCode(Integer minVersionCode) {
         this.minVersionCode = minVersionCode;
     }
     public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatePersonName() {
		return createPersonName;
	}
	public void setCreatePersonName(String createPersonName) {
		this.createPersonName = createPersonName;
	}
	public String getModifyPersonName() {
		return modifyPersonName;
	}
	public void setModifyPersonName(String modifyPersonName) {
		this.modifyPersonName = modifyPersonName;
	}
	//记录修改者
	private Long modifyPerson;
	//记录修改者名
	private String modifyPersonName;
	//创建时间
	private Date createTime;
	//修改时间
	private Date modifyTime;

	/**
	 * 设置：自增主键
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：自增主键
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：版本类型:ios android
	 */
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	/**
	 * 获取：版本类型:ios android
	 */
	public String getClientType() {
		return clientType;
	}
	/**
	 * 设置：内部版本号
	 */
	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}
	/**
	 * 获取：内部版本号
	 */
	public Integer getVersionCode() {
		return versionCode;
	}
	/**
	 * 设置：外部版本号
	 */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	/**
	 * 获取：外部版本号
	 */
	public String getVersionName() {
		return versionName;
	}
	/**
	 * 设置：0代表推荐升级，1代表强制升级
	 */
	public void setUpdateStrategy(Integer updateStrategy) {
		this.updateStrategy = updateStrategy;
	}
	/**
	 * 获取：0代表推荐升级，1代表强制升级
	 */
	public Integer getUpdateStrategy() {
		return updateStrategy;
	}
	/**
	 * 设置：下载地址
	 */
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	/**
	 * 获取：下载地址
	 */
	public String getDownloadUrl() {
		return downloadUrl;
	}
	/**
	 * 设置：更新内容细节
	 */
	public void setUpdateDetail(String updateDetail) {
		this.updateDetail = updateDetail;
	}
	/**
	 * 获取：更新内容细节
	 */
	public String getUpdateDetail() {
		return updateDetail;
	}
	/**
	 * 设置：MD5值
	 */
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	/**
	 * 获取：MD5值
	 */
	public String getMd5() {
		return md5;
	}
	/**
	 * 设置：1代表当前版本，0代表过期版本
	 */
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	/**
	 * 获取：1代表当前版本，0代表过期版本
	 */
	public Integer getIsActive() {
		return isActive;
	}
	/**
	 * 设置：1代表需要灰度升级，0代表不需要灰度升级
	 */
	public void setIsGreyUpdate(Integer isGreyUpdate) {
		this.isGreyUpdate = isGreyUpdate;
	}
	/**
	 * 获取：1代表需要灰度升级，0代表不需要灰度升级
	 */
	public Integer getIsGreyUpdate() {
		return isGreyUpdate;
	}
	/**
	 * 设置：灰度用户列表
	 */
	public void setUpdateUserList(String updateUserList) {
		this.updateUserList = updateUserList;
	}
	/**
	 * 获取：灰度用户列表
	 */
	public String getUpdateUserList() {
		return updateUserList;
	}
	/**
	 * 设置：业务线标识
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：业务线标识
	 */
	public String getSchoolId() {
		return schoolId;
	}
	/**
	 * 设置：记录创建者
	 */
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}
	/**
	 * 获取：记录创建者
	 */
	public Long getCreatePerson() {
		return createPerson;
	}
	/**
	 * 设置：记录修改者
	 */
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	/**
	 * 获取：记录修改者
	 */
	public Long getModifyPerson() {
		return modifyPerson;
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
	 * 设置：修改时间
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getModifyTime() {
		return modifyTime;
	}
}

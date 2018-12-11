package io.renren.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 专业档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-21 16:15:19
 */
public class MallProfessionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// 主键
	private Long professionId;
	// APP图片
	private String appPic;
	// 专业名称
	private String professionName;
	// 是否删除
	private Integer dr;
	// 创建用户
	private Long createPerson;
	// 创建时间
	private Date creationTime;
	// 修改用户
	private Long modifyPerson;
	// 修改时间
	private Date modifiedTime;
	// 机构ID
	private String schoolId;
	// 是否使用
	private Integer status;
	// 排序
	private Integer orderNum;
	// 专业封面
	private String pic;
	// 品牌封面
	private String brandPic;
	// 专业简介
	private String remark;
	// 課程封面
	private String coursePic;
	// 试听地址
	private String auditionUrl;
	//产品线
	private Long productId;

	@Override
	public String toString() {
		return "MallProfessionEntity [professionId=" + professionId + ", appPic=" + appPic + ", professionName=" + professionName + ", dr=" + dr
				+ ", createPerson=" + createPerson + ", creationTime=" + creationTime + ", modifyPerson=" + modifyPerson + ", modifiedTime="
				+ modifiedTime + ", schoolId=" + schoolId + ", status=" + status + ", orderNum=" + orderNum + ", pic=" + pic + ", brandPic="
				+ brandPic + ", remark=" + remark + ", coursePic=" + coursePic + ", auditionUrl=" + auditionUrl + ", productId=" + productId + "]";
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getAuditionUrl() {
		return auditionUrl;
	}

	public void setAuditionUrl(String auditionUrl) {
		this.auditionUrl = auditionUrl;
	}

	public String getCoursePic() {
		return coursePic;
	}

	public void setCoursePic(String coursePic) {
		this.coursePic = coursePic;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getBrandPic() {
		return brandPic;
	}

	public void setBrandPic(String brandPic) {
		this.brandPic = brandPic;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 设置：主键
	 */
	public void setProfessionId(Long professionId) {
		this.professionId = professionId;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	/**
	 * 获取：主键
	 */
	public Long getProfessionId() {
		return professionId;
	}

	/**
	 * 设置：APP图片
	 */
	public void setAppPic(String appPic) {
		this.appPic = appPic;
	}

	/**
	 * 获取：APP图片
	 */
	public String getAppPic() {
		return appPic;
	}

	/**
	 * 设置：专业名称
	 */
	public void setProfessionName(String professionName) {
		this.professionName = professionName;
	}

	/**
	 * 获取：专业名称
	 */
	public String getProfessionName() {
		return professionName;
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
	 * 设置：创建用户
	 */
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}

	/**
	 * 获取：创建用户
	 */
	public Long getCreatePerson() {
		return createPerson;
	}

	/**
	 * 设置：创建时间
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * 获取：创建时间
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * 设置：修改用户
	 */
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}

	/**
	 * 获取：修改用户
	 */
	public Long getModifyPerson() {
		return modifyPerson;
	}

	/**
	 * 设置：修改时间
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	/**
	 * 获取：修改时间
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}

	/**
	 * 设置：机构ID
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	/**
	 * 获取：机构ID
	 */
	public String getSchoolId() {
		return schoolId;
	}

	/**
	 * 设置：是否使用
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取：是否使用
	 */
	public Integer getStatus() {
		return status;
	}
}

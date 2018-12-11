package io.renren.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;




/**
 * 报考登记
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-26 15:21:19
 */
public class CourseExamRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long examRecordId;
	//单号
	private String examRecordNo;
	//备注
	private String remark;
	//平台ID
	private String schoolId;
	//创建时间
	private Date createTime;
	//修改时间
	private Date modifyTime;
	//创建人
	private Long createPerson;
	//创建人名
		private String createdName;
	//修改人
	private Long modifyPerson;
	//修改人名
	private String modifiedName;
//	手机号码
	private String mobile;
//	课程编号
	private String courseNo;
	
	//产品线ID
	private Long productId;
	//产品线名称
	private String productName;
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCourseNo() {
		return courseNo;
	}
	public void setCourseNo(String courseNo) {
		this.courseNo = courseNo;
	}
	public String getCreatedName() {
			return createdName;
		}
		public void setCreatedName(String createdName) {
			this.createdName = createdName;
		}
	public String getModifiedName() {
			return modifiedName;
		}
		public void setModifiedName(String modifiedName) {
			this.modifiedName = modifiedName;
		}
	//dr
	private Integer dr;
	//审核状态:0.未通过;1.通过
	private Integer audited;
	
	//子表集合
	private List<CourseExamRecordDetailEntity> detailList;
	
	
	public List<CourseExamRecordDetailEntity> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<CourseExamRecordDetailEntity> detailList) {
		this.detailList = detailList;
	}

	/**
	 * 设置：主键
	 */
	public void setExamRecordId(Long examRecordId) {
		this.examRecordId = examRecordId;
	}
	/**
	 * 获取：主键
	 */
	public Long getExamRecordId() {
		return examRecordId;
	}
	/**
	 * 设置：单号
	 */
	public void setExamRecordNo(String examRecordNo) {
		this.examRecordNo = examRecordNo;
	}
	/**
	 * 获取：单号
	 */
	public String getExamRecordNo() {
		return examRecordNo;
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
	/**
	 * 设置：创建人
	 */
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}
	/**
	 * 获取：创建人
	 */
	public Long getCreatePerson() {
		return createPerson;
	}
	/**
	 * 设置：修改人
	 */
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	/**
	 * 获取：修改人
	 */
	public Long getModifyPerson() {
		return modifyPerson;
	}
	/**
	 * 设置：dr
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：dr
	 */
	public Integer getDr() {
		return dr;
	}
	/**
	 * 设置：审核状态:0.未通过;1.通过
	 */
	public void setAudited(Integer audited) {
		this.audited = audited;
	}
	/**
	 * 获取：审核状态:0.未通过;1.通过
	 */
	public Integer getAudited() {
		return audited;
	}
}

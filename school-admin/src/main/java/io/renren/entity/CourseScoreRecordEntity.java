package io.renren.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;



/**
 * 分数登记
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-04 09:27:06
 */
public class CourseScoreRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long scoreRecordId;
	//单号
	private String scoreRecordNo;
	//报考登记PK
	private Long examRecordId;
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
	//修改人
	private Long modifyPerson;
	//dr
	private Integer dr;
	//审核状态:0.未通过;1.通过
	private Integer audited;
	
	private String productName;
	
	private long productId;
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	private List<CourseScoreRecordDetailEntity> detailList; 

	
	public List<CourseScoreRecordDetailEntity> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<CourseScoreRecordDetailEntity> detailList) {
		this.detailList = detailList;
	}
	/**
	 * 设置：主键
	 */
	public void setScoreRecordId(Long scoreRecordId) {
		this.scoreRecordId = scoreRecordId;
	}
	/**
	 * 获取：主键
	 */
	public Long getScoreRecordId() {
		return scoreRecordId;
	}
	/**
	 * 设置：单号
	 */
	public void setScoreRecordNo(String scoreRecordNo) {
		this.scoreRecordNo = scoreRecordNo;
	}
	/**
	 * 获取：单号
	 */
	public String getScoreRecordNo() {
		return scoreRecordNo;
	}
	/**
	 * 设置：报考登记PK
	 */
	public void setExamRecordId(Long examRecordId) {
		this.examRecordId = examRecordId;
	}
	/**
	 * 获取：报考登记PK
	 */
	public Long getExamRecordId() {
		return examRecordId;
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

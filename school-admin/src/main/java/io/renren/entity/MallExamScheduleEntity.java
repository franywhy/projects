package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 考试时刻表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-24 16:39:34
 */
public class MallExamScheduleEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//表格自增ID
	private Long id;
	//考试时段名称
	private String scheduleName;
	//考试年月
	private String scheduleDate;
	//创建人
	private Long createPerson;
	//修改人
	private Long modifyPerson;
	//创建时间
	private Date createTime;
	//修改时间
	private Date modifyTime;
	//备注
	private String comments;
	//是否启用
	private Integer status;
	//机构id
	private String schoolId;
	//是否删除0：否  1：是
	private Integer dr;
	//产品线PK
	private Long productId;
	
	
	@Override
	public String toString() {
		return "MallExamScheduleEntity [id=" + id + ", scheduleName=" + scheduleName + ", scheduleDate=" + scheduleDate + ", createPerson="
				+ createPerson + ", modifyPerson=" + modifyPerson + ", createTime=" + createTime + ", modifyTime=" + modifyTime + ", comments="
				+ comments + ", status=" + status + ", schoolId=" + schoolId + ", dr=" + dr + ", productId=" + productId + "]";
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 设置：表格自增ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：表格自增ID
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：考试时段名称
	 */
	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}
	/**
	 * 获取：考试时段名称
	 */
	public String getScheduleName() {
		return scheduleName;
	}
	/**
	 * 设置：考试年月
	 */
	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	/**
	 * 获取：考试年月
	 */
	public String getScheduleDate() {
		return scheduleDate;
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
	 * 设置：备注
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**
	 * 获取：备注
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * 设置：是否启用
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：是否启用
	 */
	public Integer getStatus() {
		return status;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
}

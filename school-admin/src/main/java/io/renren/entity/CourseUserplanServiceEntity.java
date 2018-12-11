package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 服务记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-20 16:47:00
 */
public class CourseUserplanServiceEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long userplanServiceId;
	//学员规划PK
	private Long userplanId;
	//服务类型:0.系统记录1.教学老师记录
	private Integer type;
	//服务时间
	private Date userplanServiceTime;
	//下次跟进时间
	private Date nextTime;
	//服务名称
	private String serviceTypeId;
	//服务内容
	private String serviceTypeContent;
	//服务反馈名称
	private String serviceFbId;
	//服务反馈内容
	private String serviceFbContent;
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
	//备注
	private String remark;

	/**
	 * 设置：主键
	 */
	public void setUserplanServiceId(Long userplanServiceId) {
		this.userplanServiceId = userplanServiceId;
	}
	/**
	 * 获取：主键
	 */
	public Long getUserplanServiceId() {
		return userplanServiceId;
	}
	/**
	 * 设置：学员规划PK
	 */
	public void setUserplanId(Long userplanId) {
		this.userplanId = userplanId;
	}
	/**
	 * 获取：学员规划PK
	 */
	public Long getUserplanId() {
		return userplanId;
	}
	/**
	 * 设置：服务类型:0.系统记录1.教学老师记录
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：服务类型:0.系统记录1.教学老师记录
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：服务时间
	 */
	public void setUserplanServiceTime(Date userplanServiceTime) {
		this.userplanServiceTime = userplanServiceTime;
	}
	/**
	 * 获取：服务时间
	 */
	public Date getUserplanServiceTime() {
		return userplanServiceTime;
	}
	/**
	 * 设置：下次跟进时间
	 */
	public void setNextTime(Date nextTime) {
		this.nextTime = nextTime;
	}
	/**
	 * 获取：下次跟进时间
	 */
	public Date getNextTime() {
		return nextTime;
	}
	/**
	 * 设置：服务名称
	 */
	public void setServiceTypeId(String serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}
	/**
	 * 获取：服务名称
	 */
	public String getServiceTypeId() {
		return serviceTypeId;
	}
	/**
	 * 设置：服务内容
	 */
	public void setServiceTypeContent(String serviceTypeContent) {
		this.serviceTypeContent = serviceTypeContent;
	}
	/**
	 * 获取：服务内容
	 */
	public String getServiceTypeContent() {
		return serviceTypeContent;
	}
	/**
	 * 设置：服务反馈名称
	 */
	public void setServiceFbId(String serviceFbId) {
		this.serviceFbId = serviceFbId;
	}
	/**
	 * 获取：服务反馈名称
	 */
	public String getServiceFbId() {
		return serviceFbId;
	}
	/**
	 * 设置：服务反馈内容
	 */
	public void setServiceFbContent(String serviceFbContent) {
		this.serviceFbContent = serviceFbContent;
	}
	/**
	 * 获取：服务反馈内容
	 */
	public String getServiceFbContent() {
		return serviceFbContent;
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

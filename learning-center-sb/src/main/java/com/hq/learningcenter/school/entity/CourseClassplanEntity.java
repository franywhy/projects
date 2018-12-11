package com.hq.learningcenter.school.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 排课计划表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-27 09:58:34
 */
public class CourseClassplanEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//排课计划ID
	private String classplanId;
	//排课计划名称
	private String classplanName;
	//课程PK
	private Long courseId;
	//直播时间说明
	private String classplanLiveDetail;
	//备注
	private String remark;
	//开课日期
	private Date startTime;
	//授课老师PK
	private Long teacherId;
	//上课时点PK
	private Long timetableId;
	//直播间PK
	private Long liveRoomId;
	//直播室PK
	private Long studioId;
	//创建用户
	private Long creator;
	//创建时间
	private Date creationTime;
	//最近修改用户
	private Long modifier;
	//最近修改日期
	private Date modifiedTime;
	//平台PK
	private String schoolId;
	//删除标志0正常1删除
	private Integer dr;
	//状态0：作废 1：正常 2：结课
	private Integer status;
	//审核状态0：不通过 1：通过
	private Integer isAudited;
	//是否公开课 0：是   1：否
	private Integer isOpen;
	//资料PK
	private Long materialId;
	//
	private Date ts;
	//产品ID
	private Long productId;

	/**
	 * 设置：排课计划ID
	 */
	public void setClassplanId(String classplanId) {
		this.classplanId = classplanId;
	}
	/**
	 * 获取：排课计划ID
	 */
	public String getClassplanId() {
		return classplanId;
	}
	/**
	 * 设置：排课计划名称
	 */
	public void setClassplanName(String classplanName) {
		this.classplanName = classplanName;
	}
	/**
	 * 获取：排课计划名称
	 */
	public String getClassplanName() {
		return classplanName;
	}
	/**
	 * 设置：课程PK
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	/**
	 * 获取：课程PK
	 */
	public Long getCourseId() {
		return courseId;
	}
	/**
	 * 设置：直播时间说明
	 */
	public void setClassplanLiveDetail(String classplanLiveDetail) {
		this.classplanLiveDetail = classplanLiveDetail;
	}
	/**
	 * 获取：直播时间说明
	 */
	public String getClassplanLiveDetail() {
		return classplanLiveDetail;
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
	 * 设置：开课日期
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：开课日期
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * 设置：授课老师PK
	 */
	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}
	/**
	 * 获取：授课老师PK
	 */
	public Long getTeacherId() {
		return teacherId;
	}
	/**
	 * 设置：上课时点PK
	 */
	public void setTimetableId(Long timetableId) {
		this.timetableId = timetableId;
	}
	/**
	 * 获取：上课时点PK
	 */
	public Long getTimetableId() {
		return timetableId;
	}
	/**
	 * 设置：直播间PK
	 */
	public void setLiveRoomId(Long liveRoomId) {
		this.liveRoomId = liveRoomId;
	}
	/**
	 * 获取：直播间PK
	 */
	public Long getLiveRoomId() {
		return liveRoomId;
	}
	/**
	 * 设置：直播室PK
	 */
	public void setStudioId(Long studioId) {
		this.studioId = studioId;
	}
	/**
	 * 获取：直播室PK
	 */
	public Long getStudioId() {
		return studioId;
	}
	/**
	 * 设置：创建用户
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * 获取：创建用户
	 */
	public Long getCreator() {
		return creator;
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
	 * 设置：最近修改用户
	 */
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	/**
	 * 获取：最近修改用户
	 */
	public Long getModifier() {
		return modifier;
	}
	/**
	 * 设置：最近修改日期
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	/**
	 * 获取：最近修改日期
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}
	/**
	 * 设置：平台PK
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：平台PK
	 */
	public String getSchoolId() {
		return schoolId;
	}
	/**
	 * 设置：删除标志0正常1删除
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：删除标志0正常1删除
	 */
	public Integer getDr() {
		return dr;
	}
	/**
	 * 设置：状态0：作废 1：正常 2：结课
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态0：作废 1：正常 2：结课
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：审核状态0：不通过 1：通过
	 */
	public void setIsAudited(Integer isAudited) {
		this.isAudited = isAudited;
	}
	/**
	 * 获取：审核状态0：不通过 1：通过
	 */
	public Integer getIsAudited() {
		return isAudited;
	}
	/**
	 * 设置：是否公开课 0：是   1：否
	 */
	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}
	/**
	 * 获取：是否公开课 0：是   1：否
	 */
	public Integer getIsOpen() {
		return isOpen;
	}
	/**
	 * 设置：资料PK
	 */
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	/**
	 * 获取：资料PK
	 */
	public Long getMaterialId() {
		return materialId;
	}
	/**
	 * 设置：
	 */
	public void setTs(Date ts) {
		this.ts = ts;
	}
	/**
	 * 获取：
	 */
	public Date getTs() {
		return ts;
	}
	/**
	 * 设置：产品ID
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	/**
	 * 获取：产品ID
	 */
	public Long getProductId() {
		return productId;
	}
}

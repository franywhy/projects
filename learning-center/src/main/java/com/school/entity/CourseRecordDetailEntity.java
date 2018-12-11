package com.school.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 课程录播
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-11 16:49:39
 */
public class CourseRecordDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long recordId;
	//课程PK
	private Long courseId;
	//父级ID，一级章节为0
	private Long parentId;
	//名称
	private String name;
	//授课老师id
	private Long userId;
	//类型0：章  1：节
	private Integer type;
	//录播课id
	private String vid;
	//保利威视视频名称
	private String polyvName;
	//时长
	private String duration;
	//上传时间
	private Date ptime;
	//首截图
	private String firstImage;
	//排序
	private Long orderNum;
	//创建用户
	private Long createPerson;
	//创建时间
	private Date creationTime;
	//最近修改用户
	private Long modifyPerson;
	//最近修改日期
	private Date modifiedTime;

	/**
	 * 设置：主键
	 */
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	/**
	 * 获取：主键
	 */
	public Long getRecordId() {
		return recordId;
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
	 * 设置：父级ID，一级章节为0
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：父级ID，一级章节为0
	 */
	public Long getParentId() {
		return parentId;
	}
	/**
	 * 设置：名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：授课老师id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：授课老师id
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：类型0：章  1：节
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：类型0：章  1：节
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：录播课id
	 */
	public void setVid(String vid) {
		this.vid = vid;
	}
	/**
	 * 获取：录播课id
	 */
	public String getVid() {
		return vid;
	}
	/**
	 * 设置：保利威视视频名称
	 */
	public void setPolyvName(String polyvName) {
		this.polyvName = polyvName;
	}
	/**
	 * 获取：保利威视视频名称
	 */
	public String getPolyvName() {
		return polyvName;
	}
	/**
	 * 设置：时长
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}
	/**
	 * 获取：时长
	 */
	public String getDuration() {
		return duration;
	}
	/**
	 * 设置：上传时间
	 */
	public void setPtime(Date ptime) {
		this.ptime = ptime;
	}
	/**
	 * 获取：上传时间
	 */
	public Date getPtime() {
		return ptime;
	}
	/**
	 * 设置：首截图
	 */
	public void setFirstImage(String firstImage) {
		this.firstImage = firstImage;
	}
	/**
	 * 获取：首截图
	 */
	public String getFirstImage() {
		return firstImage;
	}
	/**
	 * 设置：排序
	 */
	public void setOrderNum(Long orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 获取：排序
	 */
	public Long getOrderNum() {
		return orderNum;
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
	 * 设置：最近修改用户
	 */
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	/**
	 * 获取：最近修改用户
	 */
	public Long getModifyPerson() {
		return modifyPerson;
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
}

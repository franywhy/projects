package com.school.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 排课计划直播明细子表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-19 14:55:20
 */
public class CourseClassplanLivesEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private String classplanLiveId;
	//排课计划PK
	private String classplanId;
	//直播课程名称
	private String classplanLiveName;
	//直播时间说明
	private String classplanLiveTimeDetail;
	//直播日期
	private Date dayTime;
	//直播开始时间
	private Date startTime;
	//结束时间
	private Date endTime;
	//准备开始时间
	private Date readyTime;
	//关闭时间
	private Date closeTime;
	//时段0.上午;1.下午;2.晚上
	private Integer timeBucket;
	//直播间PK
	private Long liveroomId;
	//直播室PK
	private Long studioId;
	//回放地址
	private String backUrl;
	
	//授课老师
	private Long teacherId;
	//版型权限
	private String liveClassTypeIds;
	//排序
	private Integer orderNum;
	//课程子表直播课PK
	private Long courseLiveDetailId;
	//创建用户
	private Long createPerson;
	//创建时间
	private Date creationTime;
	//最近修改用户
	private Long modifyPerson;
	//最近修改日期
	private Date modifiedTime;
	//平台PK
	private String schoolId;
	//课程PK
	private Long courseId;
	//dr
	private Integer dr;
	//课堂资料（非自适应）URL
	private String fileUrl;
	//课堂资料（非自适应）的资料名
	private String fileName;
	//上期复习资料URL
	private String reviewUrl;
	//上期复习资料文件名
	private String reviewName;
	//本期预习资料URL
	private String prepareUrl;
	//本期预习资料文件名
	private String prepareName;
	//课堂资料（自适应）URL
	private String coursewareUrl;
	//课堂资料（自适应）文件名
	private String coursewareName;
	//课后作业资料URL
	private String homeworkUrl;
	//课后作业资料文件名
	private String homeworkName;
	//记录mongoDB中表_id字段
	private String mId;
	//回放ID
	private String backId;
	//回放类型0.CC 1.展视互动
	private Integer backType;
	
	
	public Integer getBackType() {
		return backType;
	}
	public void setBackType(Integer backType) {
		this.backType = backType;
	}
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	/**
	 * 获取：回放id
	 */
	public String getBackId() {
		return backId;
	}
	/**
	 * 设置：回放id
	 */
	public void setBackId(String backId) {
		this.backId = backId;
	}
	
	/**
	 * 获取：文件上传
	 */
	public String getFileUrl() {
		return fileUrl;
	}
	/**
	 * 设置：文件上传
	 */
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	/**
	 * 获取：dr
	 */
	public Integer getDr() {
		return dr;
	}
	/**
	 * 设置：dr
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：主键
	 */
	public String getClassplanLiveId() {
		return classplanLiveId;
	}
	/**
	 * 设置：主键
	 */
	public void setClassplanLiveId(String classplanLiveId) {
		this.classplanLiveId = classplanLiveId;
	}
	/**
	 * 获取：排课计划PK
	 */
	public String getClassplanId() {
		return classplanId;
	}
	/**
	 * 设置：排课计划PK
	 */
	public void setClassplanId(String classplanId) {
		this.classplanId = classplanId;
	}
	/**
	 * 设置：直播课程名称
	 */
	public void setClassplanLiveName(String classplanLiveName) {
		this.classplanLiveName = classplanLiveName;
	}
	/**
	 * 获取：直播课程名称
	 */
	public String getClassplanLiveName() {
		return classplanLiveName;
	}
	/**
	 * 设置：直播时间说明
	 */
	public void setClassplanLiveTimeDetail(String classplanLiveTimeDetail) {
		this.classplanLiveTimeDetail = classplanLiveTimeDetail;
	}
	/**
	 * 获取：直播时间说明
	 */
	public String getClassplanLiveTimeDetail() {
		return classplanLiveTimeDetail;
	}
	/**
	 * 设置：直播日期
	 */
	public void setDayTime(Date dayTime) {
		this.dayTime = dayTime;
	}
	/**
	 * 获取：直播日期
	 */
	public Date getDayTime() {
		return dayTime;
	}
	/**
	 * 设置：直播开始时间
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：直播开始时间
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * 设置：结束时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：结束时间
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * 设置：时段0.上午;1.下午;2.晚上
	 */
	public void setTimeBucket(Integer timeBucket) {
		this.timeBucket = timeBucket;
	}
	/**
	 * 获取：时段0.上午;1.下午;2.晚上
	 */
	public Integer getTimeBucket() {
		return timeBucket;
	}
	/**
	 * 设置：直播间PK
	 */
	public void setLiveroomId(Long liveroomId) {
		this.liveroomId = liveroomId;
	}
	/**
	 * 获取：直播间PK
	 */
	public Long getLiveroomId() {
		return liveroomId;
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
	 * 设置：回放地址
	 */
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
	/**
	 * 获取：回放地址
	 */
	public String getBackUrl() {
		return backUrl;
	}
	/**
	 * 设置：授课老师
	 */
	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}
	/**
	 * 获取：授课老师
	 */
	public Long getTeacherId() {
		return teacherId;
	}
	/**
	 * 设置：版型权限
	 */
	public void setLiveClassTypeIds(String liveClassTypeIds) {
		this.liveClassTypeIds = liveClassTypeIds;
	}
	/**
	 * 获取：版型权限
	 */
	public String getLiveClassTypeIds() {
		return liveClassTypeIds;
	}
	/**
	 * 设置：排序
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 获取：排序
	 */
	public Integer getOrderNum() {
		return orderNum;
	}
	/**
	 * 设置：课程子表直播课PK
	 */
	public void setCourseLiveDetailId(Long courseLiveDetailId) {
		this.courseLiveDetailId = courseLiveDetailId;
	}
	/**
	 * 获取：课程子表直播课PK
	 */
	public Long getCourseLiveDetailId() {
		return courseLiveDetailId;
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
	
	public Date getReadyTime() {
		return readyTime;
	}
	public void setReadyTime(Date readyTime) {
		this.readyTime = readyTime;
	}
	public Date getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getReviewUrl() {
		return reviewUrl;
	}

	public void setReviewUrl(String reviewUrl) {
		this.reviewUrl = reviewUrl;
	}

	public String getReviewName() {
		return reviewName;
	}

	public void setReviewName(String reviewName) {
		this.reviewName = reviewName;
	}

	public String getPrepareUrl() {
		return prepareUrl;
	}

	public void setPrepareUrl(String prepareUrl) {
		this.prepareUrl = prepareUrl;
	}

	public String getPrepareName() {
		return prepareName;
	}

	public void setPrepareName(String prepareName) {
		this.prepareName = prepareName;
	}

	public String getCoursewareUrl() {
		return coursewareUrl;
	}

	public void setCoursewareUrl(String coursewareUrl) {
		this.coursewareUrl = coursewareUrl;
	}

	public String getCoursewareName() {
		return coursewareName;
	}

	public void setCoursewareName(String coursewareName) {
		this.coursewareName = coursewareName;
	}

	public String getHomeworkUrl() {
		return homeworkUrl;
	}

	public void setHomeworkUrl(String homeworkUrl) {
		this.homeworkUrl = homeworkUrl;
	}

	public String getHomeworkName() {
		return homeworkName;
	}

	public void setHomeworkName(String homeworkName) {
		this.homeworkName = homeworkName;
	}

	@Override
	public String toString() {
		return "CourseClassplanLivesEntity [classplanLiveId=" + classplanLiveId + ", classplanId=" + classplanId
				+ ", classplanLiveName=" + classplanLiveName + ", classplanLiveTimeDetail=" + classplanLiveTimeDetail
				+ ", dayTime=" + dayTime + ", startTime=" + startTime + ", endTime=" + endTime + ", timeBucket="
				+ timeBucket + ", liveroomId=" + liveroomId + ", studioId=" + studioId + ", backUrl=" + backUrl
				+ ", teacherId=" + teacherId + ", liveClassTypeIds=" + liveClassTypeIds + ", orderNum=" + orderNum
				+ ", courseLiveDetailId=" + courseLiveDetailId + ", createPerson=" + createPerson + ", creationTime="
				+ creationTime + ", modifyPerson=" + modifyPerson + ", modifiedTime=" + modifiedTime + ", schoolId="
				+ schoolId + ", courseId=" + courseId + ", dr=" + dr + ", fileUrl=" + fileUrl +", fileName=" + fileName
				+", reviewUrl=" + reviewUrl +", reviewName=" + reviewName +", prepareUrl=" + prepareUrl +", prepareName=" + prepareName
				+ ", coursewareUrl=" + coursewareUrl + ", coursewareName=" + coursewareName + ", homeworkUrl=" + homeworkUrl+ ", homeworkName=" + homeworkName
				+ ", mId=" + mId + ", backId=" + backId + ", backType=" + backType + "]";
	}
	
}

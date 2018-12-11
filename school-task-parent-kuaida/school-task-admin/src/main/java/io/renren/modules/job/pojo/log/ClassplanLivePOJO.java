package io.renren.modules.job.pojo.log;



import io.renren.modules.job.entity.CourseClassplanLivesEntity;
import io.renren.modules.job.utils.BackUtils;

import java.io.Serializable;
import java.util.Date;

public class ClassplanLivePOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private String classplanLiveId;
	//排课计划PK
	private String classplanId;
	//直播课名称
	private String classplanLiveName;
	//直播时间说明
	private String classplanLiveTimeDetail;
	//直播日期
	private Date dayTime;
	//直播开始时间
	private Date startTime;
	//结束时间
	private Date endTime;
	//时段0.上午;1.下午;2.晚上
	private Integer timeBucket;
	//直播间PK
	private Long liveroomId;
	//直播室PK
	private Long studioId;
	//回放地址
	private String backUrl;
	//回放id
	private String backId;
	//回放类型
	private Integer backType;
	//授课老师
	private Long teacherId;
	//班型权限
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
	//文件上传
	private String fileUrl;
		
	//直播间名称
	private String liveRoomName;
	//直播间频道号
	private String liveRoomChannelId;
	//直播间频道密码
	private String liveRoomChannelPassword;
	//直播室名称
	private String studioName;
	//授课老师名称
	private String teacherName;
	//展示互动直播id
	private String genseeLiveId;
	//展示互动直播房间号
	private String genseeLiveNum;
	//排课计划名称
	private String classplanName;
	//课程名称
	private String courseName;
	//即将开始时间
	private Date readyTime;
	//进入直播结束时间
	private Date closeTime;
	//上期复习
	private String review;
	//本期预习
	private String prepare;
	//课堂资料
	private String courseware;
	//练习阶段ID
	private String practiceStageId;
	//考试阶段ID
	private String examStageId;
	//考试阶段名称
	private String phaseName;
	//考勤
	private int attendance;

	public int getAttendance() {
		return attendance;
	}

	public void setAttendance(int attendance) {
		this.attendance = attendance;
	}

	public String getClassplanName() {
		return classplanName;
	}
	public void setClassplanName(String classplanName) {
		this.classplanName = classplanName;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	/**
	 * 获取：展示互动直播id
	 */
	public String getGenseeLiveId() {
		return genseeLiveId;
	}
	/**
	 * 设置：展示互动直播id
	 */
	public void setGenseeLiveId(String genseeLiveId) {
		this.genseeLiveId = genseeLiveId;
	}
	/**
	 * 获取：展示互动直播房间号
	 */
	public String getGenseeLiveNum() {
		return genseeLiveNum;
	}
	/**
	 * 设置：展示互动直播房间号
	 */
	public void setGenseeLiveNum(String genseeLiveNum) {
		this.genseeLiveNum = genseeLiveNum;
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
	 * 获取：回放类型
	 */
	public Integer getBackType() {
		return backType;
	}
	/**
	 * 设置：回放类型
	 */
	public void setBackType(Integer backType) {
		this.backType = backType;
	}
	/**
	 * 获取：直播间名称
	 */
	public String getLiveRoomName() {
		return liveRoomName;
	}
	/**
	 * 设置：直播间名称
	 */
	public void setLiveRoomName(String liveRoomName) {
		this.liveRoomName = liveRoomName;
	}
	/**
	 * 获取：直播间频道号
	 */
	public String getLiveRoomChannelId() {
		return liveRoomChannelId;
	}
	/**
	 * 设置：直播间频道号
	 */
	public void setLiveRoomChannelId(String liveRoomChannelId) {
		this.liveRoomChannelId = liveRoomChannelId;
	}
	/**
	 * 获取：直播间频道密码
	 */
	public String getLiveRoomChannelPassword() {
		return liveRoomChannelPassword;
	}
	/**
	 * 设置：直播间频道密码
	 */
	public void setLiveRoomChannelPassword(String liveRoomChannelPassword) {
		this.liveRoomChannelPassword = liveRoomChannelPassword;
	}
	/**
	 * 获取：直播室名称
	 */
	public String getStudioName() {
		return studioName;
	}
	/**
	 * 设置：直播室名称
	 */
	public void setStudioName(String studioName) {
		this.studioName = studioName;
	}
	/**
	 * 获取：授课老师名称
	 */
	public String getTeacherName() {
		return teacherName;
	}
	/**
	 * 设置：授课老师名称
	 */
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
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
	/**
	 * 获取：即将开始时间
	 */
	public Date getReadyTime() {
		return readyTime;
	}
	/**
	 * 设置：即将开始时间
	 */
	public void setReadyTime(Date readyTime) {
		this.readyTime = readyTime;
	}	
	/**
	 * 获取：进入直播间结束时间
	 */
	public Date getCloseTime() {
		return closeTime;
	}
	/**
	 * 设置：进入直播间结束时间
	 */
	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}
	
		
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	public String getPrepare() {
		return prepare;
	}
	public void setPrepare(String prepare) {
		this.prepare = prepare;
	}
	public String getCourseware() {
		return courseware;
	}
	public void setCourseware(String courseware) {
		this.courseware = courseware;
	}
	public String getPracticeStageId() {
		return practiceStageId;
	}
	public void setPracticeStageId(String practiceStageId) {
		this.practiceStageId = practiceStageId;
	}
	public String getExamStageId() {
		return examStageId;
	}
	public void setExamStageId(String examStageId) {
		this.examStageId = examStageId;
	}
	
	public String getPhaseName() {
		return phaseName;
	}
	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}
	
	
	public static CourseClassplanLivesEntity getEntity(ClassplanLivePOJO pojo){
		CourseClassplanLivesEntity en = new CourseClassplanLivesEntity();
		if(pojo != null){
			System.out.println(pojo.toString());
			en.setClassplanLiveId(pojo.getClassplanLiveId());
			en.setClassplanId(pojo.getClassplanId());
			en.setClassplanLiveName(pojo.getClassplanLiveName());
			en.setClassplanLiveTimeDetail(pojo.getClassplanLiveTimeDetail());
			en.setDayTime(pojo.getDayTime());
			en.setStartTime(pojo.getStartTime());
			en.setEndTime(pojo.getEndTime());
			en.setTimeBucket(pojo.getTimeBucket());
			en.setLiveroomId(pojo.getLiveroomId());
			en.setStudioId(pojo.getStudioId());
			en.setBackUrl(pojo.getBackUrl());
			BackUtils.setBackId(en);
//			en.setBackId(pojo.getBackId());
//			en.setBackType(pojo.getBackType());
			en.setTeacherId(pojo.getTeacherId());
			en.setLiveClassTypeIds(pojo.getLiveClassTypeIds());
			en.setOrderNum(pojo.getOrderNum());
			en.setCourseLiveDetailId(pojo.getCourseLiveDetailId());
			en.setCreatePerson(pojo.getCreatePerson());
			en.setCreationTime(pojo.getCreationTime());
			en.setModifyPerson(pojo.getModifyPerson());
			en.setModifiedTime(pojo.getModifiedTime());
			en.setSchoolId(pojo.getSchoolId());
			en.setCourseId(pojo.getCourseId());
			en.setDr(pojo.getDr());
			en.setFileUrl(pojo.getFileUrl());
			en.setReadyTime(pojo.getReadyTime());
			en.setCloseTime(pojo.getCloseTime());
			en.setReview(pojo.getReview());
			en.setPrepare(pojo.getPrepare());
			en.setCourseware(pojo.getCourseware());
			en.setPracticeStageId(pojo.getPracticeStageId());
			en.setExamStageId(pojo.getExamStageId());
			en.setPhaseName(pojo.getPhaseName());
			en.setAttendance(pojo.getAttendance());
		}	
		return en;
	}
		
}

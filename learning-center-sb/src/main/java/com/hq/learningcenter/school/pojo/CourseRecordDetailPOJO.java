package com.hq.learningcenter.school.pojo;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

public class CourseRecordDetailPOJO {
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
	//播放时长(单位:毫秒)
	private Long durationS;
	//时长
	private String duration;
	//首截图
	private String firstImage;
	//排序
	private Long orderNum;
	
	private String ccId;
	
	//授课老师
	private String teacherName;
	//唯一识别Id
	private String uniqueId;
	//给前端/移动端显示时长(格式：xx分钟)
	private String timeMin;
	//单节录播视屏播放进度
	private String playSchedule;
	//单节录播视屏播放时长
	private Long playDuration;
	
	//param1
	private Long param1;
	//param2
	private String param2;
	//param3
	private String param3;
	//param4
	private String param4;
	//param5
	private String param5;
	//学员观看进度
	private BigDecimal attentPer; 
	  
	public BigDecimal getAttentPer() {
		return attentPer;
	}

	public void setAttentPer(BigDecimal attentPer) {
		this.attentPer = attentPer;
	}

	List<CourseRecordDetailPOJO> list;

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getPolyvName() {
		return polyvName;
	}

	public void setPolyvName(String polyvName) {
		this.polyvName = polyvName;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getFirstImage() {
		return firstImage;
	}

	public void setFirstImage(String firstImage) {
		this.firstImage = firstImage;
	}

	public Long getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Long orderNum) {
		this.orderNum = orderNum;
	}

	public List<CourseRecordDetailPOJO> getList() {
		return list;
	}

	public void setList(List<CourseRecordDetailPOJO> list) {
		this.list = list;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public Long getParam1() {
		return param1;
	} 

	public void setParam1(Long param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getParam3() {
		return param3 = "";
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}

	public String getParam4() {
		return param4 = "";
	}

	public void setParam4(String param4) {
		this.param4 = param4;
	}

	public String getParam5() {
		return param5 = "";
	}

	public void setParam5(String param5) {
		this.param5 = param5;
	}

	public String getUniqueId() {
		return uniqueId = UUID.randomUUID().toString();
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getTimeMin() {
		if(StringUtils.isNotBlank(duration)){
			String[] arr = getDuration().split(":");
			if(null !=arr && arr.length == 3){
				return (Integer.parseInt(arr[0]) * 60 + Integer.parseInt(arr[1]))+ "分钟";
			}
		}
		return "0分钟";
	}

	public void setTimeMin(String timeMin) {
		this.timeMin = timeMin;
	}

	public Long getDurationS() {
		return durationS;
	}

	public void setDurationS(Long durationS) {
		this.durationS = durationS;
	}

	public String getPlaySchedule() {
		return "";
	}

	public void setPlaySchedule(String playSchedule) {
		this.playSchedule = playSchedule;
	}

	public Long getPlayDuration() {
		return 0L;
	}

	public void setPlayDuration(Long playDuration) {
		this.playDuration = playDuration;
	}

	public String getCcId() {
		return ccId;
	}

	public void setCcId(String ccId) {
		this.ccId = ccId;
	}
}

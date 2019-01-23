package com.hq.bi.offline.task.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class LogStudentAttendEntity {

	private String id;

	private Long userPlanId;
	
	private Long classTypeId;

	private String classplanLiveId;
	
	private Long userId;

	private Long areaId;

	private Long deptId;

	private Long classId;

	private Long classTeacherId;

	private String mobile;

	private Date startClassTime;

	private String areaName;

	private String deptName;

	private String className;

	private String userName;

	private String teacherName;
	
	private BigDecimal livePer;
	
	private BigDecimal attendPer;
	
	private BigDecimal minWatchDur;

	private BigDecimal minFullDur;
	
	private String classplanLiveName;

	private BigDecimal compliancePer;

	private Date creationTime;

	private Date modifiedTime;

}

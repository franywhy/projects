package com.hq.bi.search.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class LogStudentAttendEntity {

	private Long userPlanId;

	private Long userId;

	private String mobile;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date startClassTime;

	private String areaName;

	private String deptName;

	private String className;

	private String userName;

	private String teacherName;
	
	private String classplanLiveName;

	private String userStatus;

	private String minWatchDurTxt;

	private String minFullDurTxt;

	private String livePerTxt;

	private String attendPerTxt;

	private String compliancePerTxt;

	private BigDecimal livePer;

	private BigDecimal attendPer;

	private BigDecimal minWatchDur;

	private BigDecimal minFullDur;

	private BigDecimal compliancePer;
}

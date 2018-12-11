package io.renren.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 排课计划表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-06 10:45:58
 */
public class CourseClassplanChangeRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long changeId;
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
	//审核状态
	private Integer isAudited;
	//是否公开课 0：是   1：否
	private Integer isOpen;
	//排课是否正常0：作废 1：正常 2：结课
	//资料库ID
	private Long materialId;
	//资料库名称
	private String materialName;
	//同步数据时间戳
	private Date ts;
	//产品id
	private Long productId;

	//审核用户id
	private Long auditor;
	//审核日期
	private Date auditTime;
	//版本号，默认为1'
	private Integer versionNo;
	//状态0：未超过开课时间申请 1：超过开课时间申请
	private Integer recordType;
    //审核用户id
    private String auditorName;
    //产品线
    private String productName;
    //上课时点
    private String timetableName;
    //直播室
    private String studioName;
    //直播间
    private String liveRoomName;
    //授课老师
    private String teacherName;
    //最近修改用户
    private  String modifiedName;
    //直播课数量
    private Integer liveNumber;
    //学生数量
    private Integer studentNumber;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTimetableName() {
        return timetableName;
    }

    public void setTimetableName(String timetableName) {
        this.timetableName = timetableName;
    }

    public String getStudioName() {
        return studioName;
    }

    public void setStudioName(String studioName) {
        this.studioName = studioName;
    }

    public String getLiveRoomName() {
        return liveRoomName;
    }

    public void setLiveRoomName(String liveRoomName) {
        this.liveRoomName = liveRoomName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getModifiedName() {
        return modifiedName;
    }

    public void setModifiedName(String modifiedName) {
        this.modifiedName = modifiedName;
    }

    public Integer getLiveNumber() {
        return liveNumber;
    }

    public void setLiveNumber(Integer liveNumber) {
        this.liveNumber = liveNumber;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public Long getChangeId() {
        return changeId;
    }

    public void setChangeId(Long changeId) {
        this.changeId = changeId;
    }

    public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
		public Long getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
		//课程名称
		private String courseName;
	
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getClassplanId() {
		return classplanId;
	}
	public void setClassplanId(String classplanId) {
		this.classplanId = classplanId;
	}
	/**
	 * 
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
	 * 设置：状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：审核状态
	 */
	public void setIsAudited(Integer isAudited) {
		this.isAudited = isAudited;
	}
	/**
	 * 获取：审核状态
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

	public Long getAuditor() {
		return auditor;
	}

	public void setAuditor(Long auditor) {
		this.auditor = auditor;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	public Integer getRecordType() {
		return recordType;
	}

	public void setRecordType(Integer recordType) {
		this.recordType = recordType;
	}

	@Override
	public String toString() {
		return "CourseClassplanChangeRecordEntity{" +
				"classplanId='" + classplanId + '\'' +
				", classplanName='" + classplanName + '\'' +
				", courseId=" + courseId +
				", classplanLiveDetail='" + classplanLiveDetail + '\'' +
				", remark='" + remark + '\'' +
				", startTime=" + startTime +
				", teacherId=" + teacherId +
				", timetableId=" + timetableId +
				", liveRoomId=" + liveRoomId +
				", studioId=" + studioId +
				", creator=" + creator +
				", creationTime=" + creationTime +
				", modifier=" + modifier +
				", modifiedTime=" + modifiedTime +
				", schoolId='" + schoolId + '\'' +
				", dr=" + dr +
				", status=" + status +
				", isAudited=" + isAudited +
				", isOpen=" + isOpen +
				", materialId=" + materialId +
				", materialName='" + materialName + '\'' +
				", ts=" + ts +
				", productId=" + productId +
				", auditor=" + auditor +
				", auditTime=" + auditTime +
				", versionNo=" + versionNo +
				", recordType=" + recordType +
				", courseName='" + courseName + '\'' +
				'}';
	}
}

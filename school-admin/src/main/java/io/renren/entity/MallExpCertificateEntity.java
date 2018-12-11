package io.renren.entity;

import java.io.Serializable;
import java.util.Date;

import io.renren.utils.DateUtils;
import io.renren.utils.NumberUtils;



/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-11-04 10:20:59
 */
public class MallExpCertificateEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//记录ＩＤ
	private Integer id;
	//姓名
	private String name;
	//身份证号
	private String card;
	//报读日期
	private Date readDate;
	//结课日期
	private Date endDate;
	//报读课程
	private String course;
	//经验等级
	private Integer exp;
	//证书编号
	private String certNo;
	//颁发日期
	private Date sendDate;
	//所属校区
	private Integer school;
	//发放状态 0:未发放 1:发放中 2:已发放
	private Integer sendStatus;
	//主要课程
	private String courseRemark;
	//状态:0.禁用;1.启用
	private Integer status;
	//平台ID
	private String schoolId;
	//创建用户
	private Long createPerson;
	//创建时间
	private Date creationTime;
	//最近修改用户
	private Long modifyPerson;
	//最近修改日期
	private Date modifiedTime;
	//手机关联的用户id
    private Long userId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
	 * 设置：记录ＩＤ
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：记录ＩＤ
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：姓名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：姓名
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：身份证号
	 */
	public void setCard(String card) {
		this.card = card;
	}
	/**
	 * 获取：身份证号
	 */
	public String getCard() {
		return card;
	}
	/**
	 * 设置：报读日期
	 */
	public void setReadDate(Date readDate) {
		this.readDate = readDate;
	}
	/**
	 * 获取：报读日期
	 */
	public Date getReadDate() {
		return readDate;
	}
	/**
	 * 设置：结课日期
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * 获取：结课日期
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * 设置：报读课程
	 */
	public void setCourse(String course) {
		this.course = course;
	}
	/**
	 * 获取：报读课程
	 */
	public String getCourse() {
		return course;
	}
	/**
	 * 设置：经验等级
	 */
	public void setExp(Integer exp) {
		this.exp = exp;
	}
	/**
	 * 获取：经验等级
	 */
	public Integer getExp() {
		return exp;
	}
	/**
	 * 设置：证书编号
	 */
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	/**
	 * 获取：证书编号
	 */
	public String getCertNo() {
		return certNo;
	}
	/**
	 * 设置：颁发日期
	 */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	/**
	 * 获取：颁发日期
	 */
	public Date getSendDate() {
		return sendDate;
	}
	/**
	 * 设置：所属校区
	 */
	public void setSchool(Integer school) {
		this.school = school;
	}
	/**
	 * 获取：所属校区
	 */
	public Integer getSchool() {
		return school;
	}
	/**
	 * 设置：发放状态 0:未发放 1:发放中 2:已发放
	 */
	public void setSendStatus(Integer sendStatus) {
		this.sendStatus = sendStatus;
	}
	/**
	 * 获取：发放状态 0:未发放 1:发放中 2:已发放
	 */
	public Integer getSendStatus() {
		return sendStatus;
	}
	/**
	 * 设置：主要课程
	 */
	public void setCourseRemark(String courseRemark) {
		this.courseRemark = courseRemark;
	}
	/**
	 * 获取：主要课程
	 */
	public String getCourseRemark() {
		return courseRemark;
	}
	/**
	 * 设置：状态:0.禁用;1.启用
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态:0.禁用;1.启用
	 */
	public Integer getStatus() {
		return status;
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
	
	private String schoolName;

	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	
	private String mobile;

	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	private String readDateStr;
	private String endDateStr;
	private String expStr;

	public String getReadDateStr() {
		return DateUtils.format(readDate, "yyyy年MM月");
	}
	public String getEndDateStr() {
		return DateUtils.format(endDate, "yyyy年MM月");
	}
	public void setReadDateStr(String readDateStr) {
		this.readDateStr = DateUtils.format(readDate, "yyyy年MM月");
	}
	public void setEndDateStr(String endDateStr) {
		this.endDateStr = DateUtils.format(endDate, "yyyy年MM月");
	}
	public String getExpStr() {		
		//return NumberUtils.numberArab2CN(exp)+"级";
		return expStr;
		
	}
	private Integer type;

	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}

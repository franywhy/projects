package io.renren.pojo;

import io.renren.enums.AuditStatusEnum;
import io.renren.enums.CourseAbormalTypeEnum;
import io.renren.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author linchaokai
 * @Description
 * @date 2018/3/19 9:47
 */
public class CourseAbnormalOrderPOJO  implements Serializable {
    private static final long serialVersionUID = 1L;

    //pk
    private Long id;
    //学员pk
    private Long studentId;
    //异常类型(0-正常 1-休学 2-失联)
    private Integer abnormalType;
    //订单pk
    private Long orderId;
    //预计开始时间
    private Date startTime;
    //预计结束时间
    private Date expectEndTime;
    //实际结束时间
    private Date endTime;
    //状态(0-审核中 1-取消 2-通过)
    private Integer auditStatus;
    //异常原因
    private String abnormalReason;
    //备注
    private String remark;
    //创建时间
    private Date createTime;
    //申请用户pk
    private Long createPerson;
    //审核用户
    private Long modifyPerson;
    //审核时间
    private Date modifiedTime;
    //修改用户
    private Long updatePerson;
    //修改时间
    private Date updateTime;
    //单号
    private String orderno;

    //学员姓名
    private String nickName;
    //学员号码
    private String mobile;

    //异常类型(0-正常 1-休学 2-失联)
    private String abnormalTypeStr;

    //预计开始时间
    private String startTimeStr;

    //预计结束时间
    private String expectEndTimeStr;

    //实际结束时间
    private String endTimeStr;

    //状态(0-审核中 1-取消 2-通过)
    private String auditStatusStr;

    //订单编号
    private String mallOrderNo;

    //班主任
    private String teacherName;

    //班级名称
    private String className;

    //专业
    private String professionName;

    //省份
    private String areaName;

    //班型
    private String classtypeName;

    //层次
    private String levelName;

    //提交时间
    private String createTimeStr;
    //报名时间
    private Date creationTime;

    //报名时间显示
    private String creationTimeStr;

    //报名时间
    private Date dateValidity;

    //学籍有效期
    private String dateValidityStr;
    /**
     * 设置：pk
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * 获取：pk
     */
    public Long getId() {
        return id;
    }
    /**
     * 设置：学员pk
     */
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    /**
     * 获取：学员pk
     */
    public Long getStudentId() {
        return studentId;
    }
    /**
     * 设置：异常类型(0-正常 1-休学 2-失联)
     */
    public void setAbnormalType(Integer abnormalType) {
        this.abnormalType = abnormalType;
    }
    /**
     * 获取：异常类型(0-正常 1-休学 2-失联)
     */
    public Integer getAbnormalType() {
        return abnormalType;
    }
    /**
     * 设置：订单pk
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    /**
     * 获取：订单pk
     */
    public Long getOrderId() {
        return orderId;
    }
    /**
     * 设置：预计开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    /**
     * 获取：预计开始时间
     */
    public Date getStartTime() {
        return startTime;
    }
    /**
     * 设置：预计结束时间
     */
    public void setExpectEndTime(Date expectEndTime) {
        this.expectEndTime = expectEndTime;
    }
    /**
     * 获取：预计结束时间
     */
    public Date getExpectEndTime() {
        return expectEndTime;
    }
    /**
     * 设置：实际结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    /**
     * 获取：实际结束时间
     */
    public Date getEndTime() {
        return endTime;
    }
    /**
     * 设置：状态(0-审核中 1-取消 2-通过)
     */
    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }
    /**
     * 获取：状态(0-审核中 1-取消 2-通过)
     */
    public Integer getAuditStatus() {
        return auditStatus;
    }
    /**
     * 设置：异常原因
     */
    public void setAbnormalReason(String abnormalReason) {
        this.abnormalReason = abnormalReason;
    }
    /**
     * 获取：异常原因
     */
    public String getAbnormalReason() {
        return abnormalReason;
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
     * 设置：申请用户pk
     */
    public void setCreatePerson(Long createPerson) {
        this.createPerson = createPerson;
    }
    /**
     * 获取：申请用户pk
     */
    public Long getCreatePerson() {
        return createPerson;
    }
    /**
     * 设置：审核用户
     */
    public void setModifyPerson(Long modifyPerson) {
        this.modifyPerson = modifyPerson;
    }
    /**
     * 获取：审核用户
     */
    public Long getModifyPerson() {
        return modifyPerson;
    }
    /**
     * 设置：审核时间
     */
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
    /**
     * 获取：审核时间
     */
    public Date getModifiedTime() {
        return modifiedTime;
    }
    /**
     * 设置：修改用户
     */
    public void setUpdatePerson(Long updatePerson) {
        this.updatePerson = updatePerson;
    }
    /**
     * 获取：修改用户
     */
    public Long getUpdatePerson() {
        return updatePerson;
    }
    /**
     * 设置：修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    /**
     * 获取：修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }
    /**
     * 设置：
     */
    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }
    /**
     * 获取：
     */
    public String getOrderno() {
        return orderno;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAbnormalTypeStr() {
        return CourseAbormalTypeEnum.getText(abnormalType);
    }

    public void setAbnormalTypeStr(String abnormalTypeStr) {
        this.abnormalTypeStr = abnormalTypeStr;
    }

    public String getStartTimeStr() {
        return DateUtils.format(startTime);
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getExpectEndTimeStr() {
        return DateUtils.format(expectEndTime);
    }

    public void setExpectEndTimeStr(String expectEndTimeStr) {
        this.expectEndTimeStr = expectEndTimeStr;
    }

    public String getEndTimeStr() {
        if(endTime != null){
            endTimeStr = DateUtils.format(endTime);
        }
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getAuditStatusStr() {
        return AuditStatusEnum.getText(auditStatus);
    }

    public void setAuditStatusStr(String auditStatusStr) {
        this.auditStatusStr = auditStatusStr;
    }

    public String getMallOrderNo() {
        return mallOrderNo;
    }

    public void setMallOrderNo(String mallOrderNo) {
        this.mallOrderNo = mallOrderNo;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getProfessionName() {
        return professionName;
    }

    public void setProfessionName(String professionName) {
        this.professionName = professionName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getClasstypeName() {
        return classtypeName;
    }

    public void setClasstypeName(String classtypeName) {
        this.classtypeName = classtypeName;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getCreateTimeStr() {
        return DateUtils.format(createTime);
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getCreationTimeStr() {
        return DateUtils.format(creationTime);
    }

    public void setCreationTimeStr(String creationTimeStr) {
        this.creationTimeStr = creationTimeStr;
    }

    public Date getDateValidity() {
        return dateValidity;
    }

    public void setDateValidity(Date dateValidity) {
        this.dateValidity = dateValidity;
    }

    public String getDateValidityStr() {
        return DateUtils.format(dateValidity);
    }

    public void setDateValidityStr(String dateValidityStr) {
        this.dateValidityStr = dateValidityStr;
    }
}

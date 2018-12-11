package io.renren.pojo.classplan;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.renren.entity.CourseClassplanEntity;

public class ClassplanPOJO implements Serializable {
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
    //审核状态
    private Integer isAudited;
    //是否公开课 0：是   1：否
    private Integer isOpen;

    //课程名称
    private String courseName;
    //授课老师名字
    private String teacherName;
    //上课时点名称
    private String timetableName;
    //直播室名称
    private String studioName;
    //直播间名称
    private String liveRoomName;
    //直播间名称
    private String liveRoomChannelId;
    //直播间名称
    private String liveRoomChannelPassword;
    //创建用户
    private String creationName;
    //最近修改用户
    private String modifiedName;
    //排课是否正常
    private String statusName;
    //资料库名称
    private String materialName;
    //资料库ID
    private Long materialId;
    private String materialIds;
    //直播课数量
    private Integer liveNumber;
    //学生数量
    private Integer studentNumber;
    //当前时间戳
    private Date ts;
    //产品id
    private Long productId;
    //产品名称
    private String productName;

    /**
     * 校验 true校验  false不校验
     */
    private Boolean checkType;

    public Boolean getCheckType() {
        return checkType;
    }
    public void setCheckType(Boolean checkType) {
        this.checkType = checkType;
    }
    /**
     * 获取：直播课数量
     */
    public Integer getLiveNumber() {
        return liveNumber;
    }
    /**
     * 设置：直播课数量
     */
    public void setLiveNumber(Integer liveNumber) {
        this.liveNumber = liveNumber;
    }
    /**
     * 获取：学生数量
     */
    public Integer getStudentNumber() {
        return studentNumber;
    }
    /**
     * 设置：学生数量
     */
    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }
    public Long getMaterialId() {
        return materialId;
    }
    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }
    //子表集合
    private List<ClassplanLivePOJO> detailList;


    public List<ClassplanLivePOJO> getDetailList() {
        return detailList;
    }
    public void setDetailList(List<ClassplanLivePOJO> detailList) {
        this.detailList = detailList;
    }

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
    public String getClassplanName() {
        return classplanName;
    }
    public void setClassplanName(String classplanName) {
        this.classplanName = classplanName;
    }
    public Long getCourseId() {
        return courseId;
    }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    public String getClassplanLiveDetail() {
        return classplanLiveDetail;
    }
    public void setClassplanLiveDetail(String classplanLiveDetail) {
        this.classplanLiveDetail = classplanLiveDetail;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Long getTeacherId() {
        return teacherId;
    }
    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
    public Long getTimetableId() {
        return timetableId;
    }
    public void setTimetableId(Long timetableId) {
        this.timetableId = timetableId;
    }
    public Long getLiveRoomId() {
        return liveRoomId;
    }
    public void setLiveRoomId(Long liveRoomId) {
        this.liveRoomId = liveRoomId;
    }
    public Long getStudioId() {
        return studioId;
    }
    public void setStudioId(Long studioId) {
        this.studioId = studioId;
    }
    public Long getCreator() {
        return creator;
    }
    public void setCreator(Long creator) {
        this.creator = creator;
    }
    public Date getCreationTime() {
        return creationTime;
    }
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
    public Long getModifier() {
        return modifier;
    }
    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }
    public Date getModifiedTime() {
        return modifiedTime;
    }
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
    public String getSchoolId() {
        return schoolId;
    }
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }
    public Integer getDr() {
        return dr;
    }
    public void setDr(Integer dr) {
        this.dr = dr;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getIsAudited() {
        return isAudited;
    }
    public void setIsAudited(Integer isAudited) {
        this.isAudited = isAudited;
    }
    public Integer getIsOpen() {
        return isOpen;
    }
    public void setIsOpen(Integer isOpen) {
        this.isOpen = isOpen;
    }
    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public String getTeacherName() {
        return teacherName;
    }
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
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
    public String getCreationName() {
        return creationName;
    }
    public void setCreationName(String creationName) {
        this.creationName = creationName;
    }
    public String getModifiedName() {
        return modifiedName;
    }
    public void setModifiedName(String modifiedName) {
        this.modifiedName = modifiedName;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getLiveRoomChannelId() {
        return liveRoomChannelId;
    }
    public void setLiveRoomChannelId(String liveRoomChannelId) {
        this.liveRoomChannelId = liveRoomChannelId;
    }
    public String getLiveRoomChannelPassword() {
        return liveRoomChannelPassword;
    }
    public void setLiveRoomChannelPassword(String liveRoomChannelPassword) {
        this.liveRoomChannelPassword = liveRoomChannelPassword;
    }

    public Date getTs() {
        return ts;
    }
    public void setTs(Date ts) {
        this.ts = ts;
    }
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMaterialIds() {
        return materialIds;
    }
    public void setMaterialIds(String materialIds) {
        this.materialIds = materialIds;
    }
    @Override
    public String toString() {
        return "ClassplanPOJO [classplanId=" + classplanId + ", classplanName=" + classplanName + ", courseId="
                + courseId + ", classplanLiveDetail=" + classplanLiveDetail + ", remark=" + remark + ", startTime="
                + startTime + ", teacherId=" + teacherId + ", timetableId=" + timetableId + ", liveRoomId="
                + liveRoomId + ", studioId=" + studioId + ", creator=" + creator + ", creationTime=" + creationTime
                + ", modifier=" + modifier + ", modifiedTime=" + modifiedTime + ", schoolId=" + schoolId + ", dr="
                + dr + ", status=" + status + ", isAudited=" + isAudited + ", isOpen=" + isOpen + ", courseName="
                + courseName + ", teacherName=" + teacherName + ", timetableName=" + timetableName + ", studioName="
                + studioName + ", liveRoomName=" + liveRoomName + ", liveRoomChannelId=" + liveRoomChannelId
                + ", liveRoomChannelPassword=" + liveRoomChannelPassword + ", creationName=" + creationName
                + ", modifiedName=" + modifiedName + ", statusName=" + statusName + ", materialName=" + materialName
                + ", materialIds=" + materialIds + ", materialId=" + materialId + ", liveNumber=" + liveNumber + ", studentNumber=" + studentNumber
                + ", ts=" + ts + ", productId=" + productId + ", productName=" + productName + ", checkType="
                + checkType + ", detailList=" + detailList + "]";
    }
    public static CourseClassplanEntity getEntity(ClassplanPOJO pojo){
        CourseClassplanEntity en = new CourseClassplanEntity();
        if(pojo != null){
            en.setClassplanId(pojo.getClassplanId());
            en.setClassplanName(pojo.getClassplanName());
            en.setCourseId(pojo.getCourseId());
            en.setClassplanLiveDetail(pojo.getClassplanLiveDetail());
            en.setRemark(pojo.getRemark());
            en.setStartTime(pojo.getStartTime());
            en.setTeacherId(pojo.getTeacherId());
            en.setTimetableId(pojo.getTimetableId());
            en.setLiveRoomId(pojo.getLiveRoomId());
            en.setStudioId(pojo.getStudioId());
            en.setCreator(pojo.getCreator());
            en.setCreationTime(pojo.getCreationTime());
            en.setModifier(pojo.getModifier());
            en.setModifiedTime(pojo.getModifiedTime());
            en.setSchoolId(pojo.getSchoolId());
            en.setDr(pojo.getDr());
            en.setStatus(pojo.getStatus());
            en.setIsAudited(pojo.getIsAudited());
            en.setIsOpen(pojo.getIsOpen());
            en.setMaterialName(pojo.getMaterialName());
            en.setMaterialId(pojo.getMaterialId());
            en.setMaterialIds(pojo.getMaterialIds());
            en.setTs(pojo.getTs());
            en.setProductId(pojo.getProductId());
        }
        return en;
    }

}

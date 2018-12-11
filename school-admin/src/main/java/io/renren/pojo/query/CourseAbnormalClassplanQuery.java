package io.renren.pojo.query;

import io.renren.utils.BaseQuery;

import java.io.Serializable;
import java.util.Date;


/**
 * 弃考免考记录单
 *
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2018-03-17 17:58:51
 */
public class CourseAbnormalClassplanQuery extends BaseQuery {

    //pk
    private Long id;
    private String abnormalClassplanNo;
    //学员pk
    private Long studentId;
    private String studentMobile;
    //异常类型(3.弃考 4.免考)
    private Integer abnormalType;
    //课程PK
    private Long courseId;
    //排课计划PK
    private String classplanId;
    //审核状态(0-审核中 1-取消  2-通过)
    private Integer auditStatus;

    public CourseAbnormalClassplanQuery() {
    }

    public String getAbnormalClassplanNo() {
        return abnormalClassplanNo;
    }

    public void setAbnormalClassplanNo(String abnormalClassplanNo) {
        this.abnormalClassplanNo = abnormalClassplanNo;
    }

    public String getStudentMobile() {
        return studentMobile;
    }

    public void setStudentMobile(String studentMobile) {
        this.studentMobile = studentMobile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    @Override
    public String toString() {
        return "CourseAbnormalClassplanQuery{" +
                "id=" + id +
                ", abnormalClassplanNo='" + abnormalClassplanNo + '\'' +
                ", studentId=" + studentId +
                ", studentMobile='" + studentMobile + '\'' +
                ", abnormalType=" + abnormalType +
                ", courseId=" + courseId +
                ", classplanId='" + classplanId + '\'' +
                ", auditStatus=" + auditStatus +
                '}';
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Integer getAbnormalType() {
        return abnormalType;
    }

    public void setAbnormalType(Integer abnormalType) {
        this.abnormalType = abnormalType;
    }


    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getClassplanId() {
        return classplanId;
    }

    public void setClassplanId(String classplanId) {
        this.classplanId = classplanId;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

}

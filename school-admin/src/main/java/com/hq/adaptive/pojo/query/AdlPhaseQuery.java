package com.hq.adaptive.pojo.query;

/**
 * 评测阶段表
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:37
 */
public class AdlPhaseQuery extends BaseQuery {

    /** 课程ID */
    private Long courseId;
    //重点:0.正常;1.重点
    private Integer keyPoint;

    private Long phaseId;

    private String phaseNo;
    private String phaseName;

    @Override
    public String toString() {
        return "AdlPhaseQuery{" +
                "courseId=" + courseId +
                ", keyPoint=" + keyPoint +
                ", phaseId=" + phaseId +
                ", phaseNo='" + phaseNo + '\'' +
                ", phaseName='" + phaseName + '\'' +
                '}';
    }

    public String getPhaseNo() {
        return phaseNo;
    }

    public void setPhaseNo(String phaseNo) {
        this.phaseNo = phaseNo;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public Long getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Long phaseId) {
        this.phaseId = phaseId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Integer getKeyPoint() {
        return keyPoint;
    }

    public void setKeyPoint(Integer keyPoint) {
        this.keyPoint = keyPoint;
    }
}

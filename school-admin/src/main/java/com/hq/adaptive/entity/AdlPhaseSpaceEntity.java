package com.hq.adaptive.entity;

import java.io.Serializable;

/**
 * 评测阶段对应知识点空间表
 */
public class AdlPhaseSpaceEntity implements Serializable {
    /** 阶段PK */
    private Long phaseId;
    /** 课程PK */
    private Long courseId;
    /** 本级知识点PK */
    private Long selfId;
    /** 包含知识点PK */
    private Long childId;
    /** 主键 */
    private Long spaceId;

    public AdlPhaseSpaceEntity(Long spaceId, Long phaseId, Long courseId, Long selfId, Long childId) {
        this.spaceId = spaceId;
        this.phaseId = phaseId;
        this.courseId = courseId;
        this.selfId = selfId;
        this.childId = childId;
    }

    public AdlPhaseSpaceEntity() {

    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
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

    public Long getSelfId() {
        return selfId;
    }

    public void setSelfId(Long selfId) {
        this.selfId = selfId;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    @Override
    public String toString() {
        return "AdlPhaseSpaceEntity{" +
                "phaseId=" + phaseId +
                ", courseId=" + courseId +
                ", selfId=" + selfId +
                ", childId=" + childId +
                ", spaceId=" + spaceId +
                '}';
    }
}

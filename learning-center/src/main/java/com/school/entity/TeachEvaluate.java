package com.school.entity;

import java.util.Date;

public class TeachEvaluate {
    private Long id;

    private Integer score;

    private String content;

    private Long userId;

    private String topicId;

    private Long topicType;

    private Integer stageCode;

    private Date createTime;

    private Integer status;

    private String fileKey;

    private Integer materialScore;

    private Integer contentScore;

    private Integer teachStyleScore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId == null ? null : topicId.trim();
    }

    public Long getTopicType() {
        return topicType;
    }

    public void setTopicType(Long topicType) {
        this.topicType = topicType;
    }

    public Integer getStageCode() {
        return stageCode;
    }

    public void setStageCode(Integer stageCode) {
        this.stageCode = stageCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey == null ? null : fileKey.trim();
    }

    public Integer getMaterialScore() {
        return materialScore;
    }

    public void setMaterialScore(Integer materialScore) {
        this.materialScore = materialScore;
    }

    public Integer getContentScore() {
        return contentScore;
    }

    public void setContentScore(Integer contentScore) {
        this.contentScore = contentScore;
    }

    public Integer getTeachStyleScore() {
        return teachStyleScore;
    }

    public void setTeachStyleScore(Integer teachStyleScore) {
        this.teachStyleScore = teachStyleScore;
    }
}
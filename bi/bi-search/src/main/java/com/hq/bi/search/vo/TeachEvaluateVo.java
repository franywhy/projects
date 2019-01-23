package com.hq.bi.search.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Created with IntelliJ IDEA.
 * @Description: school-tadmin-parent
 * @User: chengjigang
 * @Date: 2018-12-05
 * @Time: 16:52
 */
@Data
public class TeachEvaluateVo {
    /**
     * ID
     */
    private Long id;

    /**
     * 评分
     */
    private Integer score;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 用户名称
     */
    private String nickName;

    /**
     * 用户手机号
     */
    private String mobile;

    /**
     * 创建日期
     */
    private Date createTime;


    /**
     * 交付件关联fileKey
     */
    private String fileKey;

    /**
     * 课程资料评分
     */
    private Integer materialScore;

    /**
     * 教学内容评分
     */
    private Integer contentScore;

    /**
     * 教学风格评分
     */
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
        this.content = content;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
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

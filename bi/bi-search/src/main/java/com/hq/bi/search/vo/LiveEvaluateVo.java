package com.hq.bi.search.vo;

import lombok.Data;

import java.util.Date;

/**
 * 直播评价数据输出类
 */
@Data
public class LiveEvaluateVo {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 排课id
     */
    private String classplanId;

    /**
     * 排课名称
     */
    private String classplanName;

    /**
     * 直播课次id
     */
    private String topicId;

    /**
     * 直播课次id 2
     */
    private String classplanLiveId;

    /**
     * 课次/章节名称
     */
    private String classplanLiveName;

    /**
     * 评价时间
     */
    private Date createTime;

    /**
     * 老师id
     */
    private String teacherId;

    /**
     * 老师名称 teacherName
     */
    private String nickName;

    /**
     * 老师名称 2
     */
    private String teacherName;

    /**
     * 学员人数
     */
    private Integer userNum;

    /**
     * 评价人数
     */
    private Integer evaluateNum;

    /**
     * 综合评分
     */
    private Double totalScores;

    /**
     * 课程资料评分
     */
    private Double materialScores;

    /**
     *教学内容评分
     */
    private Double contentScores;

    /**
     * 教学风格评分
     */
    private Double teachStyleScores;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 产品线
     */
    private String productId;
}

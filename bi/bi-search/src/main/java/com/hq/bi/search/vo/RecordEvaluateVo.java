package com.hq.bi.search.vo;

import lombok.Data;

import java.util.Date;

/**
 * 录播评价数据输出类
 */
@Data
public class RecordEvaluateVo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 课程id
     */
    private String courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 录播id
     */
    private String topicId;

    /**
     * 录播id 2
     */
    private String recordId;

    /**
     * 录播名称
     */
    private String className;

    /**
     * 录播名称 2
     */
    private String recordName;

    /**
     * 教师id
     */
    private String teacherId;

    /**
     * 教师名称
     */
    private String teacherName;

    /**
     * 教师名称 2
     */
    private String nickName;

    /**
     * 学员人数
     */
    private Integer userNum;

    /**
     * 评价人数
     */
    private Integer evaluateNum;

    /**
     * 最近评价时间
     */
    private Date createTime;

    /**
     * 综合评分
     */
    private Double totalScores;

    /**
     * 课程资料评分
     */
    private Double materialScores;

    /**
     * 教学内容评分
     */
    private Double contentScores;

    /**
     * 教学风格评分
     */
    private Double teachStyleScores;

    /**
     * 产品线
     */
    private String productId;

    /**
     * 创建日期
     */
    private Date createDate;
}

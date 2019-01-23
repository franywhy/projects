package com.hq.bi.search.entity;

import lombok.Data;

import java.util.Date;

/**
 * Description:
 * Autor: hutao
 * Date: 2018-12-10-18:41
 */
@Data
public class ErrorHistoryReportEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     * 测评结果表ID
     */
    private Long id;

    /**
     * 题目Id
     */
    private Long subjectId ;

    /**
     * 题目名称
     */
    private String subjectName;

    /**
     * 章编号
     */
    private String chapterCode;

    /**
     * 章名称
     */
    private String chapterName;

    /**
     * 节编号
     */
    private String sectionCode;

    /**
     * 节名称
     */
    private String sectionName;

    /**
     * 课程编号
     */
    private String courseCode;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 错题次数
     */
    private Long errorTimes;
    /**
     * 错题人数
     */
    private Long errorNumber;

    /**
     * 做题人数
     */
    private Long doneNumber;

    /**
     * 错题率
     */
    private String errorRate;

    /**
     * 考试时间
     */
    private Date testTime;
}

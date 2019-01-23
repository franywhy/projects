package com.hq.bi.search.entity;

import lombok.Data;

import java.util.Date;

/**
 * Description:教学考试质量评比页面结果
 * Autor: wl
 * Date: 2018-12-15
 */
@Data
public class TeachExamQualityEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     * 测评结果表ID
     */
    private Long id;


    /**
     * 学员ID
     */
    private Long userId ;


    /**
     * 讲师姓名
     */
    private String teacherName;

    /**
     * 讲师Id
     */
    private String teacherId;


    /**
     * 课程PK
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 学员规划时间
     */
    private Date courseUserPlanCreationTime;


    /**
     * 国考报考PK
     */
    private Long registrationId;

    /**
     * 国考报考报考时间
     */
    private Date registrationCreateTime;

    /**
     * 国考弃考PK
     */
    private String abandonId;

    /**
     * 国考成绩PK
     */
    private String examinationResultId;

    /**
     * 学员人数
     */
    private Integer studentCount;

    /**
     * 报考人数
     */
    private Integer registrationCount;

    /**
     * 弃考人数
     */
    private Integer abandonCount;

    /**
     * 国考通过率
     */
    private String examinationPassRate;

    /**
     * 国考通过率百分比
     */
    private String examinationPassRatePercent;

    /**
     * 国考平均分
     */
    private Double examinationAvgScore;

    /**
     * 国考成绩
     */
    private Integer examinationScore;

    /**
     * 国考参考人数
     */
    private Integer examinationCount;

    /**
     * 国考通过人数
     */
    private Integer examinationPassCount;


    /**
     * 模考平均分
     */
    private Double examAvgScore;



}

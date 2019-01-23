package com.hq.bi.offline.task.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:
 * Autor: wl
 * Date: 2018-12-13
 */
@Data
public class ClassExamQualityEntity  {
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
     * 班主任姓名
     */
    private String teacherName;



    /**
     * 专业PK
     */
    private Long professionId;

    /**
     * 专业名称
     */
    private String professionName;

    /**
     * 班级ID
     */
    private Long classId;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 课程PK
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;


    /**
     * 学员规划创建时间
     */
    private Date course_userplan_creation_time;

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
     * 国考成绩PK
     */
    private int examinationScore;
}

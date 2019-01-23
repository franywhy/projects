package com.hq.bi.search.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * description:
 * @author: hutao
 * @date 2018/12/14 11:58
 */
@Data
public class SchoolRollEvaluationEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     * 测评结果表ID
     */
    private Long id;


    /**
     * 班主任ID
     */
    private Long classTeacherId;

    /**
     * 班主任姓名
     */
    private String classTeacherName;


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
     * 总数
     */
    private Long totalNum;

    /**
     * 在读人数
     */
    private Long learningNum;

    /**
     * 休学人数
     */
    private Long pauseNum;

    /**
     * 休学率
     */
    private String pauseRate;

    /**
     * 退学人数
     */
    private Long dropNum;

    /**
     * 退学率
     */
    private String dropRate;


}

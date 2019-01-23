package com.hq.bi.search.entity;

import lombok.Data;

import java.util.Date;

/**
 * description:
 * @author: hutao
 * @date 2018/12/14 11:58
 */
@Data
public class PKIndexEvaluationEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     * 测评结果表ID
     */
    private Long id;

    /**
     * 学员数量
     */
    private Long userNum;

    /**
     * 班主任ID
     */
    private Long classTeacherId;

    /**
     * 班主任姓名
     */
    private String classTeacherName;

    /**
     * 讲师ID
     */
    private Long teacherId;

    /**
     * 讲师姓名
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
     * 排课计划ID
     */
    private String classPlanId;

    /**
     * 排课计划名称
     */
    private String classPlanName;

    /**
     * 直播课次ID
     */
    private String  classPlanLiveId;

    /**
     * 直播课程名称
     */
    private String classPlanLiveName;

    /**
     * 课次直播日期
     */
    private Date dayTime ;

    /**
     * 商品ID
     */
    private Long mallGoodsId;

    /**
     * 商品名称
     */
    private String mallGoodsName;

    /**
     * 预习作业正确率
     */
    private String previewCorrectRate ;

    /**
     * 预习作业完成率
     */
    private String previewFinishRate;

    /**
     * 课堂作业正确率
     */
    private String classroomCorrectRate;

    /**
     * 课堂作业完成率
     */
    private String classroomFinishRate;

    /**
     * 课后作业正确率
     */
    private String afterclassCorrectRate;

    /**
     * 课后作业完成率
     */
    private String afterclassFinishRate;

    /**
     * 出勤率
     */
    private String attendanceRate;

    /**
     * 授课好评率
     */
    private String evaluateGoodRate;

    /**
     * 问答总量
     */
    private Long questionTotalNum;

    /**
     * 已回答数量
     */
    private Long questionAnswerNum;


}

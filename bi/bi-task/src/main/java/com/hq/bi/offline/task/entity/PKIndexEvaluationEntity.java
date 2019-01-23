package com.hq.bi.offline.task.entity;

import lombok.Data;

import java.util.Date;

/**
 * Description:
 * @author: hutao
 * Date: 2018-12-10-18:41
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
     * 学员ID
     */
    private Long userId ;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 学员姓名
     */
    private String userName;

    /**
     * 学员手机号
     */
    private Long mobile;

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
     * 课程FK
     */
    private Long courseFk;

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
     * 地区ID
     */
    private Long areaId;

    /**
     * 商品ID
     */
    private Long mallGoodsId;

    /**
     * 商品名称
     */
    private String mallGoodsName;

    /**
     * 预习作业ID
     */
    private Long exercisesPreviewId ;

    /**
     * 预习提交状态  提交:1  未提交:0'
     */
    private Long previewSubmitStatus;

    /**
     * 预习作业正确数
     */
    private Long previewCorrectNum;

    /**
     * 预习作业错误数
     */
    private Long previewErrorNum;

    /**
     * 课堂作业ID
     */
    private Long exercisesClassroomId ;

    /**
     * 课堂作业提交状态  提交:1  未提交:0'
     */
    private Long classroomSubmitStatus;

    /**
     * 课堂作业正确数
     */
    private Long classroomCorrectNum;

    /**
     * 课堂作业错误数
     */
    private Long classroomErrorNum;

    /**
     * 课后作业ID
     */
    private Long exercisesAfterClassId ;

    /**
     * 课后作业提交状态  提交:1  未提交:0'
     */
    private Long afterClassSubmitStatus;

    /**
     * 课后作业正确数
     */
    private Long afterClassCorrectNum;

    /**
     * 课后作业错误数
     */
    private Long afterClassErrorNum;

    /**
     * 授课评分好评  好评:1  非好评:0
     */
    private Long evaluateGoodScore;

    /**
     * 应出勤时长(毫秒)
     */
    private Long fullDur;

    /**
     *直播和回放观看总时长(毫秒)
     */
    private Long watchDur;

    /**
     * 学习笔记ID
     */
    private Long teachNoteId;

    /**
     * 学习笔记创建时间
     */
    private Date noteCreateTime;

    /**
     * 学习笔记类型
     */
    private Long topicType;
}

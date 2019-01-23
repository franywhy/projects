package com.hq.bi.search.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Description:预习作业统计页面结果
 * Autor: wl
 * Date: 2018-12-15
 */
@Data
public class HomeWorkStaticEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 课程PK
     */
    private Long courseId;

    /**
     * 预习作业名称
     */
    private String homeWorkName ;


    /**
     * 产品线PK
     */
    private Long productId;


    /**
     * 所属课程
     */
    private String courseName;


    /**
     * 所属课次
     */
    private String classplanLiveName;


    /**
     * 所属课次PK
     */
    private String classplanLiveId;


    /**
     * 学员ID
     */
    private Long userId ;



    /**
     * 学员姓名
     */
    private String studentName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 提交状态
     */
    private String submitStatusName;

    /**
     * 作业提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date homeWorkSubmitTime;


    /**
     * 答对数
     */
    private Integer answerRightCount;

    /**
     * 答错数
     */
    private Integer answerWrongCount;

    /**
     * 知识点掌握数
     */
    private Integer kpNumRight;

    /**
     * 知识点总数
     */
    private Integer kpNum;


    /**
     * 知识点掌握度
     */
    private String knowledgeRightRate;

    /**
     * 正确率
     */
    private String rightRate;



}

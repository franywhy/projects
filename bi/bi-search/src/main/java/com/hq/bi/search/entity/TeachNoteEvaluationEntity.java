package com.hq.bi.search.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * description:
 * @author: hutao
 * @date 2018/12/14 11:58
 */
@Data
public class TeachNoteEvaluationEntity {
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
     * 学员姓名
     */
    private String userName;

    /**
     * 学员手机号
     */
    private String mobile;

    /**
     * 专业PK
     */
    private Long professionId;

    /**
     * 专业名称
     */
    private String professionName;

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
     * 商品ID
     */
    private Long mallGoodsId;

    /**
     * 商品名称
     */
    private String mallGoodsName;

    /**
     * 学习笔记数量
     */
    private Long teachNoteNum;

    /**
     * 学习笔记类型
     */
    private Long topicType;

    /**
     * 学习笔记创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date noteCreateTime;
}

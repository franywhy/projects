package com.hq.bi.offline.task.entity;

import java.util.Date;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author hzr
 * @since 2018-12-07
 */
@Data
public class TeachExamStatisticsEntity  {


    /**
     * 主键
     */

    private Long id;

    /**
     * 试卷名称
     */
    private String topic;

    /**
     * 专业
     */
    private String profession;

    /**
     * 课程
     */
    private String course;

    /**
     * 试卷类型('1:阶段考试、 2:模拟考试、 3:单科考试、4:考前冲刺')
     */
    private Integer examinationPaperType;

    /**
     * 学员姓名
     */
    private String studentName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 试卷总分
     */
    private String paperScore;

    /**
     * 强制批改(0：否，1：是)
     */
    private Integer isCorrect;

    /**
     * 考试提交时间
     */
    private Date submitTime;

    /**
     * 考试得分
     */
    private String examScore;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 提交状态(0：未提交，1：已提交)
     */
    private Integer isSubmit;


}

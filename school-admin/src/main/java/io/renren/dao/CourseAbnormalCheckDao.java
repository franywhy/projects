package io.renren.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 异常单方法
 * @author shihongjie
 * @date 2018-04-02
 */
public interface CourseAbnormalCheckDao {

    /**
     * 异常订单--其他
     * @param orderId   订单pk
     * @return
     */
    int queryAbnormalOrder(@Param(value="orderId")Long orderId);

    /**
     * 休学\失联单 结束
     * @param orderId       订单PK
     * @param abnormalType  异常类型(0-正常 1-休学 2-失联)
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @return
     */
    int queryCourseAbnormalOrderEnd(@Param(value="orderId")Long orderId ,@Param(value="abnormalType")int abnormalType,@Param(value="startTime")Date startTime ,@Param(value="endTime") Date endTime);

    /**
     * 休学\失联单 未结束
     * @param orderId       订单PK
     * @param abnormalType  异常类型(0-正常 1-休学 2-失联)
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @return
     */
    int queryCourseAbnormalOrderOpen(@Param(value="orderId")Long orderId , @Param(value="abnormalType")int abnormalType,@Param(value="startTime")Date startTime ,@Param(value="endTime") Date endTime);

    /**
     * 免考核
     * @param orderId       订单PK
     * @param courseId      课程PK
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @return
     */
    int queryCourseAbnormalFreeAssessment(@Param(value="orderId")Long orderId ,@Param(value="courseId")Long courseId , @Param(value="startTime")Date startTime ,@Param(value="endTime") Date endTime);

    /**
     * 弃考
     * @param userplanId        排课计划PK
     * @param courseId          课程PK
     * @param examScheduleId    考试时间段PK
     * @return
     */
    int queryCourseAbnormalAbandonExam(@Param(value="userplanId")Long userplanId ,@Param(value="courseId")Long courseId , @Param(value="examScheduleId")Long examScheduleId);

}

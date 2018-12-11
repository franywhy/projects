package io.renren.service;

import io.renren.entity.CourseAbormalEntity;
import io.renren.enums.CourseAbormalTypeEnum;
import io.renren.pojo.CourseAbormalDatePOJO;

import java.util.Date;
import java.util.List;

/**
 * 教师、休学、失联、弃考、免考等状态服务
 */
public interface CourseAbnormalCheckService {

    /**
     * 获取学员当前处于的状态
     * @see io.renren.enums.CourseAbormalTypeEnum
     * @param userId        学员PK
     * @param classplanIds   排课计划PK集合
     * @param orderId       订单PK
     * @param startDate     开始时间
     * @param endDate       结束时间
     * @return          学员当前状态
     */
    CourseAbormalTypeEnum getUserCourseAbormalTypeEnum(Long userId , List<String> classplanIds , Long orderId , Date startDate , Date endDate);

//    /**
//     * 获取学员某段时间的所有异常单
//     * @param userId 学员PK
//     * @param courseId 课程pk
//     * @param orderId 订单PK
//     * @param startDate 开始时间
//     * @param endDate 结束时间
//     * @param classplanId 排课计划pk
//     * @return
//     */
//    List<CourseAbormalEntity> getCourseAbormalList(Long userId , Long courseId, Long orderId , Date startDate , Date endDate , String classplanId);
//
//    /**
//     * 学员异常事件范围集合
//     * @param userId
//     * @param courseId
//     * @param orderId
//     * @param startDate
//     * @param endDate
//     * @param classplanId
//     * @return
//     */
//    List<CourseAbormalDatePOJO> getCourseAbormalDateList(Long userId , Long courseId, Long orderId , Date startDate , Date endDate , String classplanId);
}

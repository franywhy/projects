package io.renren.service.impl;

import io.renren.dao.CourseAbnormalCheckDao;
import io.renren.entity.CourseClassplanEntity;
import io.renren.entity.CourseUserplanEntity;
import io.renren.enums.CourseAbormalTypeEnum;
import io.renren.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("courseAbnormalCheckService")
public class CourseAbnormalCheckServiceImpl implements CourseAbnormalCheckService {

    @Autowired
    private CourseClassplanService courseClassplanService;
    @Autowired
    private CourseUserplanService courseUserplanService;
    @Autowired
    private CourseAbnormalCheckDao courseAbnormalCheckDao;

    /**
     * 获取学员当前处于的状态
     *
     * @param userId       学员PK
     * @param classplanIds 排课计划PK集合
     * @param orderId      订单PK
     * @param startDate    开始时间
     * @param endDate      结束时间
     * @return 学员当前状态
     * @see CourseAbormalTypeEnum
     */
    @Override
    public CourseAbormalTypeEnum getUserCourseAbormalTypeEnum(Long userId, List<String> classplanIds, Long orderId, Date startDate, Date endDate) {
        //校验订单
        if(checkQiTa(orderId)){
            return CourseAbormalTypeEnum.qita;
        }
        //休学
        if(checkXiuXue(orderId , startDate , endDate)){
            return CourseAbormalTypeEnum.xiuxue;
        }
        //失联
        if(checkShiLian(orderId , startDate , endDate)){
            return CourseAbormalTypeEnum.shilian;
        }
        //校验弃考
        if(checkQiKao(orderId,classplanIds)){
            return CourseAbormalTypeEnum.qikao;
        }
        //校验免考
        if(checkMianKao(orderId , classplanIds , startDate , endDate)){
            return CourseAbormalTypeEnum.miankao;
        }
        return CourseAbormalTypeEnum.normal;
    }

    /**
     * 其他
     * @param orderId
     * @return
     */
    private boolean checkQiTa(Long orderId){
        return this.courseAbnormalCheckDao.queryAbnormalOrder(orderId) > 0;
    }

    /**
     * 休学
     * @param orderId
     * @param startTime
     * @param endTime
     * @return
     */
    private boolean checkXiuXue(Long orderId, Date startTime , Date endTime){
        return (this.courseAbnormalCheckDao.queryCourseAbnormalOrderEnd(orderId, 1,startTime, endTime) > 0) ||
                (this.courseAbnormalCheckDao.queryCourseAbnormalOrderOpen(orderId,1, startTime, endTime) > 0);
    }

    /**
     * 失联
     * @param orderId
     * @param startTime
     * @param endTime
     * @return
     */
    private boolean checkShiLian(Long orderId, Date startTime , Date endTime){
        return (this.courseAbnormalCheckDao.queryCourseAbnormalOrderEnd(orderId, 2,startTime, endTime) > 0) ||
                (this.courseAbnormalCheckDao.queryCourseAbnormalOrderOpen(orderId,2, startTime, endTime) > 0);
    }

    /**
     * 免考(所有的排课免考-才叫做免考)
     * @param orderId
     * @param classplanIds
     * @param startTime
     * @param endTime
     * @return
     */
    private boolean checkMianKao(Long orderId , List<String> classplanIds , Date startTime , Date endTime){
        if(CollectionUtils.isNotEmpty(classplanIds)){
            for (int i = 0; i < classplanIds.size(); i++) {
                String classplanId = classplanIds.get(i);
                CourseClassplanEntity courseClassplanEntity = this.courseClassplanService.queryObjectByClassplanId(classplanId);
                if(null != courseClassplanEntity){
                    Long courseId = courseClassplanEntity.getCourseId();
                    if(this.courseAbnormalCheckDao.queryCourseAbnormalFreeAssessment(orderId, courseId, startTime, endTime) > 0){
                        continue;
                    }
                    return false;
                }
            }
            return true;
        }
        return false;

    }

    /**
     * 弃考(所有的课程都是弃考-才叫弃考)
     * @param orderId
     * @param classplanIds
     * @return
     */
    private boolean checkQiKao(Long orderId , List<String> classplanIds){

        CourseUserplanEntity courseUserplanEntity = this.courseUserplanService.queryUserplanObjectByOrderId(orderId);
        if(null != courseUserplanEntity && CollectionUtils.isNotEmpty(classplanIds)){
            Long userplanId = courseUserplanEntity.getUserPlanId();
            Long examScheduleId = courseUserplanEntity.getExamScheduleId();
            for (int i = 0; i < classplanIds.size(); i++) {
                String classplanId = classplanIds.get(i);
                CourseClassplanEntity courseClassplanEntity = this.courseClassplanService.queryObjectByClassplanId(classplanId);
                if(null != courseClassplanEntity){
                    Long courseId = courseClassplanEntity.getCourseId();
                    if(this.courseAbnormalCheckDao.queryCourseAbnormalAbandonExam(userplanId, courseId, examScheduleId) > 0){
                        continue;
                    }
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}

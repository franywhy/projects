package com.hq.learningcenter.school.service;

import com.hq.learningcenter.school.pojo.CourseAbnormallRegistrationPOJO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 报考申请业务方法
 * Created by DL on 2018/4/2.
 */
public interface CourseAbnormallRegistrationService {

    List<CourseAbnormallRegistrationPOJO> queryList(Map<String, Object> map);
    //获取未登记成绩报考列表
    List<CourseAbnormallRegistrationPOJO> queryExamPojoList(Map<String, Object> map);

    List<Map<String,Object>> queryAreaList(Map<String, Object> map);

    List<Map<String,Object>> queryScheduleList(Map<String, Object> map);

    void updateCancel(Integer value, Long id, Long userId, Date date);

    String save(Long orderId, Long courseId, Long areaId, Long scheduleId, String registerPK, Long userId);

    Long checkRegisteration(Long orderId,Long courseId,Long areaId, Long scheduleId);

    Integer queryRegisterCount(Long id);
}

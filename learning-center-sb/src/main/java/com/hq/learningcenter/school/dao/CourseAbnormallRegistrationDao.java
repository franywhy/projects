package com.hq.learningcenter.school.dao;

import com.hq.learningcenter.school.entity.CourseAbnormalRegistrationEntity;
import com.hq.learningcenter.school.pojo.CourseAbnormallRegistrationPOJO;
import com.hq.learningcenter.school.pojo.OrderPOJO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by DL on 2018/4/2.
 */
@Repository
public interface CourseAbnormallRegistrationDao {

    //获取报考列表
    List<CourseAbnormallRegistrationPOJO> queryPojoList(Map<String, Object> map);

    //获取未登记成绩报考列表
    List<CourseAbnormallRegistrationPOJO> queryExamPojoList(Map<String, Object> map);

    //获取地区列表
    List<Map<String,Object>> queryAreaList(Map<String, Object> map);

    //获取考试时间列表
    List<Map<String,Object>> queryScheduleList(Map<String, Object> map);

    //取消档案操作
    void updateCancel(@Param("auditStatus") Integer value, @Param("id")Long id, @Param("updatePerson")Long userId, @Param("updateTime")Date date);

    //检查报考单号是否重复
    int checkRegisterPK(String registerPK);

    //根据订单id获取订单信息
    OrderPOJO getMessageByOrderId(Long orderId);

    //保存报考单
    void save(CourseAbnormalRegistrationEntity entity);

    //检查是否有待审核报考单
    Long checkRegisteration(@Param("orderNo") String orderNo, @Param("courseId")Long courseId, @Param("areaId")Long areaId, @Param("scheduleId")Long scheduleId,@Param("statuValueNo")Integer statuValueNo,@Param("statuValue")Integer statuValue);
    //统计某学员报考一门课程的次数
    Integer queryRegisterCount( @Param("id")Long id);
}

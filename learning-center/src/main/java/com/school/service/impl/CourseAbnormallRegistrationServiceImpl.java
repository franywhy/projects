package com.school.service.impl;

import com.school.dao.CourseAbnormalFreeAssessmentDao;
import com.school.dao.CourseAbnormallRegistrationDao;
import com.school.entity.CourseAbnormalRegistrationEntity;
import com.school.enums.AuditStatusEnum;
import com.school.pojo.CourseAbnormallRegistrationPOJO;
import com.school.pojo.OrderPOJO;
import com.school.rest.persistent.KGS;
import com.school.service.CourseAbnormallRegistrationService;
import org.apache.commons.lang3.StringUtils;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by DL on 2018/4/2.
 */
@Service("courseAbnormallRegistrationService")
public class CourseAbnormallRegistrationServiceImpl implements CourseAbnormallRegistrationService {

    @Autowired
    private CourseAbnormallRegistrationDao courseAbnormallRegistrationDao;

    @Resource
    KGS courseAbnormallRegisterationPKKGS;

    @Override
    public List<CourseAbnormallRegistrationPOJO> queryList(Map<String, Object> map) {
        return courseAbnormallRegistrationDao.queryPojoList(map);
    }

    @Override
    public List<CourseAbnormallRegistrationPOJO> queryExamPojoList(Map<String, Object> map) {
        return courseAbnormallRegistrationDao.queryExamPojoList(map);
    }

    @Override
    public List<Map<String, Object>> queryAreaList(Map<String, Object> map) {
        return courseAbnormallRegistrationDao.queryAreaList(map);
    }

    @Override
    public List<Map<String, Object>> queryScheduleList(Map<String, Object> map) {
        return courseAbnormallRegistrationDao.queryScheduleList(map);
    }

    @Override
    public void updateCancel(Integer value, Long id, Long userId, Date date) {
        courseAbnormallRegistrationDao.updateCancel(value,id,userId,date);
    }

    @Override
    public String save(Long orderId, Long courseId, Long areaId, Long scheduleId, String registerPK, Long userId) {
        //根据订单id获取学员规划的等字段
        OrderPOJO orderPOJO =  courseAbnormallRegistrationDao.getMessageByOrderId(orderId);
        if (StringUtils.isNotBlank(registerPK)) {
            //检查报考登记号是否重复
            if (courseAbnormallRegistrationDao.checkRegisterPK(registerPK) > 0) {
                return "报考登记号" + registerPK + "已存在,请重新填写报考登记号!";
            }
        }
        final String registerNo = "HQBK"+courseAbnormallRegisterationPKKGS.nextId();
        CourseAbnormalRegistrationEntity entity = new CourseAbnormalRegistrationEntity();
        entity.setBkAreaId(areaId);
        entity.setCourseId(courseId);
        entity.setCreater(userId);
        entity.setCreateTime(new Date());
        entity.setDr(0);
        entity.setExamScheduleId(scheduleId+"");
        entity.setRegistrationNo(registerNo);
        entity.setStatus(AuditStatusEnum.daishenhe.getValue());
        entity.setRegistrationTime(new Date());
        entity.setRegistrationNumber(registerPK);
        entity.setUpdatePerson(userId);
        entity.setUpdateTime(new Date());
        entity.setOrderNo(orderPOJO.getOrderNo());
        entity.setProductId(orderPOJO.getProductId());
        entity.setUserPlanId(orderPOJO.getUserPlanId());
        //查询报考单是否有正在审核的
        if (courseAbnormallRegistrationDao.checkRegisteration(orderPOJO.getOrderNo(),courseId,areaId,scheduleId,AuditStatusEnum.quxiao.getValue(),null) != null){
            return "你有一个相同报考时间的课程报考单已经存在,请勿重复提交!";
        }else {
            courseAbnormallRegistrationDao.save(entity);
        }
        return null;
    }

    @Override
    public Long checkRegisteration(Long orderId, Long courseId, Long areaId, Long scheduleId) {
        OrderPOJO orderPOJO =  courseAbnormallRegistrationDao.getMessageByOrderId(orderId);
        return courseAbnormallRegistrationDao.checkRegisteration(orderPOJO.getOrderNo(),courseId,areaId,scheduleId,null,AuditStatusEnum.tongguo.getValue());
    }

    @Override
    public Integer queryRegisterCount(Long id) {
        return courseAbnormallRegistrationDao.queryRegisterCount(id);
    }
}

package com.school.service.impl;

import com.school.dao.CourseAbnormalFreeAssessmentDao;
import com.school.dao.CourseAbnormalOrderDao;
import com.school.dao.MallOrderDao;
import com.school.entity.CourseAbnormalFreeAssessmentEntity;
import com.school.enums.AuditStatusEnum;
import com.school.pojo.CourseAbnormalFreeAssessmentPOJO;
import com.school.rest.persistent.KGS;
import com.school.service.CourseAbnormalFreeAssessmentService;
import com.school.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("courseAbnormalFreeAssessmentService")
public class CourseAbnormalFreeAssessmentServiceImpl implements CourseAbnormalFreeAssessmentService {
	@Autowired
	private CourseAbnormalFreeAssessmentDao courseAbnormalFreeAssessmentDao;
	@Autowired
	private CourseAbnormalOrderDao courseAbnormalOrderDao;

	@Resource
    KGS courseAbnormalFreeAssessmentNoKGS;

	
	@Override
	public List<CourseAbnormalFreeAssessmentPOJO> queryList(Map<String, Object> map){
		return courseAbnormalFreeAssessmentDao.queryPojoList(map);
	}

	@Override
	public void save(Long userId, Long orderId, Long courseId, String startTime, String endTime, String abnormalReason) {
		CourseAbnormalFreeAssessmentEntity entity = new CourseAbnormalFreeAssessmentEntity();
		entity.setCreatePerson(userId);
		entity.setOrderId(orderId);
		entity.setCourseId(courseId);
		entity.setStartTime(DateUtils.parse(startTime));
		entity.setEndTime(DateUtils.parse(endTime));
		entity.setAbnormalReason(abnormalReason);
        entity.setProductId(courseAbnormalOrderDao.queryProductId(orderId));
        entity.setAuditStatus(AuditStatusEnum.daishenhe.getValue());
        final String orderNo = "AF" + courseAbnormalFreeAssessmentNoKGS.nextId();
        entity.setOrderno(orderNo);
        courseAbnormalFreeAssessmentDao.save(entity);

	}

	@Override
	public void updateCancel(Integer auditStatus,Long id,Long userId,Date date){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",id);
        map.put("auditStatus",auditStatus);
        map.put("updatePerson",userId);
        map.put("updateTime",date);
	    courseAbnormalFreeAssessmentDao.updateCancel(map);
	}

	@Override
	public List<Map<String , Object>> queryCourseList(Long orderId) {
		String userPlanId = courseAbnormalFreeAssessmentDao.queryUserPlanId(orderId.toString());
		List<Map<String , Object>> list = null;
		if(userPlanId != null){
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("userPlanId",userPlanId);
			list = this.courseAbnormalFreeAssessmentDao.courseListByUserPlanId(map);
		}
		return list;
	}
    @Override
	public String  verifyStatus(Long orderId, Date startTime, Date endTime,Long courseId){
		Map queryMap = new HashMap();
		queryMap.put("orderId",orderId);
		queryMap.put("courseId",courseId);
		queryMap.put("startTime",startTime);
		CourseAbnormalFreeAssessmentPOJO courseAbnormalFreeAssessmentPOJO = courseAbnormalFreeAssessmentDao.verifyStatus(queryMap);
		if(courseAbnormalFreeAssessmentPOJO != null){
			return "此课程已申请，请勿重复提交！";
		}
		return null;
	}
}

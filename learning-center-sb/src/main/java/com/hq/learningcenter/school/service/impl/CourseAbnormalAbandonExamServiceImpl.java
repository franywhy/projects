package com.hq.learningcenter.school.service.impl;

import com.hq.learningcenter.school.dao.CourseAbnormalAbandonExamDao;
import com.hq.learningcenter.school.dao.CourseAbnormalOrderDao;
import com.hq.learningcenter.school.enums.AuditStatusEnum;
import com.hq.learningcenter.school.pojo.MallExamSchedulePOJO;
import com.hq.learningcenter.school.service.CourseAbnormalAbandonExamService;
import com.hq.learningcenter.school.entity.CourseAbnormalAbandonExamEntity;
import com.hq.learningcenter.school.pojo.CourseAbnormalAbandonExamPOJO;
import com.hq.learningcenter.school.rest.persistent.KGS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("courseAbnormalAbandonExamService")
public class CourseAbnormalAbandonExamServiceImpl implements CourseAbnormalAbandonExamService {
	@Autowired
	private CourseAbnormalAbandonExamDao courseAbnormalAbandonExamDao;

//	@Resource
//	KGS invoicesNumberKGS;
//	private static final String INVOICESNUMBER_HEAD = "HQQK_";

	@Resource
    KGS invoicesNumberKGS;

	@Autowired
	private CourseAbnormalOrderDao courseAbnormalOrderDao;

	@Override
	public List<MallExamSchedulePOJO> queryScheduleDateList(Map<String,Object> map) {
		return courseAbnormalAbandonExamDao.queryScheduleDateList(map);
	}

	@Override
	public CourseAbnormalAbandonExamPOJO queryObject(Long id){
		return courseAbnormalAbandonExamDao.queryObject(id);
	}

	@Override
	public String verifyStatus(String orderNo, Long courseId, Long bkAreaId, Long scheduleId) {
		return null;
	}

	@Override
	public void updateCancel(Integer auditStatus, Long id, Long userId, Date date) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id",id);
		map.put("auditStatus",auditStatus);
		map.put("updatePerson",userId);
		map.put("updateTime",date);
		courseAbnormalAbandonExamDao.updateCancel(map);
	}


	@Override
	public List<CourseAbnormalAbandonExamPOJO> queryPOJOList(Map<String, Object> map) {
		return courseAbnormalAbandonExamDao.queryPOJOList(map);
	}

	@Override
	public void save(Long userId, Long orderId, String abnormalReason,Long registrationId) {
		CourseAbnormalAbandonExamEntity examEntity = new CourseAbnormalAbandonExamEntity();
		examEntity.setCreater(userId);
		examEntity.setCreateTime(new Date());
		examEntity.setInvoicesNumber("HQQK_"+invoicesNumberKGS.nextId());
		examEntity.setProductId(courseAbnormalOrderDao.queryProductId(orderId));
		examEntity.setStatus(AuditStatusEnum.daishenhe.getValue());
		examEntity.setReason(abnormalReason);
		examEntity.setRegistrationId(registrationId);
		courseAbnormalAbandonExamDao.save(examEntity);
	}
}

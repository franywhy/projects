package io.renren.modules.job.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.modules.job.dao.SchoolReportDetailDao;
import io.renren.modules.job.entity.SchoolReportDetailEntity;
import io.renren.modules.job.pojo.ClassPlanLivesDetailPOJO;
import io.renren.modules.job.pojo.SchoolReportUserMessagePOJO;
import io.renren.modules.job.service.SchoolReportDetailService;

import java.util.List;
import java.util.Map;




@Service("schoolReportDetailService")
public class SchoolReportDetailServiceImpl implements SchoolReportDetailService {
	@Autowired
	private SchoolReportDetailDao schoolReportDetailDao;

	@Override
	public void save(SchoolReportDetailEntity schoolReportDetail){
		schoolReportDetailDao.save(schoolReportDetail);
	}
	@Override
	public List<SchoolReportUserMessagePOJO> queryUserMessage() {
		return schoolReportDetailDao.queryUserMessage();
	}


	@Override
	public List<String> classPlanIdByOrder(Long orderId) {
		return schoolReportDetailDao.classPlanIdByOrder(orderId);
	}

	@Override
	public List<ClassPlanLivesDetailPOJO> getClassPlanLivesDetail(Long orderId,Long userId, String startDateStr, String endDateStr) {
		return schoolReportDetailDao.getClassPlanLivesDetail(orderId,userId,startDateStr,endDateStr);
	}
}

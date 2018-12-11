package io.renren.modules.job.service;


import java.util.List;
import java.util.Map;

import io.renren.modules.job.entity.SchoolReportDetailEntity;
import io.renren.modules.job.pojo.ClassPlanLivesDetailPOJO;
import io.renren.modules.job.pojo.SchoolReportUserMessagePOJO;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-03-17 16:09:03
 */
public interface SchoolReportDetailService {

	
	void save(SchoolReportDetailEntity schoolReportDetail);


	//获取班主任底下的学员信息
    List<SchoolReportUserMessagePOJO> queryUserMessage();

    //根据学员id获取排课信息
    List<String> classPlanIdByOrder(Long orderId);

    //获取学员一段时间内课次是否有异常
    List<ClassPlanLivesDetailPOJO> getClassPlanLivesDetail(Long orderId, Long userId, String startDateStr, String endDateStr);
}

package io.renren.service;


import io.renren.entity.SchoolReportDetailEntity;
import io.renren.pojo.ClassPlanLivesDetailPOJO;
import io.renren.pojo.SchoolReportUserMessagePOJO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-03-17 16:09:03
 */
public interface SchoolReportDetailService {
	
	SchoolReportDetailEntity queryObject(Long id);
	
	List<SchoolReportDetailEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SchoolReportDetailEntity schoolReportDetail);
	
	void update(SchoolReportDetailEntity schoolReportDetail);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	//获取班主任底下的学员信息
    List<SchoolReportUserMessagePOJO> queryUserMessage();

    //根据学员id获取排课信息
    List<String> classPlanIdByOrder(Long orderId);

    //获取学员一段时间内课次是否有异常
    List<ClassPlanLivesDetailPOJO> getClassPlanLivesDetail(Long orderId,Long userId, String startDateStr, String endDateStr);
}

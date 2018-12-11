package io.renren.dao;

import io.renren.entity.CourseAbnormalAbandonExamEntity;
import io.renren.pojo.CourseAbnormalAbandonExamPOJO;
import io.renren.pojo.CourseAbnormalRegistrationPOJO1;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 弃考档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-27 14:37:18
 */
public interface CourseAbnormalAbandonExamDao extends BaseDao<CourseAbnormalAbandonExamEntity> {
	
	String saveExamEntity(CourseAbnormalAbandonExamEntity courseAbnormalAbandonExam);
	
	List<CourseAbnormalAbandonExamPOJO> queryPOJOList(Map<String, Object> map);			
	//作废状态
	void updateCancelStatus(Map<String, Object> map);
	//审核状态	
	void updatePassStatus(Map<String, Object> map);
	
	void updateStatus(Map<String, Object> map);

	CourseAbnormalRegistrationPOJO1 queryByOrderNo(String orderNo);
	
	CourseAbnormalRegistrationPOJO1 queryByNo(Map<String, Object> map);
	
	CourseAbnormalRegistrationPOJO1  queryByRegistrationNo(Map<String, Object> map);
	
	List<CourseAbnormalAbandonExamEntity> queryByRegistrationId(Map<String, Object> map);
	
	/**
	 *
	 * @param registrationId
	 * @param status
	 * @return
	 */
	CourseAbnormalAbandonExamEntity queryEntityByRegistrationId(@Param("registrationId")Long registrationId ,@Param("status")int status);
	
	CourseAbnormalAbandonExamEntity queryObject(Long id);
	
	
}

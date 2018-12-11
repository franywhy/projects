package io.renren.dao;

import io.renren.entity.CourseAbnormalFreeAssessmentEntity;
import io.renren.pojo.CourseAbnormalFreeAssessmentPOJO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 休学失联记录单
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-19 09:26:43
 */
public interface CourseAbnormalFreeAssessmentDao extends BaseMDao<CourseAbnormalFreeAssessmentEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

    List<CourseAbnormalFreeAssessmentPOJO> queryPojoList(Map<String, Object> map);

	CourseAbnormalFreeAssessmentPOJO queryPojoObject(@Param(value = "id") Long id);

	CourseAbnormalFreeAssessmentPOJO verifyStatus(Map<String, Object> map);

	int updateStatus(Map<String, Object> map);

	int updateCancelBatch(Map<String, Object> map);

	int updateAuditPassBatch(Map<String, Object> map);

	List<Long> queryCoursesId(@Param(value = "courseName") String courseName);
}

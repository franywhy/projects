package io.renren.dao;

import io.renren.entity.CourseExamScheduleEntity;
import io.renren.pojo.CourseExamSchedulePOJO;

import java.util.List;
import java.util.Map;

/**
 * 课程考试时段档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-24 18:55:01
 */
public interface CourseExamScheduleDao extends BaseMDao<CourseExamScheduleEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	/**
	 * 查询列表
	 * @param map
	 * @return
	 */
	List<CourseExamSchedulePOJO> queryPojoList(Map<String, Object> map);

	/**
	 * 根据id查询信息
	 * @param map
	 * @return
	 */
	CourseExamSchedulePOJO queryPojoObject(Map<String, Object> map);
}

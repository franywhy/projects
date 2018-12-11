package io.renren.modules.job.service;

import java.util.List;
import java.util.Map;

import io.renren.modules.job.entity.CourseClassplanEntity;

/**
 * 排课计划表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-06 10:45:58
 */
public interface CourseClassplanService {


	/**
	 * 查询排课 根据排课ID
	 */
	CourseClassplanEntity queryObjectByClassplanId(String classplanId);

	List<Map<String , Object>> queryClassPlanForQueue(Map<String, Object> map);

	List<Map<String, Object>> querySsCourseClassplanMessage(String millisecond);
}

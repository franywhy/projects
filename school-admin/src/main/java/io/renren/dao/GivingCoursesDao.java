package io.renren.dao;

import java.util.List;
import java.util.Map;

import io.renren.entity.GivingCoursesEntity;
import io.renren.pojo.GivingCoursesPOJO;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-31 15:08:46
 */
public interface GivingCoursesDao extends BaseMDao<GivingCoursesEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	List<GivingCoursesPOJO> queryPojoList(Map<String,Object> map);
}

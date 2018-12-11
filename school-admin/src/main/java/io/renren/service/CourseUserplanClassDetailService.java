package io.renren.service;

import io.renren.entity.CourseUserplanClassDetailEntity;
import io.renren.pojo.CourseUserplanClassDetailPOJO;
import io.renren.utils.UserPlanClassDetailException;

import java.util.List;
import java.util.Map;

/**
 * 学习计划排课详情
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-28 15:41:14
 */
public interface CourseUserplanClassDetailService {
	
		
	CourseUserplanClassDetailEntity queryObject(Map<String, Object> map);
	
	List<CourseUserplanClassDetailEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseUserplanClassDetailEntity courseUserplanClassDetail) throws UserPlanClassDetailException;
	
	void update(CourseUserplanClassDetailEntity courseUserplanClassDetail) throws UserPlanClassDetailException;
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

	/**
	 * 根据userplanClassId查询子表列表信息
	 * @param map2
	 * @return
	 */
	List<CourseUserplanClassDetailPOJO> queryListByUserplanClassId(Map<String, Object> map2);

	/**
	 * 删除ID不等于userplanClassDetailIds的数据
	 * @param userplanClassDetailIds = id数组
	 * @param userplanClassId = 学习计划PK
	 */
	void deleteBatchNotIn(Map<String, Object> map);
	
	List<Map<String , Object>> queryUserplanClassDetailForQueue(Map<String, Object> map);

	//修改问题订单的学习计划子表dr = 1
    void updateChangeByUserplanClassId(Long courseUserplanClassId);


}

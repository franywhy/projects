package io.renren.dao;

import io.renren.entity.CourseUserplanClassDetailEntity;
import io.renren.pojo.CourseUserplanClassDetailPOJO;

import java.util.List;
import java.util.Map;

/**
 * 学习计划排课详情
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-28 15:41:14
 */
public interface CourseUserplanClassDetailDao extends BaseMDao<CourseUserplanClassDetailEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

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
	
	/**
	 * 查询学员同一门课程有没有排在同意个排课计划中
	 * @param classplanId			排课主键
	 * @param userplanDetailId		学员规划子表主键
	 * @param schoolId				平台ID
	 * @param userplanClassDetailId	当前学习计划子表主键(非必填)
	 * @return
	 */
	Map<String , Object> selectOld(Map<String , Object> map);
	
	List<Map<String , Object>> queryUserplanClassDetailForQueue(Map<String, Object> map);
    //修改问题订单的学习计划子表dr = 1
    void updateChangeByUserplanClassId(Long courseUserplanClassId);

}

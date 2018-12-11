package io.renren.dao;

import io.renren.entity.CourseUserplanClassEntity;
import io.renren.pojo.CourseUserplanClassPOJO;

import java.util.List;
import java.util.Map;

/**
 * 学习计划
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-27 09:55:36
 */
public interface CourseUserplanClassDao extends BaseMDao<CourseUserplanClassEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	/**
	 * 查询列表
	 * @param map
	 * @return
	 */
	List<CourseUserplanClassPOJO> queryPojoList(Map<String, Object> map);

	/**
	 * 根据id查询信息
	 * @param map
	 * @return
	 */
	CourseUserplanClassPOJO queryPojoObject(Map<String, Object> map);

	/**
	 * 审核
	 * @param map
	 */
	int audited(Map<String, Object> map);
	
	Integer queryUserClassPlanMid(String mId);
    //修改问题订单的学习计划dr = 1
    void updateChangeByOrderNo(Long userplanId);

    //根据学员规划获取学习计划id
    List<Long> queryCourseUserplanClass(Long userPlanId);
}

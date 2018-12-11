package io.renren.dao;

import io.renren.entity.ScheduleJobEntity;

import java.util.List;
import java.util.Map;

/**
 * 定时任务
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月1日 下午10:29:57
 */
public interface ScheduleJobDao extends BaseDao<ScheduleJobEntity> {
	
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	/**
	 * 根据条件查询
	 * @param map
	 * @return
	 */
	List<ScheduleJobEntity> queryListByExitId(Map<String, Object> map);
	
	/**
	 * 通过外部ID删除资料
	 * @param map
	 * @return
	 */
	int deleteBatchByExitId(Map<String, Object> map);
	
}

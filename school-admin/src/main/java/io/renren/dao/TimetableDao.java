package io.renren.dao;

import io.renren.entity.TimetableEntity;
import io.renren.pojo.timetable.TimeTablePOJO;

import java.util.List;
import java.util.Map;

/**
 * 上课点档案
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-20 16:43:49
 */
public interface TimetableDao extends BaseDao<TimetableEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	int saveAndGetKey(TimetableEntity t);

	/**
	 * 上课时点列表
	 * @param map
	 * @return
	 */
	List<TimetableEntity> findTimetableList(Map<String, Object> map);

	/**
	 * 查询信息
	 * @param number
	 * @return
	 */
	TimeTablePOJO queryPojoObject(Long number);

	/**
	 * 查询列表
	 * @param map
	 * @return
	 */
	List<TimeTablePOJO> queryPojoList(Map<String, Object> map);
	
	/**
	 * 查询status=1的上课时点列表
	 * @param map
	 * @return
	 */
	List<TimeTablePOJO> queryPojoList1(Map<String, Object> map);
	
	/**
	 * 查询status=1的上课时点列表总数
	 * @param map
	 * @return
	 */
	int queryTotal1(Map<String, Object> map);
}

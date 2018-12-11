package io.renren.service;

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
public interface TimetableService {
	
	TimetableEntity queryObject(Long number);
	
	List<TimetableEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TimeTablePOJO timetable);
	
	void update(TimeTablePOJO timetable);
	
	void delete(Long number);
	
	void deleteBatch(Long[] numbers);
	
	void pause(Long[] numbers);
	
	void resume(Long[] numbers);

	/**
	 * 上课时点列表
	 * @param map
	 * @return
	 */
	List<TimetableEntity> findTimetableList(Map<String, Object> map);

	//查询信息
	TimeTablePOJO queryPojoObject(Long number);

	//查询列表
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

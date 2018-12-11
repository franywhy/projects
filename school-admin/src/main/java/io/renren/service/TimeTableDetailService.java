package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.TimeTableDetailEntity;

public interface TimeTableDetailService {
	
	List<TimeTableDetailEntity> queryObject(Long number);

	int queryTotal(Map<String, Object> map);

	void save(TimeTableDetailEntity timetable);

	int update(TimeTableDetailEntity timetable);

	void delete(Long number);

	void deleteBatch(Long[] numbers);

	/**
	 * 删除ID不等于ids的数据
	 * @param ids = id数组
	 * @param number = PK
	 */
	void deleteBatchNotIn(Map<String, Object> map);


}

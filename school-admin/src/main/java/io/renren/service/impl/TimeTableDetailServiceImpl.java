package io.renren.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.TimeTableDetailDao;
import io.renren.entity.TimeTableDetailEntity;
import io.renren.service.TimeTableDetailService;

@Service("TimeTableDetailService")
public class TimeTableDetailServiceImpl implements TimeTableDetailService{
	@Autowired
	private TimeTableDetailDao timeTableDetailDao;
	
	@Override
	public List<TimeTableDetailEntity> queryObject(Long number) {
		return timeTableDetailDao.queryList(number);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return timeTableDetailDao.queryTotal(map);
	}

	@Override
	public void save(TimeTableDetailEntity timetable) {
		timeTableDetailDao.save(timetable);
	}

	@Override
	public int update(TimeTableDetailEntity timetable) {
		return timeTableDetailDao.update(timetable);
	}

	@Override
	public void delete(Long number) {
		timeTableDetailDao.delete(number);
		
	}

	@Override
	public void deleteBatch(Long[] numbers) {
		timeTableDetailDao.deleteBatch(numbers);	
	}

	/**
	 * 删除ID不等于ids的数据
	 * @param ids = id数组
	 * @param number = PK
	 */
	@Override
	public void deleteBatchNotIn(Map<String, Object> map) {
		timeTableDetailDao.deleteBatchNotIn(map);
	}


}

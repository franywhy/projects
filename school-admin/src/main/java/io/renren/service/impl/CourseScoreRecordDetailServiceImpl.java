package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import io.renren.dao.CourseScoreRecordDetailDao;
import io.renren.entity.CourseScoreRecordDetailEntity;
import io.renren.service.CourseScoreRecordDetailService;



@Service("courseScoreRecordDetailService")
public class CourseScoreRecordDetailServiceImpl implements CourseScoreRecordDetailService {
	@Autowired
	private CourseScoreRecordDetailDao courseScoreRecordDetailDao;
	
	@Override
	public CourseScoreRecordDetailEntity queryObject(Map<String, Object> map){
		return courseScoreRecordDetailDao.queryObject(map);
	}
	
	@Override
	public List<CourseScoreRecordDetailEntity> queryList(Map<String, Object> map){
		return courseScoreRecordDetailDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseScoreRecordDetailDao.queryTotal(map);
	}
	
	@Override
	public void save(CourseScoreRecordDetailEntity courseScoreRecordDetail){
		courseScoreRecordDetailDao.save(courseScoreRecordDetail);
	}
	
	@Override
	public void update(CourseScoreRecordDetailEntity courseScoreRecordDetail){
		courseScoreRecordDetailDao.update(courseScoreRecordDetail);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		courseScoreRecordDetailDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		courseScoreRecordDetailDao.deleteBatch(map);
	}

	@Override
	public List<Map<String, Object>> queryAllByPK(Map<String, Object> map) {
		return courseScoreRecordDetailDao.queryAllByPK(map);
	}
	
	
}

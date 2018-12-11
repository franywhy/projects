package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.CourseExamRecordDetailDao;
import io.renren.entity.CourseExamRecordDetailEntity;
import io.renren.service.CourseExamRecordDetailService;
import io.renren.utils.Constant;



@Service("courseExamRecordDetailService")
public class CourseExamRecordDetailServiceImpl implements CourseExamRecordDetailService {
	@Autowired
	private CourseExamRecordDetailDao courseExamRecordDetailDao;
	
	@Override
	public CourseExamRecordDetailEntity queryObject(Map<String, Object> map){
		return courseExamRecordDetailDao.queryObject(map);
	}
	
	@Override
	public List<CourseExamRecordDetailEntity> queryList(Map<String, Object> map){
		return courseExamRecordDetailDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseExamRecordDetailDao.queryTotal(map);
	}

    @Override
    public int queryTotal2(Map<String, Object> map) {
        return courseExamRecordDetailDao.queryTotal2(map);
    }

    @Override
	public void save(CourseExamRecordDetailEntity courseExamRecordDetail){
		courseExamRecordDetailDao.save(courseExamRecordDetail);
	}
	
	@Override
	public void update(CourseExamRecordDetailEntity courseExamRecordDetail){
		courseExamRecordDetailDao.update(courseExamRecordDetail);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		courseExamRecordDetailDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		courseExamRecordDetailDao.deleteBatch(map);
	}

	@Override
	public void deleteBatchNotIn(Map<String, Object> map) {
		courseExamRecordDetailDao.deleteBatchNotIn(map);
	}

	@Override
	public List<Map<String, Object>> queryAll(Map<String, Object> map) {
		return courseExamRecordDetailDao.queryAll(map);
	}

	@Override
	public void deleteDetailBatch(Map<String, Object> map) {
		courseExamRecordDetailDao.deleteDetailBatch(map);
	}
	
	
}

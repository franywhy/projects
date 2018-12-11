package io.renren.service.impl;

import java.util.List;
import java.util.Map;

import io.renren.utils.BeanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.renren.dao.CourseScoreRecordDao;
import io.renren.entity.CourseExamRecordEntity;
import io.renren.entity.CourseScoreRecordDetailEntity;
import io.renren.entity.CourseScoreRecordEntity;
import io.renren.service.CourseScoreRecordDetailService;
import io.renren.service.CourseScoreRecordService;



@Service("courseScoreRecordService")
public class CourseScoreRecordServiceImpl implements CourseScoreRecordService {
	@Autowired
	private CourseScoreRecordDao courseScoreRecordDao;
	@Autowired
	private CourseScoreRecordDetailService courseScoreRecordDetailService;
	
	@Override
	public CourseScoreRecordEntity queryObject(Map<String, Object> map){
		return courseScoreRecordDao.queryObject(map);
	}
	
	@Override
	public List<CourseScoreRecordEntity> queryList(Map<String, Object> map){
		return courseScoreRecordDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseScoreRecordDao.queryTotal(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void save(CourseScoreRecordEntity courseScoreRecord){
		//加入去除前后空格操作
		BeanHelper.beanAttributeValueTrim(courseScoreRecord);
		courseScoreRecordDao.save(courseScoreRecord);
		List<CourseScoreRecordDetailEntity> detailList = courseScoreRecord.getDetailList();
		if(null != detailList && detailList.size() > 0){
			for(int i=0;i<detailList.size();i++){
				CourseScoreRecordDetailEntity courseScoreRecordDetail = detailList.get(i);
				courseScoreRecordDetail.setScoreRecordId(courseScoreRecord.getScoreRecordId());
				//加入去除前后空格操作
				BeanHelper.beanAttributeValueTrim(courseScoreRecordDetail);
				courseScoreRecordDetailService.save(courseScoreRecordDetail);
			}
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void update(CourseScoreRecordEntity courseScoreRecord){
		courseScoreRecordDao.update(courseScoreRecord);
		List<CourseScoreRecordDetailEntity> detailList = courseScoreRecord.getDetailList();
		if(null != detailList && detailList.size() > 0){
			for(int i=0;i<detailList.size();i++){
				CourseScoreRecordDetailEntity courseScoreRecordDetail = detailList.get(i);
				courseScoreRecordDetail.setScoreRecordId(courseScoreRecord.getScoreRecordId());
				
				courseScoreRecordDetailService.update(courseScoreRecordDetail);
			}
		}
	}
	
	@Override
	public void delete(Map<String, Object> map){
		courseScoreRecordDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		courseScoreRecordDao.deleteBatch(map);
	}

	@Override
	public List<CourseExamRecordEntity> queryExamRecordList(Map<String, Object> map) {
		return courseScoreRecordDao.queryExamRecordList(map);
	}

	@Override
	public int queryExamRecordTotal(Map<String, Object> map) {
		return courseScoreRecordDao.queryExamRecordTotal(map);
	}

	@Override
	public List<Map<String, Object>> queryAll(Map<String, Object> map) {
		return courseScoreRecordDao.queryAll(map);
	}

	@Override
	public Map<String, Object> queryOne(Map<String, Object> map) {
		return courseScoreRecordDao.queryOne(map);
	}

	@Override
	public void accept(CourseScoreRecordEntity courseScoreRecord) {
		courseScoreRecordDao.accept(courseScoreRecord);
		
	}

	@Override
	public List<CourseExamRecordEntity> queryExamRecordListForExport(Map<String, Object> map) {
		return courseScoreRecordDao.queryExamRecordListForExport(map);
	}
	
}

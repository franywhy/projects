package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.CourseExamRecordEntity;

/**
 * 报考登记
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-26 15:21:19
 */
public interface CourseExamRecordService {
	
		
	CourseExamRecordEntity queryObject(Map<String, Object> map);
	
	List<CourseExamRecordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseExamRecordEntity courseExamRecord);
	
	void update(CourseExamRecordEntity courseExamRecord);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

	void accept(CourseExamRecordEntity courseExamRecord);
	
	CourseExamRecordEntity getExamRecordIdByNo(Map<String, Object> map);
		
}

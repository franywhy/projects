package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.CourseExamRecordEntity;
import io.renren.entity.CourseScoreRecordEntity;

/**
 * 分数登记
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-04 09:27:06
 */
public interface CourseScoreRecordService {
	
		
	CourseScoreRecordEntity queryObject(Map<String, Object> map);
	
	List<CourseScoreRecordEntity> queryList(Map<String, Object> map);
	
	List<Map<String, Object>> queryAll(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseScoreRecordEntity courseScoreRecord);
	
	void update(CourseScoreRecordEntity courseScoreRecord);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

	List<CourseExamRecordEntity> queryExamRecordList(Map<String, Object> map);
	
	List<CourseExamRecordEntity> queryExamRecordListForExport(Map<String, Object> map);
	
	int queryExamRecordTotal(Map<String, Object> map);

	Map<String, Object> queryOne(Map<String, Object> map);

	void accept(CourseScoreRecordEntity courseScoreRecord);

}

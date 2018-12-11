package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.CourseScoreRecordDetailEntity;

/**
 * 分数登记子表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-04 09:27:06
 */
public interface CourseScoreRecordDetailService {
	
		
	CourseScoreRecordDetailEntity queryObject(Map<String, Object> map);
	
	List<CourseScoreRecordDetailEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseScoreRecordDetailEntity courseScoreRecordDetail);
	
	void update(CourseScoreRecordDetailEntity courseScoreRecordDetail);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

	List<Map<String, Object>> queryAllByPK(Map<String, Object> map);
	
		
		
}

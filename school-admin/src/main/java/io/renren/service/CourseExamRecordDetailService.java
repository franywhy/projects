package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.CourseExamRecordDetailEntity;

/**
 * 报考登记学员course_exam_record_user
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-28 17:21:26
 */
public interface CourseExamRecordDetailService {
	
		
	CourseExamRecordDetailEntity queryObject(Map<String, Object> map);
	
	List<CourseExamRecordDetailEntity> queryList(Map<String, Object> map);
	
	List<Map<String, Object>> queryAll(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);

    /**
     * 条件查询分页总数
     * @param map
     * @return
     */
	int queryTotal2(Map<String, Object> map);

	void save(CourseExamRecordDetailEntity courseExamRecordDetail);
	
	void update(CourseExamRecordDetailEntity courseExamRecordDetail);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

	void deleteBatchNotIn(Map<String, Object> map);

	void deleteDetailBatch(Map<String, Object> map);
	
		
		
}

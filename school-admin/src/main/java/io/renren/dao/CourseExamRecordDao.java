package io.renren.dao;

import io.renren.entity.CourseExamRecordEntity;

import java.util.Map;

/**
 * 报考登记
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-26 15:21:19
 */
public interface CourseExamRecordDao extends BaseMDao<CourseExamRecordEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	void accept(CourseExamRecordEntity entity);
	
	CourseExamRecordEntity getExamRecordIdByNo(Map<String, Object> map);
}

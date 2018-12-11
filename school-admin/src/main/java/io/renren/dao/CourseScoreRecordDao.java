package io.renren.dao;

import io.renren.entity.CourseExamRecordDetailEntity;
import io.renren.entity.CourseExamRecordEntity;
import io.renren.entity.CourseScoreRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * 分数登记
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-04 09:27:06
 */
public interface CourseScoreRecordDao extends BaseMDao<CourseScoreRecordEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	List<CourseExamRecordEntity> queryExamRecordList(Map<String, Object> map);

	int queryExamRecordTotal(Map<String, Object> map);
	
	List<Map<String, Object>> queryAll(Map<String, Object> map);

	Map<String, Object> queryOne(Map<String, Object> map);

	void accept(CourseScoreRecordEntity courseScoreRecord);
	
	List<CourseExamRecordEntity> queryExamRecordListForExport(Map<String, Object> map);
}

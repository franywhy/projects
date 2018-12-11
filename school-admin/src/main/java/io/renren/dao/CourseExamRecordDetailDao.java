package io.renren.dao;

import io.renren.entity.CourseExamRecordDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 报考登记学员course_exam_record_user
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-26 15:21:19
 */
public interface CourseExamRecordDetailDao extends BaseMDao<CourseExamRecordDetailEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	/**
	 * 批量删除不等于map的行
	 * @param map
	 */
	void deleteBatchNotIn(Map<String, Object> map);

	/**
	 * 查询子表
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryAll(Map<String, Object> map);

    /**
     * 条件查询子表分页总数
     * @param map
     * @return
     */
    int queryTotal2(Map<String, Object> map);

	/**
	 * 根据主表ID删除子表行
	 * @param map
	 */
	void deleteDetailBatch(Map<String, Object> map);
	
	
}

package io.renren.dao;

import io.renren.entity.CourseRecordDetailEntity;
import java.util.Map;

/**
 * 课程录播
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-11 09:45:44
 */
public interface CourseRecordDetailDao extends BaseMDao<CourseRecordDetailEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	/**
	 * 批量更新状态
	 */
	int queryJCount(Map<String, Object> map);
	/**
	 * 查询某一个录播课
	 * @param recordId
	 * @return
	 */
	io.renren.pojo.CourseRecordDetailPOJO queryPOJO(Long recordId);
	/**
	 * 查询录播课列表
	 * @return
	 */
	java.util.List<io.renren.pojo.CourseRecordDetailPOJO> queryPOJOList(Long courseId);
}

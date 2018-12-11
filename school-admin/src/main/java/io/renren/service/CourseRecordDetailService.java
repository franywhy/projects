package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.CourseRecordDetailEntity;

/**
 * 课程录播
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 09:51:40
 */
public interface CourseRecordDetailService {
	
		
	CourseRecordDetailEntity queryObject(Map<String, Object> map);
	
	/**
	 * 
	 * @param map
	 * @param map courseId
	 * @return
	 */
	List<CourseRecordDetailEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseRecordDetailEntity courseRecordDetail);
	
	void update(CourseRecordDetailEntity courseRecordDetail);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
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

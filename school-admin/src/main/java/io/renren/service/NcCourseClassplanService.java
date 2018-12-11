package io.renren.service;

import io.renren.entity.NcCourseClassplanEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-08-21 11:20:03
 */
public interface NcCourseClassplanService {
	
		
	NcCourseClassplanEntity queryObject(Map<String, Object> map);
	
	List<NcCourseClassplanEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(NcCourseClassplanEntity ncCourseClassplan);
	
	void update(NcCourseClassplanEntity ncCourseClassplan);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

    //查询所有未生成学员规划的排课数据
    List<NcCourseClassplanEntity> queryDataByTs(Map<String, Object> map);

    //查询所有未生成学员规划的排课数据总量
    Integer queryTotalNotSuccess();

    void updateIsSuccess(Long id);

    void updateIsSuccessByTime(String mobile, String classplanId, String ncModifiedTime);
}

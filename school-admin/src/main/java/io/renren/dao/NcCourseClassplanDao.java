package io.renren.dao;

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
public interface NcCourseClassplanDao extends BaseMDao<NcCourseClassplanEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

    List<NcCourseClassplanEntity> queryDataByTs(String ts);

    List<NcCourseClassplanEntity> queryDataByTs(Map<String, Object> map);

    Integer queryTotalNotSuccess();

    void updateIsSuccess(Map<String, Object> map);

    void updateIsSuccessByTime(Map<String, Object> map);
}

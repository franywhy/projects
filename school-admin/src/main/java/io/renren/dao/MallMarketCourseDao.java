package io.renren.dao;

import io.renren.entity.MallMarketCourseEntity;
import io.renren.pojo.MallMarketCoursePOJO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-01-19 11:58:28
 */
public interface MallMarketCourseDao extends BaseMDao<MallMarketCourseEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	List<MallMarketCoursePOJO> queryListClassifyPOJO(Map<String, Object> map);

	int queryTotalClassify(Map<String, Object> map);

	List<MallMarketCoursePOJO> queryListPOJO(Map<String, Object> map);

	List<MallMarketCoursePOJO> queryClassifyDownList(Map<String, Object> map);

	MallMarketCoursePOJO queryObjectPOJO(Map<String, Object> map);
}

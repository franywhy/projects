package io.renren.dao;

import io.renren.entity.AppCourseBannerEntity;
import io.renren.pojo.AppCourseBannerPOJO;

import java.util.List;
import java.util.Map;

/**
 * app课程banner档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-01-18 14:23:20
 */
public interface AppCourseBannerDao extends BaseMDao<AppCourseBannerEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	AppCourseBannerPOJO queryObjectPOJO(Map<String, Object> map);

	List<AppCourseBannerPOJO> queryListPOJO(Map<String, Object> map);
}

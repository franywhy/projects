package io.renren.dao;

import io.renren.entity.WebBannerEntity;
import java.util.List;
import java.util.Map;

/**
 * banner档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-24 15:47:07
 */
public interface WebBannerDao extends BaseMDao<WebBannerEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

}

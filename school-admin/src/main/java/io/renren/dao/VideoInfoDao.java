package io.renren.dao;

import io.renren.entity.VideoInfoEntity;
import java.util.Map;

/**
 * 录播信息
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-23 16:46:38
 */
public interface VideoInfoDao extends BaseMDao<VideoInfoEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}

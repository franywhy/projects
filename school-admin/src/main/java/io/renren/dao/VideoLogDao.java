package io.renren.dao;

import io.renren.entity.VideoLogEntity;
import java.util.Map;

/**
 * 观看录播日志-有效记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-23 16:46:38
 */
public interface VideoLogDao extends BaseMDao<VideoLogEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	/**
	 * 根据videoId查询VideoLogEntity
	 * @param map
	 * @return
	 */
	VideoLogEntity queryByVideoId(Map<String, Object> map);
	
	int videoLogExist(String mId);	
}

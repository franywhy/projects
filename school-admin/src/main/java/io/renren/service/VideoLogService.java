package io.renren.service;

import io.renren.entity.VideoLogEntity;

import java.util.List;
import java.util.Map;

/**
 * 观看录播日志-有效记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-23 16:46:38
 */
public interface VideoLogService {
	
		
	VideoLogEntity queryObject(Map<String, Object> map);
	
	List<VideoLogEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(VideoLogEntity videoLog);
	
	void update(VideoLogEntity videoLog);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
	VideoLogEntity queryByVideoId(Map<String, Object> map);
    
	int videoLogExist(String mId);	
		
}

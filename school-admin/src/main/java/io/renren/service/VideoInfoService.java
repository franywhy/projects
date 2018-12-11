package io.renren.service;

import io.renren.entity.VideoInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 录播信息
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-23 16:46:38
 */
public interface VideoInfoService {
	
		
	VideoInfoEntity queryObject(Map<String, Object> map);
	
	List<VideoInfoEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(VideoInfoEntity videoInfo);
	
	void update(VideoInfoEntity videoInfo);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
		
		
}

package io.renren.service;

import io.renren.entity.MallServiceRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-24 16:39:34
 */
public interface MallServiceRecordService {
	
	MallServiceRecordEntity queryObject(Long id);
	
	List<MallServiceRecordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(MallServiceRecordEntity mallServiceRecord);
	
	void update(MallServiceRecordEntity mallServiceRecord);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
	
	void pause(Long[] ids);
	
	void resume(Long[] ids);
	
}

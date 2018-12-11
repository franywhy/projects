package io.renren.service;



import io.renren.entity.ColdStartingEntity;

import java.util.List;
import java.util.Map;

/**
 * 冷启动数据
 * 
 * @author linyuebin
 * @email trust_100@163.com
 * @date 2017-12-30 11:30:54
 */
public interface ColdStartingService {
	
	ColdStartingEntity queryObject(Long id);
	
	List<ColdStartingEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ColdStartingEntity coldStarting);
	
	void update(ColdStartingEntity coldStarting);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

    void resume(Long[] orderIds);

    void pause(Long[] ids);
}

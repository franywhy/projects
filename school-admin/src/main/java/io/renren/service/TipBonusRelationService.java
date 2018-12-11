package io.renren.service;


import io.renren.pojo.manage.TipBonusRelationPOJO;

import java.util.List;
import java.util.Map;

/**
 * 标签红包关系service
 * 
 * @author vince
 * @date 2018-08-27 17:01:37
 */
public interface TipBonusRelationService {

	Object queryObject(Long id);
	
	List<TipBonusRelationPOJO> queryList(Map<String, Object> map);

	List<Object> queryListObject(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(Map map);
	
	void update(Map map);

	Boolean queryPower(Long laberIdL);
}

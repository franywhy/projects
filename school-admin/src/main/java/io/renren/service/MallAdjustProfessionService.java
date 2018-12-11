package io.renren.service;

import io.renren.entity.MallAdjustProfessionEntity;
import io.renren.pojo.MallAdjustProfessionPOJO;

import java.util.List;
import java.util.Map;

/**
 * 特殊情况-转专业
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-08-11 15:00:13
 */
public interface MallAdjustProfessionService {
	
	List<MallAdjustProfessionPOJO> queryPojoList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(MallAdjustProfessionEntity mallAdjustProfessionEntity);
}

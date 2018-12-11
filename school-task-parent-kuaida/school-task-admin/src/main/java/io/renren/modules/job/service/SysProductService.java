package io.renren.modules.job.service;

import java.util.List;
import java.util.Map;

import io.renren.modules.job.entity.SysProductEntity;

/**
 * 产品线
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-27 17:12:41
 */
public interface SysProductService {
	
		
	SysProductEntity queryObject(Map<String, Object> map);

	Float queryCoefficient(Long productId);

    Float queryRecordEfficient(Long productId);
}

package io.renren.service;

import io.renren.entity.SysProductEntity;

import java.util.List;
import java.util.Map;

/**
 * 产品线
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-27 17:12:41
 */
public interface SysProductService {
	
		
	SysProductEntity queryObject(Map<String, Object> map);
	
	List<SysProductEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysProductEntity sysProduct);
	
	void update(SysProductEntity sysProduct);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

	void pause(Long[] productIds);

	void resume(Long[] productIds);

	/**
	 * 弹窗列表
	 * @param map
	 * @return
	 */
	List<SysProductEntity> queryList1(Map<String, Object> map);

	/**
	 * 弹窗总数
	 * @param map
	 * @return
	 */
	int queryTotal1(Map<String, Object> map);
	
	long getProductIdByProductName(Map<String, Object> map);

	Float queryCoefficient(Long productId);
		
}

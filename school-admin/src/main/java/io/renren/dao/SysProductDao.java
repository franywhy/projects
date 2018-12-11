package io.renren.dao;

import io.renren.entity.SysProductEntity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 产品线
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-27 17:12:41
 */
public interface SysProductDao extends BaseMDao<SysProductEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	/**
	 * 弹窗列表
	 */
	List<SysProductEntity> queryList1(Map<String, Object> map);

	/**
	 * 弹窗总数
	 * @param map
	 * @return
	 */
	int queryTotal1(Map<String, Object> map);
	
	long getProductIdByProductName(Map<String, Object> map);
	
	SysProductEntity queryByclassplanLiveId(Map<String, Object> map);

	Float queryCoefficient(@Param("productId")Long productId);
}

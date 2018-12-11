package com.hqjy.pay.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.hqjy.pay.PayOrderEntity;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-06 18:29:01
 */
@Mapper
public interface PayOrderDao extends BaseDao<PayOrderEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}

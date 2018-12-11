package com.hqjy.pay.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.hqjy.pay.PayLogEntity;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-11 11:57:44
 */
@Mapper
public interface PayLogDao extends BaseDao<PayLogEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	int queryLogExist(Map<String,Object> map);
}

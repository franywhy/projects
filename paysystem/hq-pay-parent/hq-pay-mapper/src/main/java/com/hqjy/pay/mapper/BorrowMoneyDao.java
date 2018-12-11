package com.hqjy.pay.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.hqjy.pay.BorrowMoneyEntity;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-28 09:22:10
 */
@Mapper
public interface BorrowMoneyDao extends BaseDao<BorrowMoneyEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}

package com.hq.learningapi.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-27 09:58:34
 */
@Repository
public interface SysProductDao {
	
	Map<String,Object> queryByProductId(@Param("productId") Long productId);
	
}

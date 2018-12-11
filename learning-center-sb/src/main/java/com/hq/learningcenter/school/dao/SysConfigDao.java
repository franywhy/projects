package com.hq.learningcenter.school.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 常量表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-27 09:58:34
 */
@Repository
public interface SysConfigDao{
	
	String queryByKey(@Param("key") String key);
	
}

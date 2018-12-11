package com.hq.learningcenter.school.service;

import org.apache.ibatis.annotations.Param;

import com.hq.learningcenter.school.entity.SysProductEntity;

public interface ProductService {
	
	/**
	 * 根据classplanLiveId获取产品线信息
	 * @param classplanLiveId
	 * @return
	 */
	SysProductEntity queryByclassplanLiveId(@Param("classplanLiveId") String classplanLiveId);
	
}

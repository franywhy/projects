package io.renren.service;

import io.renren.entity.SexEntity;

import java.util.List;
import java.util.Map;

/**
 * 性别表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-27 14:49:25
 */
public interface SexService {
	
	List<SexEntity> findSexList();
	
}

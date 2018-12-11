package com.school.service;

import com.school.entity.LcBusinessMenuEntity;

import java.util.List;
import java.util.Map;

/**
 * 业务-菜单关联表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-08 19:27:03
 */
public interface LcBusinessMenuService {
	
	LcBusinessMenuEntity queryByBusinessId(Map<String, Object> map);
		
}

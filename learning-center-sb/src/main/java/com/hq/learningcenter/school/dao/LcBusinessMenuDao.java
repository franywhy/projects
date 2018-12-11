package com.hq.learningcenter.school.dao;

import com.hq.learningcenter.school.entity.LcBusinessMenuEntity;

import java.util.Map;

/**
 * 业务-菜单关联表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-08 19:27:03
 */
public interface LcBusinessMenuDao{
	
	LcBusinessMenuEntity queryByBusinessId(Map<String, Object> map);
}

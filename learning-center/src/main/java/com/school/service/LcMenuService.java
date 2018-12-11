package com.school.service;

import java.util.List;
import java.util.Map;

import com.school.pojo.LcMenuPOJO;

public interface LcMenuService {

	/**
	 * 根据业务线查询左侧菜单
	 * @param businessId
	 * @return
	 */
	List<LcMenuPOJO> queryLcMenu(String businessId);

	//查询app的学员菜单
    List<LcMenuPOJO> queryLcUsermenu(Map<String, Object> map);
}

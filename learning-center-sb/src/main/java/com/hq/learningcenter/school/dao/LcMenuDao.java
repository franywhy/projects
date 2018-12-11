package com.hq.learningcenter.school.dao;

import java.util.List;
import java.util.Map;

import com.hq.learningcenter.school.pojo.LcMenuPOJO;
import org.apache.ibatis.annotations.Param;

/**
 * 订单
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-27 09:58:34
 */
public interface LcMenuDao{
	/**
	 * 根据业务线查询左侧菜单
	 * @param businessId
	 * @return
	 */
	List<LcMenuPOJO> queryLcMenu(@Param("businessId") String businessId);

	//查询学员菜单栏
    List<LcMenuPOJO> queryLcUsermenu(Map<String, Object> map);
}

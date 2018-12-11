package com.hq.learningapi.dao;


import com.hq.learningapi.pojo.CourseOlivePOJO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 公开课
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 15:01:23
 */
public interface CourseOliveDao {

	List<CourseOlivePOJO> queryPojoList(Map<String, Object> map);

	Map<String,Object> queryPojoObject(@Param("oliveId") Long oliveId);

	List<Map> queryMapList(Map<String, Object> map);

	Boolean checkAuthority(@Param("authorityId") int authorityId, @Param("ncCommodityIdList") Set<String> ncCommodityIdList);

}

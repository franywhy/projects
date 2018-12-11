package io.renren.dao;

import io.renren.entity.CourseOliveEntity;
import io.renren.pojo.CourseOlivePOJO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 公开课
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 15:01:23
 */
public interface CourseOliveDao extends BaseMDao<CourseOliveEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	List<CourseOlivePOJO> queryPojoList(Map<String, Object> map);

	CourseOlivePOJO queryPojoObject(Map<String, Object> map);

	int updatePushStatus(Map<String, Object> map);

	Map<String, Object> queryMsgMap(Long oliveId);

    String queryCountByPushTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}

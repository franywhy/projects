package io.renren.dao;

import io.renren.entity.CourseAbnormalOrderEntity;
import io.renren.pojo.CourseAbnormalOrderPOJO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 休学失联记录单
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-19 09:26:43
 */
public interface CourseAbnormalOrderDao extends BaseMDao<CourseAbnormalOrderEntity> {

    List<CourseAbnormalOrderPOJO> queryPojoList(Map<String, Object> map);

	CourseAbnormalOrderPOJO queryPojoObject(@Param(value="id")Long id);

	CourseAbnormalOrderPOJO verifyStatus(Map<String, Object> map);

	int updateCancelBatch(Map<String, Object> map);
}

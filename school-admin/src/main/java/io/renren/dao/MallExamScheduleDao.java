package io.renren.dao;

import io.renren.entity.MallExamScheduleEntity;
import io.renren.pojo.examschedule.ExamSchedulePOJO;

import java.util.List;
import java.util.Map;

/**
 * 考试时刻表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-24 16:39:34
 */
public interface MallExamScheduleDao extends BaseDao<MallExamScheduleEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	/**
	 * 查询列表数据
	 * @param map
	 * @return
	 */
	List<ExamSchedulePOJO> queryPojoList(Map<String, Object> map);

	/**
	 * 根据id查询信息
	 * @param map
	 * @return
	 */
	ExamSchedulePOJO queryPojoObject(Map<String, Object> map);
	
	/**
	 * 查询status=1，dr=0的考试时间段列表
	 * @param map
	 * @return
	 */
	List<ExamSchedulePOJO> queryPojoList1(Map<String, Object> map);
	
	/**
	 * 根据param查询信息
	 * @param map
	 * @return
	 */
	MallExamScheduleEntity queryPojoByParam(Map<String, Object> map);
}

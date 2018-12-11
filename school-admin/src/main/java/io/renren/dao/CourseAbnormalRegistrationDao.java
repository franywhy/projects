package io.renren.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import io.renren.entity.CourseAbnormalRegistrationEntity;
import io.renren.pojo.CourseAbnormalRegistrationPOJO;

/**
 * 报考档案表
 * @author vince
 * @date 2018-03-28 09:21:52
 */
@Mapper
public interface CourseAbnormalRegistrationDao extends BaseDao<CourseAbnormalRegistrationEntity> {
	
	
	/**
	 * 根据ID查询实体
	 */
	CourseAbnormalRegistrationEntity queryObject(Long value);
	
	/**
	 * 查询报考档案
	 */
	List<CourseAbnormalRegistrationPOJO> queryRegistrationList(Map<String, Object> map);
	
	/**
	 * 查询报考档案Lay弹窗数据
	 */
	List<CourseAbnormalRegistrationPOJO> queryRegistrationLayList(Map<String, Object> map);
	
	/**
	 * 批量更新
	 */
	void updateBatch(Map<String, Object> map);
	
	/**
	 * 查询报考档案实体
	 */
	List<CourseAbnormalRegistrationEntity> queryRegistrationEntity(Map<String, Object> map);
	
	/**
	 * 弹窗总条数
	 */
	int queryLayTotal(Map<String, Object> map);
}

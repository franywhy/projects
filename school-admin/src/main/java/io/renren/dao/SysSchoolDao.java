package io.renren.dao;

import org.apache.ibatis.annotations.Param;

import io.renren.entity.SysSchoolEntity;

/**
 * 校区管理档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-01-29 10:09:52
 */
public interface SysSchoolDao extends BaseDao<SysSchoolEntity> {

	SysSchoolEntity queryObjectByCity(@Param("city")String city);

	SysSchoolEntity queryObjectByNcId(@Param("nc_id")String nc_id);

	void deleteByNcId(@Param("nc_id")String nc_id);
	
}

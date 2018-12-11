package io.renren.service;

import io.renren.entity.SysSchoolEntity;

import java.util.List;
import java.util.Map;

/**
 * 校区管理档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-01-29 10:09:52
 */
public interface SysSchoolService {
	
	SysSchoolEntity queryObject(Map<String, Object> map);
	
	List<SysSchoolEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysSchoolEntity sysSchool);
	
	void update(SysSchoolEntity sysSchool);
	
	void delete(Long id);
	
	void deleteBatch(Map<String, Object> map);

	SysSchoolEntity queryObjectByCity(String city);

	SysSchoolEntity queryObjectByNcId(String nc_id);

	void deleteByNcId(String nc_id);
}

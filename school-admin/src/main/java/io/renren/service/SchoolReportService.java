package io.renren.service;


import io.renren.entity.SchoolReportEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-03-19 09:17:36
 */
public interface SchoolReportService {
	
	SchoolReportEntity queryObject(Long id);
	
	List<SchoolReportEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SchoolReportEntity schoolReport);
	
	void update(SchoolReportEntity schoolReport);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

    List<SchoolReportEntity> getDetailsByDate(String startDate,String endDate,int type);
}

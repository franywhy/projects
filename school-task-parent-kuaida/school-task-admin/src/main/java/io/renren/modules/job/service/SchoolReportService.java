package io.renren.modules.job.service;


import java.util.Date;
import java.util.List;
import java.util.Map;

import io.renren.modules.job.entity.SchoolReportEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-03-19 09:17:36
 */
public interface SchoolReportService {

	void save(SchoolReportEntity schoolReport);

    List<SchoolReportEntity> getDetailsByDate(String startDate, String endDate, int type);
}

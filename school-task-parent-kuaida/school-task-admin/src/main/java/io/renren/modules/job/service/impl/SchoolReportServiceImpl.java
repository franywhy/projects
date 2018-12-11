package io.renren.modules.job.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.modules.job.dao.SchoolReportDao;
import io.renren.modules.job.entity.SchoolReportEntity;
import io.renren.modules.job.service.SchoolReportService;

import java.util.Date;
import java.util.List;
import java.util.Map;




@Service("schoolReportService")
public class SchoolReportServiceImpl implements SchoolReportService {
	@Autowired
	private SchoolReportDao schoolReportDao;

	@Override
	public void save(SchoolReportEntity schoolReport){
		schoolReportDao.save(schoolReport);
	}

    @Override
    public List<SchoolReportEntity> getDetailsByDate(String startDate,String endDate,int type) {
        return schoolReportDao.getDetailsByDate(startDate,endDate,type);
    }

}

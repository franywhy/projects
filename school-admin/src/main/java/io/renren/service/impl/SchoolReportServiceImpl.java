package io.renren.service.impl;

import io.renren.dao.SchoolReportDao;
import io.renren.entity.SchoolReportEntity;
import io.renren.service.SchoolReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;




@Service("schoolReportService")
public class SchoolReportServiceImpl implements SchoolReportService {
	@Autowired
	private SchoolReportDao schoolReportDao;
	
	@Override
	public SchoolReportEntity queryObject(Long id){
		return schoolReportDao.queryObject(id);
	}
	
	@Override
	public List<SchoolReportEntity> queryList(Map<String, Object> map){
		return schoolReportDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return schoolReportDao.queryTotal(map);
	}
	
	@Override
	public void save(SchoolReportEntity schoolReport){
		schoolReportDao.save(schoolReport);
	}
	
	@Override
	public void update(SchoolReportEntity schoolReport){
		schoolReportDao.update(schoolReport);
	}
	
	@Override
	public void delete(Long id){
		schoolReportDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		schoolReportDao.deleteBatch(ids);
	}

    @Override
    public List<SchoolReportEntity> getDetailsByDate(String startDate,String endDate,int type) {
        return schoolReportDao.getDetailsByDate(startDate,endDate,type);
    }

}

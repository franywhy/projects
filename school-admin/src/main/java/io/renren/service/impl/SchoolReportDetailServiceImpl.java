package io.renren.service.impl;

import io.renren.dao.SchoolReportDetailDao;
import io.renren.entity.SchoolReportDetailEntity;
import io.renren.pojo.ClassPlanLivesDetailPOJO;
import io.renren.pojo.SchoolReportUserMessagePOJO;
import io.renren.service.SchoolReportDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;




@Service("schoolReportDetailService")
public class SchoolReportDetailServiceImpl implements SchoolReportDetailService {
	@Autowired
	private SchoolReportDetailDao schoolReportDetailDao;
	
	@Override
	public SchoolReportDetailEntity queryObject(Long id){
		return schoolReportDetailDao.queryObject(id);
	}
	
	@Override
	public List<SchoolReportDetailEntity> queryList(Map<String, Object> map){
		return schoolReportDetailDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return schoolReportDetailDao.queryTotal(map);
	}
	
	@Override
	public void save(SchoolReportDetailEntity schoolReportDetail){
		schoolReportDetailDao.save(schoolReportDetail);
	}
	
	@Override
	public void update(SchoolReportDetailEntity schoolReportDetail){
		schoolReportDetailDao.update(schoolReportDetail);
	}
	
	@Override
	public void delete(Long id){
		schoolReportDetailDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		schoolReportDetailDao.deleteBatch(ids);
	}

    @Override
    public List<SchoolReportUserMessagePOJO> queryUserMessage() {
        return schoolReportDetailDao.queryUserMessage();
    }

    @Override
    public List<String> classPlanIdByOrder(Long orderId) {
        return schoolReportDetailDao.classPlanIdByOrder(orderId);
    }

    @Override
    public List<ClassPlanLivesDetailPOJO> getClassPlanLivesDetail(Long orderId,Long userId, String startDateStr, String endDateStr) {
        return schoolReportDetailDao.getClassPlanLivesDetail(orderId,userId,startDateStr,endDateStr);
    }


}

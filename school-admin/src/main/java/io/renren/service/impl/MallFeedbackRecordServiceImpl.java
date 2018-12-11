package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.MallFeedbackRecordDao;
import io.renren.entity.MallFeedbackRecordEntity;
import io.renren.service.MallFeedbackRecordService;
import io.renren.utils.Constant;



@Service("mallFeedbackRecordService")
public class MallFeedbackRecordServiceImpl implements MallFeedbackRecordService {
	@Autowired
	private MallFeedbackRecordDao mallFeedbackRecordDao;
	
	@Override
	public MallFeedbackRecordEntity queryObject(Long id){
		return mallFeedbackRecordDao.queryObject(id);
	}
	
	@Override
	public List<MallFeedbackRecordEntity> queryList(Map<String, Object> map){
		return mallFeedbackRecordDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return mallFeedbackRecordDao.queryTotal(map);
	}
	
	@Override
	public void save(MallFeedbackRecordEntity mallFeedbackRecord){
		mallFeedbackRecordDao.save(mallFeedbackRecord);
	}
	
	@Override
	public void update(MallFeedbackRecordEntity mallFeedbackRecord){
		mallFeedbackRecordDao.update(mallFeedbackRecord);
	}
	
	@Override
	public void delete(Long id){
		mallFeedbackRecordDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		mallFeedbackRecordDao.deleteBatch(ids);
	}
	@Override
	public void pause(Long[] ids){
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.PAUSE.getValue());
		mallFeedbackRecordDao.updateBatch(map);
	}
		
	@Override
	public void resume(Long[] ids){
	    Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.RESUME.getValue());
		mallFeedbackRecordDao.updateBatch(map);
	}
}

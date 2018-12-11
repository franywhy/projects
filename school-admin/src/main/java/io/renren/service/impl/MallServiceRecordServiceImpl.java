package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.MallServiceRecordDao;
import io.renren.entity.MallServiceRecordEntity;
import io.renren.service.MallServiceRecordService;
import io.renren.utils.Constant;



@Service("mallServiceRecordService")
public class MallServiceRecordServiceImpl implements MallServiceRecordService {
	@Autowired
	private MallServiceRecordDao mallServiceRecordDao;
	
	@Override
	public MallServiceRecordEntity queryObject(Long id){
		return mallServiceRecordDao.queryObject(id);
	}
	
	@Override
	public List<MallServiceRecordEntity> queryList(Map<String, Object> map){
		return mallServiceRecordDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return mallServiceRecordDao.queryTotal(map);
	}
	
	@Override
	public void save(MallServiceRecordEntity mallServiceRecord){
		mallServiceRecordDao.save(mallServiceRecord);
	}
	
	@Override
	public void update(MallServiceRecordEntity mallServiceRecord){
		mallServiceRecordDao.update(mallServiceRecord);
	}
	
	@Override
	public void delete(Long id){
		mallServiceRecordDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		mallServiceRecordDao.deleteBatch(ids);
	}
	@Override
	public void pause(Long[] ids){
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.PAUSE.getValue());
		mallServiceRecordDao.updateBatch(map);
	}
		
	@Override
	public void resume(Long[] ids){
	    Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.RESUME.getValue());
		mallServiceRecordDao.updateBatch(map);
	}
}

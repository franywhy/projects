package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.PushTimeTemplateDao;
import io.renren.entity.PushTimeTemplateEntity;
import io.renren.service.PushTimeTemplateService;
import io.renren.utils.Constant;



@Service("pushTimeTemplateService")
public class PushTimeTemplateServiceImpl implements PushTimeTemplateService {
	@Autowired
	private PushTimeTemplateDao pushTimeTemplateDao;
	
	@Override
	public PushTimeTemplateEntity queryObject(Map<String, Object> map){
		return pushTimeTemplateDao.queryObject(map);
	}
	
	@Override
	public List<PushTimeTemplateEntity> queryList(Map<String, Object> map){
		return pushTimeTemplateDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return pushTimeTemplateDao.queryTotal(map);
	}
	
	@Override
	public void save(PushTimeTemplateEntity pushTimeTemplate){
		pushTimeTemplateDao.save(pushTimeTemplate);
	}
	
	@Override
	public void update(PushTimeTemplateEntity pushTimeTemplate){
		pushTimeTemplateDao.update(pushTimeTemplate);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		pushTimeTemplateDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		pushTimeTemplateDao.deleteBatch(map);
	}
	
	
}

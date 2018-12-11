package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.AppFeedbackDao;
import io.renren.entity.AppFeedbackEntity;
import io.renren.service.AppFeedbackService;
import io.renren.utils.Constant;



@Service("appFeedbackService")
public class AppFeedbackServiceImpl implements AppFeedbackService {
	@Autowired
	private AppFeedbackDao appFeedbackDao;
	
	@Override
	public AppFeedbackEntity queryObject(Map<String, Object> map){
		return appFeedbackDao.queryObject(map);
	}
	
	@Override
	public List<AppFeedbackEntity> queryList(Map<String, Object> map){
		return appFeedbackDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return appFeedbackDao.queryTotal(map);
	}
	
	@Override
	public void save(AppFeedbackEntity appFeedback){
		appFeedbackDao.save(appFeedback);
	}
	
	@Override
	public void update(AppFeedbackEntity appFeedback){
		appFeedbackDao.update(appFeedback);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		appFeedbackDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		appFeedbackDao.deleteBatch(map);
	}
	
	
}

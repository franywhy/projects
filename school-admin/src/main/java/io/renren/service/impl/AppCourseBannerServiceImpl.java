package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.AppCourseBannerDao;
import io.renren.entity.AppCourseBannerEntity;
import io.renren.pojo.AppCourseBannerPOJO;
import io.renren.service.AppCourseBannerService;
import io.renren.utils.Constant;



@Service("appCourseBannerService")
public class AppCourseBannerServiceImpl implements AppCourseBannerService {
	@Autowired
	private AppCourseBannerDao appCourseBannerDao;
	
	@Override
	public AppCourseBannerEntity queryObject(Map<String, Object> map){
		return appCourseBannerDao.queryObject(map);
	}
	
	@Override
	public List<AppCourseBannerEntity> queryList(Map<String, Object> map){
		return appCourseBannerDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return appCourseBannerDao.queryTotal(map);
	}
	
	@Override
	public void save(AppCourseBannerEntity appCourseBanner){
		appCourseBannerDao.save(appCourseBanner);
	}
	
	@Override
	public void update(AppCourseBannerEntity appCourseBanner){
		appCourseBannerDao.update(appCourseBanner);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		appCourseBannerDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		appCourseBannerDao.deleteBatch(map);
	}

	@Override
	public AppCourseBannerPOJO queryObjectPOJO(Map<String, Object> map) {
		return this.appCourseBannerDao.queryObjectPOJO(map);
	}

	@Override
	public List<AppCourseBannerPOJO> queryListPOJO(Map<String, Object> map) {
		return this.appCourseBannerDao.queryListPOJO(map);
	}
	
	
}

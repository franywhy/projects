package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import io.renren.dao.AppBannerDao;
import io.renren.entity.AppBannerEntity;
import io.renren.pojo.AppBannerPOJO;
import io.renren.service.AppBannerService;
import io.renren.utils.Constant;



@Service("appBannerService")
public class AppBannerServiceImpl implements AppBannerService {
	@Autowired
	private AppBannerDao appBannerDao;
	
	@Override
	public AppBannerEntity queryObject(Map<String, Object> map){
		return appBannerDao.queryObject(map);
	}
	
	@Override
	public List<AppBannerEntity> queryList(Map<String, Object> map){
		return appBannerDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return appBannerDao.queryTotal(map);
	}
	
	@Override
	public void save(AppBannerEntity appBanner){
		//默认状态
	    appBanner.setStatus(1);
		//创建时间
		appBanner.setCreateTime(new Date());
		//修改时间
		appBanner.setModifyTime(appBanner.getCreateTime());
		appBannerDao.save(appBanner);
	}
	
	@Override
	public void update(AppBannerEntity appBanner){
		//修改时间
		appBanner.setModifyTime(new Date());
		appBannerDao.update(appBanner);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		appBannerDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		appBannerDao.deleteBatch(map);
	}

	/**
	 * 查询列表数据
	 */
	@Override
	public List<AppBannerPOJO> queryPojoList(Map<String, Object> map) {
		return appBannerDao.queryPojoList(map);
	}

	@Override
	public AppBannerPOJO queryPojoObject(Map<String, Object> map) {
		return appBannerDao.queryPojoObject(map);
	}

	@Override
	public void pause(Long[] ids) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.PAUSE.getValue());
    	appBannerDao.updateBatch(map);
	}

	@Override
	public void resume(Long[] ids) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.RESUME.getValue());
    	//map.put("modifiedTime", new Date());
    	appBannerDao.updateBatch(map);
	}

	@Override
	public int checkProfession(long id) {
		return appBannerDao.checkProfession(id);
	}
	
	
}

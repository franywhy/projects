package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.WebBannerDao;
import io.renren.entity.WebBannerEntity;
import io.renren.service.WebBannerService;
import io.renren.utils.Constant;



@Service("webBannerService")
public class WebBannerServiceImpl implements WebBannerService {
	@Autowired
	private WebBannerDao webBannerDao;
	
	@Override
	public WebBannerEntity queryObject(Map<String, Object> map){
		return webBannerDao.queryObject(map);
	}
	
	@Override
	public List<WebBannerEntity> queryList(Map<String, Object> map){
		return webBannerDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return webBannerDao.queryTotal(map);
	}
	
	@Override
	public void save(WebBannerEntity webBanner){
		webBannerDao.save(webBanner);
	}
	
	@Override
	public void update(WebBannerEntity webBanner){
		webBannerDao.update(webBanner);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		webBannerDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		webBannerDao.deleteBatch(map);
	}

	@Override
	public void pause(Long[] ids) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.PAUSE.getValue());
    	webBannerDao.updateBatch(map);
	}

	@Override
	public void resume(Long[] ids) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.RESUME.getValue());
    	//map.put("modifiedTime", new Date());
    	webBannerDao.updateBatch(map);
	}

}

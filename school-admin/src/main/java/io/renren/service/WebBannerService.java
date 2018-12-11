package io.renren.service;

import io.renren.entity.WebBannerEntity;
import java.util.List;
import java.util.Map;

/**
 * banner档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-24 15:47:07
 */
public interface WebBannerService {
	
		
	WebBannerEntity queryObject(Map<String, Object> map);
	
	List<WebBannerEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(WebBannerEntity webBanner);
	
	void update(WebBannerEntity webBanner);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

	void pause(Long[] ids);

	void resume(Long[] ids);

}

package io.renren.service;

import io.renren.entity.AppBannerEntity;
import io.renren.pojo.AppBannerPOJO;

import java.util.List;
import java.util.Map;

/**
 * 移动端banner档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-20 14:55:22
 */
public interface AppBannerService {
	
		
	AppBannerEntity queryObject(Map<String, Object> map);
	
	List<AppBannerEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(AppBannerEntity appBanner);
	
	void update(AppBannerEntity appBanner);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

	/**
	 * 查询列表数据
	 * @param map
	 * @return
	 */
	List<AppBannerPOJO> queryPojoList(Map<String, Object> map);

	/**
	 * 查询信息
	 * @param map
	 * @return
	 */
	AppBannerPOJO queryPojoObject(Map<String, Object> map);

	void pause(Long[] ids);

	void resume(Long[] ids);
	
	//判断是否有专业的引用
		 int checkProfession(long id);
		
}

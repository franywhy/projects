package io.renren.service;

import io.renren.entity.MallStudioEntity;
import io.renren.pojo.studio.StudioPOJO;

import java.util.List;
import java.util.Map;

/**
 * 直播室档案表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-22 11:10:34
 */
public interface MallStudioService {
	
	MallStudioEntity queryObject(Map<String, Object> map);
	
	List<MallStudioEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(MallStudioEntity mallStudio);
	
	void update(MallStudioEntity mallStudio);
	
	void delete(Long studioId);
	
	void deleteBatch(Long[] studioIds);
	
	void pause(Long[] studioIds);
	
	void resume(Long[] studioIds);

	/**
	 * 直播室列表
	 * @param map
	 * @return
	 */
	List<MallStudioEntity> findStudioList(Map<String, Object> map);
	
	/**
	 * 查询status=1，dr=0的直播室列表
	 * @param map
	 * @return
	 */
	List<MallStudioEntity> queryList1(Map<String, Object> map);
	
	/**
	 * 查询status=1，dr=0的直播室列表总数
	 * @param map
	 * @return
	 */
	int queryTotal1(Map<String, Object> map);

}

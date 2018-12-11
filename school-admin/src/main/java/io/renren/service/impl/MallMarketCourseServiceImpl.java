package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.MallMarketCourseDao;
import io.renren.entity.MallMarketCourseEntity;
import io.renren.pojo.MallMarketCoursePOJO;
import io.renren.service.MallMarketCourseService;
import io.renren.utils.Constant;



@Service("mallMarketCourseService")
public class MallMarketCourseServiceImpl implements MallMarketCourseService {
	@Autowired
	private MallMarketCourseDao mallMarketCourseDao;
	
	@Override
	public MallMarketCourseEntity queryObject(Map<String, Object> map){
		return mallMarketCourseDao.queryObject(map);
	}
	
	@Override
	public List<MallMarketCourseEntity> queryList(Map<String, Object> map){
		return mallMarketCourseDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return mallMarketCourseDao.queryTotal(map);
	}
	
	@Override
	public void save(MallMarketCourseEntity mallMarketCourse){
		mallMarketCourseDao.save(mallMarketCourse);
	}
	
	@Override
	public void update(MallMarketCourseEntity mallMarketCourse){
		mallMarketCourseDao.update(mallMarketCourse);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		mallMarketCourseDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		mallMarketCourseDao.deleteBatch(map);
	}

	@Override
	public List<MallMarketCoursePOJO> queryListClassifyPOJO(Map<String, Object> map) {
		return this.mallMarketCourseDao.queryListClassifyPOJO(map);
	}

	@Override
	public int queryTotalClassify(Map<String, Object> map) {
		return this.mallMarketCourseDao.queryTotalClassify(map);
	}

	@Override
	public List<MallMarketCoursePOJO> queryListPOJO(Map<String, Object> map) {
		return this.mallMarketCourseDao.queryListPOJO(map);
	}

	@Override
	public List<MallMarketCoursePOJO> queryClassifyDownList(Map<String, Object> map) {
		return this.mallMarketCourseDao.queryClassifyDownList(map);
	}

	@Override
	public void pause(Long[] ids) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.PAUSE.getValue());
		this.mallMarketCourseDao.updateBatch(map);
	}

	@Override
	public void resume(Long[] ids) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.RESUME.getValue());
		this.mallMarketCourseDao.updateBatch(map);
	}

	@Override
	public MallMarketCoursePOJO queryObjectPOJO(Map<String, Object> map) {
		return this.mallMarketCourseDao.queryObjectPOJO(map);
	}
	
	
}

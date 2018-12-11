package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.MallGoodsDetailsDao;
import io.renren.entity.MallGoodsDetailsEntity;
import io.renren.service.MallGoodsDetailsService;
import io.renren.utils.Constant;



@Service("mallGoodsDetailsService")
public class MallGoodsDetailsServiceImpl implements MallGoodsDetailsService {
	@Autowired
	private MallGoodsDetailsDao mallGoodsDetailsDao;
	
//	@Override
//	public List<MallGoodsDetailsEntity> queryObject(Long id){
//		return mallGoodsDetailsDao.queryList(id);
//	}
	
	@Override
	public List<MallGoodsDetailsEntity> queryList(Map<String, Object> map){
		return mallGoodsDetailsDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return mallGoodsDetailsDao.queryTotal(map);
	}
	
	@Override
	public void save(MallGoodsDetailsEntity mallGoodsDetails){
		mallGoodsDetails.setDr(0);
		mallGoodsDetailsDao.save(mallGoodsDetails);
	}
	
	@Override
	public void update(MallGoodsDetailsEntity mallGoodsDetails){
		mallGoodsDetailsDao.update(mallGoodsDetails);
	}
	
	@Override
	public void delete(Long id){
		mallGoodsDetailsDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		mallGoodsDetailsDao.delete(map);
	}
	@Override
	public void pause(Long[] ids){
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.PAUSE.getValue());
		mallGoodsDetailsDao.updateBatch(map);
	}
		
	@Override
	public void resume(Long[] ids){
	    Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.RESUME.getValue());
		mallGoodsDetailsDao.updateBatch(map);
	}

	@Override
	public List<Map<String, Object>> queryAreaByGoodId(Map<String, Object> map) {
		return this.mallGoodsDetailsDao.queryAreaByGoodId(map);
	}

	@Override
	public List<Map<String, Object>> queryListMap(Map<String, Object> map) {
		return this.mallGoodsDetailsDao.queryListMap(map);
	}

	@Override
	public void deleteBatchNotIn(Map<String, Object> map) {
		this.mallGoodsDetailsDao.deleteBatchNotIn(map);
	}

	@Override
	public List<Long> getGoods(Long courseId, String schoolId) {
		return this.mallGoodsDetailsDao.getGoods(courseId, schoolId);
	}

	@Override
	public List<Long> getCourses(Long goodsInfoId, String schoolId) {
		return this.mallGoodsDetailsDao.getCourses(goodsInfoId, schoolId);
	}

	@Override
	public long getAreaIdByGoodsId(Map<String, Object> map) {
		return this.mallGoodsDetailsDao.getAreaIdByGoodsId(map);
	}
	/**
	 * 查询商品某一个省份下的课程
	 * @param map schoolId:校区PK id:商品ID areaId:省份ID
	 * @return
	 */
	@Override
	public List<MallGoodsDetailsEntity> selectAreaCouresList(Map<String, Object> map) {
		return this.mallGoodsDetailsDao.selectAreaCouresList(map);
	}
}
